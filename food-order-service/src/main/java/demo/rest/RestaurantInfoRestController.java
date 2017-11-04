package demo.rest;

import demo.domain.Order;
import demo.domain.Restaurant;
import demo.service.OrderService;
import demo.service.RestaurantInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RestaurantInfoRestController {

    RestaurantInfoService restaurantInfoService;
    OrderService orderService;

    @Autowired
    public RestaurantInfoRestController(RestaurantInfoService restaurantInfoService, OrderService orderService) {

        this.restaurantInfoService = restaurantInfoService;
        this.orderService = orderService;
    }

    @RequestMapping(value = "/bulkUploadRestaurants", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public List<Restaurant> upload(@RequestBody List<Restaurant> restaurantList) {
        return restaurantInfoService.saveRestaurantsInfo(restaurantList);
    }

    @RequestMapping(value = "/deleteAll", method = RequestMethod.DELETE)
    public void deleteAll() {
        restaurantInfoService.deleteAllRestaurants();
    }

    @RequestMapping(value = "/placeOrder")
    public Order createOrder() {
        Order order = orderService.generateOrder();
        return orderService.processOrder(order);
    }

}
