package demo.domain;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@RequiredArgsConstructor(onConstructor = @__(@PersistenceConstructor))
@Document
public class MenuItem {

    private String name;
    private Double price;

    @JsonCreator
    public MenuItem(@JsonProperty("dishName") String name, @JsonProperty("dishPrice") Double price) {
        this.name = name;
        this.price = price;
    }
}
