package cm.sji.encuisine.services;

import cm.sji.encuisine.dto.RecipeDTO;
import cm.sji.encuisine.dto.RecipeFilterDTO;
import cm.sji.encuisine.servicesimplementation.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RecipeService {

    List<RecipeDTO> getAllRecipes();


    Page<RecipeDTO> getRecipesPaginated(Pageable pageable);

    @Transactional(readOnly = true)
    <ResourceNotFoundException extends Throwable> RecipeDTO getRecipeById(Long id) throws ResourceNotFoundException;


    RecipeDTO createRecipe(RecipeDTO recipeDTO);


    RecipeDTO updateRecipe(Long id, RecipeDTO recipeDTO) throws ResourceNotFoundException;


    void deleteRecipe(Long id) throws ResourceNotFoundException;


    List<RecipeDTO> searchRecipes(RecipeFilterDTO filterDTO);


    List<RecipeDTO> getTopRatedRecipesByCuisine(String cuisine, int limit);


    List<RecipeDTO> searchRecipesByName(String searchTerm);
}
