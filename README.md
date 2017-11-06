# food-delivery-app

## Functional description
User can search a restaurant based on restaurant name. Then user can order food by
choosing different menu item, quantity and add a note about his/her diet restrictions and etc.
User can also fills in the delivery address. After user places an order, the order should
contain food items user ordered, quantity, price and order time. User then needs to pay for
his/her order by providing credit card number, expiration date, and security code. After
payment is made successfully, it should return payment ID, timestamp and then the order is
considered as completed so the user can see the estimated delivery time.

## Design
The app has three microservices: **food-order-service**, **order-processing-service**, and **order-payment-service**. 
It incorporated Netflix Eureka as service registration and discovery, and also used Hystrix as circuit breaker. <br>

**food-order-service** allows users to search a restaurant by certain criteria and place orders. 
Since there is no front-end, the order is generated randomly when user send any request to
http://localhost:9001/placeOrder. Restaurants information and orders are stored in the database(MongoDB). <br>

**order-processing-service** takes the order and publish it to the "orders" queue in RabbitMQ. <br>

**order-payment-service** subscribes the "orders" queue. It de-serializes the order information and verifies
whether the payment is valid or not. If the payment is valid, order is confirmed and persisted into database(MySQL).

**food-order-service**, **order-processing-service** and **order-payment-service** is deployed on port 9001, 9002 and 9003, respectively. **Eureka** and **Hystrix-dashboard** is deployed on port 8761 and 7979, respectively. **RabbitMQ** management interface is on port 15672.

## How to run the app
### clone project
```
git clone https://github.com/zjuzhanxf/food-delivery-app.git
```

### build
```
mvn clean install
```

### spin up containers
Start MySQL, MongoDB and RabbitMQ containers.<br>
MySQL: store payment information in order-payment-service. <br>
MongoDB: store restaurant information and order information in food-order-service. <br>
RabbitMQ: communication between order-processing-service(publisher) and order-payment-service(subscriber)
```
docker-compose up
```

### start eureka
return to food-delivery-app folder, then
```
cd platform/eureka
java -jar target/eureka-0.0.1-SNAPSHOT.jar
```

### start hystrix-dashboard
return to food-delivery-app folder, then
```
cd platform/hystrix-dashboard
java -jar target/hystrix-dashboard-1.4.5.RELEASE.jar
```

### start food-order-service
return to food-delivery-app folder, then
```
cd food-order-service/
java -jar target/food-order-service-1.0-SNAPSHOT.jar
```

### start order-processing-service
return to food-delivery-app folder, then
```
cd order-processing-service/
java -jar target/order-processing-service-1.0-SNAPSHOT.jar
```

### start order-payment-service
return to food-delivery-app folder, then
```
cd order-payment-service/
java -jar target/order-payment-service-1.0-SNAPSHOT.jar
```

### upload restaurants information
Use Postman Chrome app to post restaurant information to MongoDB. <br>
Restaurant information is stored in file food-delivery-app/food-order-service/src/main/resources/restaurants-init.json <br>
Open the browser, go to localhost:9001, which is redirected to HAL browser. It will list all the restaurant information. <br>

### place order
Open web browser. Enter "localhost:9001/placeOrder" in address field and hit Enter. <br>
Each time the page is refreshed, a new order is placed. <br>
Go to localhost:9001, which is redirected to HAL browser. It will list all the order information. <br>
Also pay attention to the log from each service.

#### sample log from food-order-service
```
2017-11-05 23:46:25.043  INFO 1808 --- [erServiceImpl-1] d.service.impl.DefaultOrderServiceImpl   : Order is placed: Order(id=5a00134e77c801909b74f591, fromRestaurant=Sama Uyghur Cuisine, orderItemList=[MenuItem(name=Xinjiang Big Plate Chicken, price=12.99)], orderTime=Sun Nov 05 23:46:21 PST 2017, totalPrice=12.99, creditCardInfo=CreditCardInfo(cardNumber=9561-2307-6938-1224, expiration=12/17, securityCode=822), note=Gluten-free, deliverAddress=1463 Galighner Terrace, Monroe, PA 45084
```
#### sample log from order-processing-service
```
2017-11-05 23:46:24.971  INFO 1809 --- [nio-9002-exec-1] demo.OrderProcessingSource               : Order is processed. Proceed to payment service.
```
#### sample log from order-payment-service (payment valid case)
In this case, the payment information is persisted into MySQL.<br>
```
2017-11-05 23:46:26.303  INFO 1811 --- [binder.orders-1] demo.OrderProcessingSink                 : [Payment Successful!] Payment ID is 17. Your credit card is charged 12.99 dollars at Sun Nov 05 23:46:25 PST 2017. Order ID is 5a00134e77c801909b74f591. Your order will be delivered in 44 minutes
```
#### sample log from order-payment-service (payment invalid case)
In this case, the payment information is **NOT** persisted into MySQL.<br>
```
2017-11-05 23:49:54.370 ERROR 1811 --- [binder.orders-1] demo.OrderProcessingSink                 : [Payment Failed!] Invalid Card! Use another card please.
```
#### When is payment failed?
The generated order has credit card number with format xxxx-xxxx-xxxx-xxxx, where "x" is a digit. <br>
If first 4 digits are larger than 1000, the card is valid and payment is successful. Otherwise, the payment fails. <br>
Example 1: Card number 0989-4792-8055-9302 is invalid, because 0989 is less than 1000. <Br>
Example 2: Card number 3421-8596-1248-7850 is valid, because 3421 is larger than 1000. 

### Rabbit-MQ management interface
Go to http://localhost:15672/#/ and login with username "guest" and password "guest". The rabbitMQ dashboard shows all the statistics of messages published and consumed.

### Eureka and Hystrix-dashboard
Go to http://localhost:8761/ to view a list of registered services.
Go to http://localhost:9001/hystrix.stream to monitor the system status.
