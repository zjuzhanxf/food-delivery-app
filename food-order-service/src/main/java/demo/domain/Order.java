package demo.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Document
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Order {

    @Id
    String id;

    String fromRestaurant;
    List<MenuItem> orderItemList;
    Date orderTime;
    Double totalPrice;
    CreditCardInfo creditCardInfo;
    String note;
    String deliverAddress;

    public Order() {
        fromRestaurant = null;
        orderItemList = new ArrayList<>();
        orderTime = null;
        totalPrice = 0.0;
        creditCardInfo = null;
        note = null;
        deliverAddress = null;
    }

    public void addItemToOrder(MenuItem item) {
        orderItemList.add(item);
    }

    public Double calculateTotal() {
        Double sum = 0.0;
        for (MenuItem item : orderItemList) {
            sum += item.getPrice();
        }
        return sum;
    }

}

