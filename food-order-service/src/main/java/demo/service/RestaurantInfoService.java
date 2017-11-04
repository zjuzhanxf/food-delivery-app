package demo.service;

import demo.domain.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RestaurantInfoService {
    List<Restaurant> saveRestaurantsInfo(List<Restaurant> restaurantList);

    Page<Restaurant> findByCategoryOrderByRatingDesc(String category, Pageable pageable);

    Restaurant findById(String id);

    Page<Restaurant> findAllOrderByRatingDesc(Pageable pageable);

    List<Restaurant> findAllRestaurants();

    Restaurant findOneRandomRestaurant();

    void deleteAllRestaurants();
}
