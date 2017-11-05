package demo.service.impl;

import demo.domain.Payment;
import demo.domain.PaymentRepository;
import demo.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by xiaofeng on 11/4/17.
 */

@Service
public class DefaultPaymentServiceImpl implements PaymentService {

    private PaymentRepository paymentRepository;

    @Autowired
    public DefaultPaymentServiceImpl(PaymentRepository repository) {
        this.paymentRepository = repository;
    }

    @Override
    public Payment save(Payment payment) {

        return paymentRepository.save(payment);
    }

    @Override
    public Page<Payment> findAll(Pageable pageable) {
        return paymentRepository.findAll(pageable);
    }
}
