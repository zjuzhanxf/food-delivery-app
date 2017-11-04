package demo.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class CreditCardInfo {
    String cardNumber;
    String expiration;
    String securityCode;
}

