package demo.service.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import demo.domain.*;
import demo.service.OrderService;
import demo.service.RestaurantInfoService;
import lombok.extern.slf4j.Slf4j;
import org.fluttercode.datafactory.impl.DataFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class DefaultOrderServiceImpl implements OrderService {
    private RestaurantInfoService restaurantInfoService;
    private OrderRepository orderRepository;


    // eureka uses client side load balancing, called com.netflix.ribbon
    @Autowired
    private RestTemplate restTemplate;

//    @Value("${com.toby.order.processing.service}")
//    private String orderProcessingService;

    @Autowired
    public DefaultOrderServiceImpl(RestaurantInfoService service, OrderRepository orderRepository) {
        this.restaurantInfoService = service;
        this.orderRepository = orderRepository;
    }

    @Override
    public Order generateOrder() {
        Restaurant restaurant = restaurantInfoService.findOneRandomRestaurant();
        List<MenuItem> dishList = restaurant.getMenuItemList();
        Order order = new Order();
        int size = dishList.size();
        Random rand = new Random();
        int numDishes = rand.nextInt(size) + 1;

        // set restaurant name for order
        order.setFromRestaurant(restaurant.getName());

        // add menu item to the order
        for (int i = 0; i < numDishes; i++) {
            order.addItemToOrder(dishList.get(i));
        }

        // set total price
        order.setTotalPrice(order.calculateTotal());

        // set order time
        order.setOrderTime(new Date());

        String cardNumSegment1 = String.format("%04d", rand.nextInt(10000));
        String cardNumSegment2 = String.format("%04d", rand.nextInt(10000));
        String cardNumSegment3 = String.format("%04d", rand.nextInt(10000));
        String cardNumSegment4 = String.format("%04d", rand.nextInt(10000));
        String cardNumber = cardNumSegment1 + "-" + cardNumSegment2 + "-"
                + cardNumSegment3 + "-" + cardNumSegment4;

        String expMonth = String.format("%02d", 1 + rand.nextInt(12));
        String expYear = String.format("%02d", 17 + rand.nextInt(5));
        String expMonthYear = expMonth +"/" +expYear;

        String securityCode = String.format("%03d", rand.nextInt(1000));

        CreditCardInfo cardInfo = new CreditCardInfo(cardNumber, expMonthYear, securityCode);

        // set credit card info
        order.setCreditCardInfo(cardInfo);

        // set note
        order.setNote(generateDietRestrictionNote());

        // set delivery address
        order.setDeliverAddress(generateAddress());
        return order;
    }

    private String generateDietRestrictionNote() {
        String[] notes = {"No special note", "Gluten-free", "No Peanut", "No Onion"};
        int size = notes.length;
        Random rand = new Random();
        return notes[rand.nextInt(size)];
    }

    private String generateAddress() {
        DataFactory df = new DataFactory();

        //format: 155 Bison St, Carson, FE 29098
        return df.getAddress()+", "+df.getCity()+", "
                + df.getCity().substring(0,2).toUpperCase() +" "+ df.getNumberText(5);
    }

    @HystrixCommand(fallbackMethod = "processOrderFallBack")
    @Override
    public Order processOrder(Order newOrder) {

        String orderProcessingService = "http://order-processing-service";

        // Save the order to order database
        Order savedOrder = saveOrder(newOrder);

        // send newOrder to order-processing-service
        restTemplate.postForLocation(orderProcessingService + "/api/orders", savedOrder);
        log.info(String.format("Order is placed: %s", savedOrder));

        return newOrder;
    }


    public Order processOrderFallBack (Order newOrder) {
        log.error("Unable to process order!");
        return null;
    }

    @Override
    public Order saveOrder(Order order) {
        Order savedOrder = orderRepository.save(order);
        return savedOrder;
    }

}
