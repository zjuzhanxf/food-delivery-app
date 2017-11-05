package demo.service;

import demo.domain.Order;

public interface OrderService {
    Order generateOrder();

    Order processOrder(Order order);

    Order saveOrder(Order order);

}
