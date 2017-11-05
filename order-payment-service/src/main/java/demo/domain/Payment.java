package demo.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue
    Long paymentId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "cardNumber", column = @Column(name = "credit_card_number")),
            @AttributeOverride(name = "expiration", column = @Column(name = "credit_card_expiration")),
            @AttributeOverride(name = "securityCode", column = @Column(name = "credit_card_security_code"))
    })
    CreditCardInfo creditCardInfo;

    Double totalPrice;

    @JsonProperty(value = "id")
    String orderId;

    Date timestamp = new Date();

    @JsonCreator
    public Payment(@JsonProperty("creditCardInfo") CreditCardInfo cardInfo, @JsonProperty("totalPrice") Double price) {
        this.creditCardInfo = cardInfo;
        this.totalPrice = price;
    }
}
