package cm.sji.encuisine.dto;

import java.util.List;

public class RecipeFilterDTO {
    private String cuisine;
    private List<String> ingredients;
    private String prepType;

    public RecipeFilterDTO() {
    }

    public RecipeFilterDTO(String cuisine, List<String> ingredients, String prepType) {
        this.cuisine = cuisine;
        this.ingredients = ingredients;
        this.prepType = prepType;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getPrepType() {
        return prepType;
    }

    public void setPrepType(String prepType) {
        this.prepType = prepType;
    }
}
