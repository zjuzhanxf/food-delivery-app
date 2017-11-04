package demo.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "restaurants")
public interface RestaurantRepository extends PagingAndSortingRepository<Restaurant, String> {

    Page<Restaurant> findByCategoryOrderByRatingDesc(@Param("category") String category, Pageable pageable);

    Page<Restaurant> findAllByOrderByRatingDesc(Pageable pageable);

    Restaurant findById(@Param("id") String id);
}
