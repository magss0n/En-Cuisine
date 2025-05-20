package cm.sji.encuisine.dto;

import java.util.List;


public class RecipeFilterDTO {

    private String cuisine;
    private String prepType;
    private List<String> ingredients;


    public RecipeFilterDTO() {
    }


    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getPrepType() {
        return prepType;
    }

    public void setPrepType(String prepType) {
        this.prepType = prepType;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "RecipeFilterDTO{" +
                "cuisine='" + cuisine + '\'' +
                ", prepType='" + prepType + '\'' +
                ", ingredients=" + ingredients +
                '}';
    }
}