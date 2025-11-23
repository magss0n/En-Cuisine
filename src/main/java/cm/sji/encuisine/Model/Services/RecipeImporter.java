package cm.sji.encuisine.Model.Services;

import cm.sji.encuisine.Model.Entities.Recipe;
import cm.sji.encuisine.Model.Repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeImporter {

    private final RecipeApiService recipeApiService;
    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeImporter(RecipeApiService recipeApiService, RecipeRepository recipeRepository) {
        this.recipeApiService = recipeApiService;
        this.recipeRepository = recipeRepository;
    }

    @Scheduled(fixedRate = 86_400_000)
    public void importRecipes(String letter) {
        recipeApiService.fetchRecipes(letter);

    }
}
