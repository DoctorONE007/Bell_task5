package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ColorInfo {

    Integer id;
    String name;
    Integer year;
    String color;
    @JsonProperty("pantone_value")
    String pantoneValue;

}
