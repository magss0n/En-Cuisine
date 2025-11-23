package cm.sji.encuisine.Model.Services;

import cm.sji.encuisine.Model.Entities.Ingredient;
import cm.sji.encuisine.Model.Entities.Recipe;
import cm.sji.encuisine.Model.Repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeService {
    private RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Recipe addRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public Recipe getRecipe(Long id) {
        return recipeRepository.findById(id).orElse(null);
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public Recipe getRecipeByName(String name) {
        return recipeRepository.findByName(name);
    }

    public List<Recipe> getRecipesByCategory(String category) {
        return recipeRepository.findAllByCategory(category);
    }

    public List<Recipe> findSomeRecipes(){
        return recipeRepository.findSomeRecipes();
    }

    public List<String> findAllCategories(){
        return recipeRepository.findAllCategories();
    }

    public List<String> findAllAreas(){
        return recipeRepository.findAllAreas();
    }

    public List<Recipe> getRecipeByIngredient(String ingredient) {
        return recipeRepository.findAllByIngredient(ingredient);
    }

    public List<Recipe> getByNameContaining(String name) {
        return recipeRepository.findByNameContaining(name);
    }
}
