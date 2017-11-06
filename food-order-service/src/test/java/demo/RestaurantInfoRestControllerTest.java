package demo;

import demo.domain.*;
import demo.domain.MenuItem;
import demo.rest.RestaurantInfoRestController;
import demo.service.OrderService;
import demo.service.RestaurantInfoService;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by xiaofeng on 11/5/17.
 */
public class RestaurantInfoRestControllerTest {


    private RestaurantInfoService restaurantInfoService;
    private OrderService orderService;

    private RestaurantInfoRestController restaurantInfoRestController;
    List<Restaurant> inputList;


    @Before
    public void setupMock() {

        restaurantInfoService = mock(RestaurantInfoService.class);
        orderService = mock(OrderService.class);

        restaurantInfoRestController = new RestaurantInfoRestController(restaurantInfoService, orderService);
        inputList = new ArrayList<Restaurant>();
        inputList.add(generateRestaurant("In-n-out", "Fast food", 4.3));
        List<MenuItem> menuList1 = new ArrayList<>();
        menuList1.add(new MenuItem("Hamburger", 1.30));
        menuList1.add(new MenuItem("Cheese burger", 1.50));
        menuList1.add(new MenuItem("Double cheese burger", 2.50));
        inputList.get(0).setMenuItemList(menuList1);

        inputList.add(generateRestaurant("Burger King", "Fast food", 3.2));
        List<MenuItem> menuList2 = new ArrayList<>();
        menuList2.add(new MenuItem("Whopper Jr", 2.10));
        menuList2.add(new MenuItem("Whopper Sanwich", 2.10));
        menuList2.add(new MenuItem("Bacon King", 3.50));
        inputList.get(1).setMenuItemList(menuList2);

        inputList.add(generateRestaurant("PF Chang", "Asian Fusion", 3.5));
        List<MenuItem> menuList3 = new ArrayList<>();
        menuList3.add(new MenuItem("Dan Dan Noodle", 10.30));
        menuList3.add(new MenuItem("Beef fried rice", 8.99));
        menuList3.add(new MenuItem("Pan fried meat pie", 5.99));
        inputList.get(2).setMenuItemList(menuList3);
    }

    @Test
    public void whenSaveList_returnSuccess(){
        List<Restaurant> expectedListAfterSaving = new ArrayList<>();
        expectedListAfterSaving.add(generateRestaurant("In-n-out", "Fast food", 4.3));
        expectedListAfterSaving.add(generateRestaurant("Burger King", "Fast food", 3.2));
        expectedListAfterSaving.add(generateRestaurant("PF Chang", "Asian Fusion", 3.5));

        when(restaurantInfoService.saveRestaurantsInfo(inputList)).thenReturn(expectedListAfterSaving);

        assertThat(restaurantInfoRestController.upload(inputList).size()).isEqualTo(3);
        assertThat(restaurantInfoRestController.upload(inputList).get(0).getName()).isEqualTo("In-n-out");
        assertThat(restaurantInfoRestController.upload(inputList).get(1).getMenuItemList()).isEqualTo(null);
        assertThat(restaurantInfoRestController.upload(inputList).get(2).getRating()).isEqualTo(3.5);
    }

    @Test
    public void whenGenerateOrder_returnOneOrder(){

        Order order = new Order();
        order.setFromRestaurant(inputList.get(0).getName());
        order.setOrderItemList(inputList.get(0).getMenuItemList());
        order.setNote("No peanut");
        order.setTotalPrice(18.99);
        order.setCreditCardInfo(new CreditCardInfo("1234-5678-9012-3456", "12/17", "432"));

        Order newOrder = new Order();
        newOrder.setId("abc3223dsc");
        newOrder.setFromRestaurant(inputList.get(0).getName());
        newOrder.setOrderItemList(inputList.get(0).getMenuItemList());
        newOrder.setNote("No peanut");
        newOrder.setTotalPrice(18.99);
        newOrder.setCreditCardInfo(new CreditCardInfo("1234-5678-9012-3456", "12/17", "432"));

        when(orderService.generateOrder()).thenReturn(order);
        when(orderService.processOrder(order)).thenReturn(newOrder);
        assertThat(restaurantInfoRestController.createOrder().getNote()).isEqualTo("No peanut");
        assertThat(restaurantInfoRestController.createOrder().getTotalPrice()).isEqualTo(18.99);
    }

    private Restaurant generateRestaurant(String name, String category, double rating) {
        Restaurant restaurant = new Restaurant(name, category);
        restaurant.setRating(rating);

        return restaurant;
    }
}
