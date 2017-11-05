package demo.service;

import demo.domain.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by xiaofeng on 11/4/17.
 */
public interface PaymentService {
    Payment save(Payment payment);

    Page<Payment> findAll(Pageable pageable);
}
