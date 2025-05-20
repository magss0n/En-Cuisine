package cm.sji.encuisine.dto;

import java.util.List;
import java.util.Objects;


public class RecipeDTO {

    private Long id;
    private String name;
    private String cuisine;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private String instructions;
    private String prepType;
    private Double rating;
    private List<String> ingredients;

    public RecipeDTO() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public Integer getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(Integer prepTime) {
        this.prepTime = prepTime;
    }

    public Integer getCookTime() {
        return cookTime;
    }

    public void setCookTime(Integer cookTime) {
        this.cookTime = cookTime;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getPrepType() {
        return prepType;
    }

    public void setPrepType(String prepType) {
        this.prepType = prepType;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeDTO recipeDTO = (RecipeDTO) o;
        return Objects.equals(id, recipeDTO.id) &&
                Objects.equals(name, recipeDTO.name) &&
                Objects.equals(cuisine, recipeDTO.cuisine) &&
                Objects.equals(prepTime, recipeDTO.prepTime) &&
                Objects.equals(cookTime, recipeDTO.cookTime) &&
                Objects.equals(servings, recipeDTO.servings) &&
                Objects.equals(instructions, recipeDTO.instructions) &&
                Objects.equals(prepType, recipeDTO.prepType) &&
                Objects.equals(rating, recipeDTO.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, cuisine, prepTime, cookTime, servings, instructions, prepType, rating);
    }

    @Override
    public String toString() {
        return "RecipeDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cuisine='" + cuisine + '\'' +
                ", prepTime=" + prepTime +
                ", cookTime=" + cookTime +
                ", servings=" + servings +
                ", instructions='" + instructions + '\'' +
                ", prepType='" + prepType + '\'' +
                ", rating=" + rating +
                ", ingredients=" + ingredients +
                '}';
    }
}
