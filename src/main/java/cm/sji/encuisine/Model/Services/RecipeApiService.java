package cm.sji.encuisine.Model.Services;

import cm.sji.encuisine.Model.Entities.Ingredient;
import cm.sji.encuisine.Model.Entities.Meal;
import cm.sji.encuisine.Model.Entities.MealDBResponse;
import cm.sji.encuisine.Model.Entities.Recipe;
import cm.sji.encuisine.Model.Repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecipeApiService {
    private final RecipeService recipeService;
    private final RecipeRepository recipeRepository;
    @Value("${recipe.api.url}")
    private String apiUrl;

    @Value("${recipe.api.key}")
    private String apiKey;

    public RecipeApiService(RecipeService recipeService, RecipeRepository recipeRepository) {
        this.recipeService = recipeService;
        this.recipeRepository = recipeRepository;
    }

    public List<Recipe> fetchRecipes(String searchTerm) {
        RestTemplate restTemplate = new RestTemplate();
        String url = apiUrl + "/search.php?f=" + searchTerm;

        // Map response to DTO
        MealDBResponse response = restTemplate.getForObject(url, MealDBResponse.class);

        assert response != null;
        return response.getMeals().stream()
                .map(this::convertToEntity)
                .toList();
    }

    private Recipe convertToEntity(Meal meal) {
        Recipe recipe = recipeService.getRecipeByName(meal.getStrMeal());
//        recipe.setId(Long.parseLong(meal.getIdMeal()));
        recipe.setName(meal.getStrMeal());
        recipe.setCategory(meal.getStrCategory());
        recipe.setArea(meal.getStrArea());
        recipe.setInstructions(meal.getStrInstructions());
        recipe.setImageUrl(meal.getStrMealThumb());
        recipe.setYoutubeUrl(meal.getStrYoutube());
        Method getIngredient;
        Method getMeasure;

        // Parse ingredients (handles strIngredient1-20)
        List<Ingredient> ingredients = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            String ingredient;
            String measure;
            try {
                getIngredient = Meal.class.getMethod("getStrIngredient" + i);
                getMeasure = Meal.class.getMethod("getStrMeasure" + i);
                ingredient = (String) getIngredient.invoke(meal);
                measure = (String) getMeasure.invoke(meal);
            } catch (NoSuchMethodException  | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }

            if (ingredient != null && !ingredient.isEmpty()) {
                Ingredient ing = new Ingredient();
                ing.setName(ingredient);
                ing.setMeasure(measure);
                ing.setRecipe(recipe);
                ingredients.add(ing);
            }
        }
        recipe.setIngredients(ingredients);
        recipeRepository.save(recipe);
        System.out.println("Imported " + recipe.getName());
        return recipe;
    }
}
