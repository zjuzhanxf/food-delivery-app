package demo.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Document
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@RequiredArgsConstructor(onConstructor = @__(@PersistenceConstructor))
public class Restaurant {

    @Id
    String id;

    @JsonProperty("restaurantName")
    String name;

    @JsonProperty("restaurantCategory")
    String category;

    @JsonProperty("rating")
    Double rating;

    @JsonProperty("menuItemList")
    List<MenuItem> menuItemList;


    @JsonCreator
    public Restaurant(@JsonProperty("restaurantName") String name, @JsonProperty("restaurantCategory") String category) {
        this.name = name;
        this.category = category;
    }
}
