package demo.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Embeddable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Embeddable
public class CreditCardInfo {
    String cardNumber;
    String expiration;
    String securityCode;

    public CreditCardInfo(){
    }

    @JsonCreator
    public CreditCardInfo(@JsonProperty("cardNumber") String cardNumber, @JsonProperty("expiration") String expiration, @JsonProperty("securityCode") String securityCode) {
        this.cardNumber = cardNumber;
        this.expiration = expiration;
        this.securityCode = securityCode;
    }

}

