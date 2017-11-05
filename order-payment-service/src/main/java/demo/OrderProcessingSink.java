package demo;


import com.fasterxml.jackson.databind.ObjectMapper;
import demo.domain.CreditCardInfo;
import demo.domain.Payment;
import demo.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import java.io.IOException;
import java.util.Random;

@EnableBinding(Sink.class)
@Slf4j
@MessageEndpoint
public class OrderProcessingSink {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private PaymentService paymentService;

    @ServiceActivator(inputChannel = Sink.INPUT)
    public void paymentProcessor(String orderInfo) throws IOException {

       // convert JSON string to object
       Payment payment = mapper.readValue(orderInfo, Payment.class);
       if (!isPaymentValid(payment)) {
           log.error("[Payment Failed!] Invalid Card! Use another card please.");
           return;
       }

       Payment savedPayment = paymentService.save(payment);

       Random rand = new Random();
       int waitTime = 5 + rand.nextInt(56);  // generate an integer [5, 60]
       log.info(String.format("[Payment Successful!] " +
               "Payment ID is %d. " +
               "Your credit card is charged %.2f dollars at %s. " +
               "Order ID is %s. " +
               "Your order will be delivered in %d minutes",
               savedPayment.getPaymentId(),
               payment.getTotalPrice(),
               payment.getTimestamp(),
               payment.getOrderId(),
               waitTime)
       );
    }

    private boolean isPaymentValid(Payment payment) {
        CreditCardInfo cardInfo = payment.getCreditCardInfo();
        String cardNumber = cardInfo.getCardNumber();

        /* card number format: aaaa-bbbb-cccc-dddd
         * Rule: if aaaa <= 1000, it is a invalid card.
         */
        int first4Digits = Integer.valueOf(cardNumber.split("-")[0]);

        return first4Digits > 1000;
    }
}
