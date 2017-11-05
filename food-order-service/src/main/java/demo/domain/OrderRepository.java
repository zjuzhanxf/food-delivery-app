package demo.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by xiaofeng on 11/4/17.
 */
@RepositoryRestResource(path = "orders")
public interface OrderRepository extends PagingAndSortingRepository<Order, String> {
    Page<Order> findAll(Pageable pageable);
}
