package demo.service.impl;

import demo.domain.Restaurant;
import demo.domain.RestaurantRepository;
import demo.service.RestaurantInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class RestaurantInfoServiceImpl implements RestaurantInfoService {
    RestaurantRepository repository;

    @Autowired
    public RestaurantInfoServiceImpl(RestaurantRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Restaurant> saveRestaurantsInfo(List<Restaurant> restaurantList) {
        return (List<Restaurant>)repository.save(restaurantList);
    }

    @Override
    public Page<Restaurant> findByCategoryOrderByRatingDesc(String category, Pageable pageable) {
        return repository.findByCategoryOrderByRatingDesc(category, pageable);
    }

    @Override
    public Restaurant findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Page<Restaurant> findAllOrderByRatingDesc(Pageable pageable) {
        return repository.findAllByOrderByRatingDesc(pageable);
    }

    @Override
    public List<Restaurant> findAllRestaurants() {
        return (List<Restaurant>) repository.findAll();
    }

    @Override
    public Restaurant findOneRandomRestaurant() {
        List<Restaurant> list = this.findAllRestaurants();
        int size = list.size();
        Random rand = new Random();

        return list.get(rand.nextInt(size));
    }

    @Override
    public void deleteAllRestaurants() {
        this.repository.deleteAll();
    }

}
