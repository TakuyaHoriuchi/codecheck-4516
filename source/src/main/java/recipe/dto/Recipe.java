package recipe.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Recipe {
    private Integer id;
    private String title;
    private String making_time;
    private String serves;
    private String ingredients;
    private String cost;
}
