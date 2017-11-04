package demo.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CreditCardInfo {
    String cardNumber;
    String expiration;
    String securityCode;

    @JsonCreator
    public CreditCardInfo(@JsonProperty("cardNumber") String cardNumber, @JsonProperty("expiration") String expiration, @JsonProperty("securityCode") String securityCode) {
        this.cardNumber = cardNumber;
        this.expiration = expiration;
        this.securityCode = securityCode;
    }

}

