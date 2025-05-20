package cm.sji.encuisine.servicesimplementation;

import cm.sji.encuisine.dto.RecipeDTO;
import cm.sji.encuisine.dto.RecipeFilterDTO;
import cm.sji.encuisine.entities.Recipe;
import cm.sji.encuisine.entities.Ingredient;
import cm.sji.encuisine.exceptions.ResourceNotFoundException;
import cm.sji.encuisine.exceptions.InvalidRequestException;
import cm.sji.encuisine.exceptions.ServiceException;
import cm.sji.encuisine.repositories.RecipeRepository;
import cm.sji.encuisine.repositories.IngredientRepository;
import cm.sji.encuisine.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class RecipeServiveImpl implements RecipeService {

    private static final Logger logger = LoggerFactory.getLogger(RecipeServiveImpl.class);

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    @Autowired
    public RecipeServiveImpl(RecipeRepository recipeRepository, IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecipeDTO> getAllRecipes() {
        try {
            List<Recipe> recipes = recipeRepository.findAll();
            return recipes.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error retrieving all recipes", e);
            throw new ServiceException("Failed to retrieve recipes", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RecipeDTO> getRecipesPaginated(Pageable pageable) {
        try {
            if (pageable == null) {
                throw new InvalidRequestException("Pageable cannot be null");
            }
            return recipeRepository.findAll(pageable)
                    .map(this::convertToDTO);
        } catch (InvalidRequestException e) {
            logger.error("Invalid pageable parameter: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error retrieving paginated recipes", e);
            throw new ServiceException("Failed to retrieve paginated recipes", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public RecipeDTO getRecipeById(Long id) {
        validateId(id, "Recipe id cannot be null");

        try {
            Recipe recipe = recipeRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id: " + id));
            return convertToDTO(recipe);
        } catch (ResourceNotFoundException e) {
            logger.warn("Recipe not found with id: {}", id);
            throw e;
        } catch (Exception e) {
            logger.error("Error retrieving recipe with id: {}", id, e);
            throw new ServiceException("Failed to retrieve recipe with id: " + id, e);
        }
    }

    @Override
    @Transactional
    public RecipeDTO createRecipe(RecipeDTO recipeDTO) {
        try {
            if (recipeDTO == null) {
                throw new InvalidRequestException("Recipe data cannot be null");
            }

            if (isNullOrEmpty(recipeDTO.getName())) {
                throw new InvalidRequestException("Recipe name cannot be empty");
            }

            Recipe recipe = convertToEntity(recipeDTO);
            Recipe savedRecipe = recipeRepository.save(recipe);
            logger.info("Created new recipe with id: {}", savedRecipe.getId());
            return convertToDTO(savedRecipe);
        } catch (InvalidRequestException e) {
            logger.error("Invalid recipe data: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error creating recipe: {}", recipeDTO != null ? recipeDTO.getName() : "null", e);
            throw new ServiceException("Failed to create recipe", e);
        }
    }

    @Override
    @Transactional
    public RecipeDTO updateRecipe(Long id, RecipeDTO recipeDTO) {
        validateId(id, "Recipe id cannot be null");

        if (recipeDTO == null) {
            throw new InvalidRequestException("Recipe data cannot be null");
        }

        try {
            Recipe existingRecipe = recipeRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id: " + id));

            // Update basic fields
            updateRecipeFields(existingRecipe, recipeDTO);

            // Save updated recipe
            Recipe updatedRecipe = recipeRepository.save(existingRecipe);
            logger.info("Updated recipe with id: {}", id);
            return convertToDTO(updatedRecipe);
        } catch (ResourceNotFoundException e) {
            logger.warn("Recipe not found for update with id: {}", id);
            throw e;
        } catch (InvalidRequestException e) {
            logger.error("Invalid data for recipe update: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error updating recipe with id: {}", id, e);
            throw new ServiceException("Failed to update recipe", e);
        }
    }

    @Override
    @Transactional
    public void deleteRecipe(Long id) {
        validateId(id, "Recipe id cannot be null");

        try {
            if (!recipeRepository.existsById(id)) {
                throw new ResourceNotFoundException("Recipe not found with id: " + id);
            }
            recipeRepository.deleteById(id);
            logger.info("Deleted recipe with id: {}", id);
        } catch (ResourceNotFoundException e) {
            logger.warn("Recipe not found for deletion with id: {}", id);
            throw e;
        } catch (Exception e) {
            logger.error("Error deleting recipe with id: {}", id, e);
            throw new ServiceException("Failed to delete recipe", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecipeDTO> searchRecipes(RecipeFilterDTO filterDTO) {
        try {
            // If no filters provided, return all recipes
            if (isEmptyFilter(filterDTO)) {
                return getAllRecipes();
            }

            List<Recipe> results = recipeRepository.findAll();

            // Apply cuisine filter if provided
            if (filterDTO.getCuisine() != null && !filterDTO.getCuisine().isEmpty()) {
                results = results.stream()
                        .filter(recipe -> recipe.getCuisine() != null &&
                                recipe.getCuisine().equalsIgnoreCase(filterDTO.getCuisine()))
                        .collect(Collectors.toList());

                // If no results, return empty list early
                if (results.isEmpty()) {
                    return new ArrayList<>();
                }
            }

            // Apply prep type filter if provided
            if (filterDTO.getPrepType() != null && !filterDTO.getPrepType().isEmpty()) {
                results = results.stream()
                        .filter(recipe -> recipe.getPrepType() != null &&
                                recipe.getPrepType().equalsIgnoreCase(filterDTO.getPrepType()))
                        .collect(Collectors.toList());

                // If no results after this filter, return empty list early
                if (results.isEmpty()) {
                    return new ArrayList<>();
                }
            }

            // Apply ingredients filter if provided
            if (filterDTO.getIngredients() != null && !filterDTO.getIngredients().isEmpty()) {
                List<String> ingredients = filterDTO.getIngredients();

                results = results.stream()
                        .filter(recipe -> {
                            // Check if recipe has ingredients
                            if (recipe.getIngredients() == null || recipe.getIngredients().isEmpty()) {
                                return false;
                            }

                            // Get all ingredient names from this recipe
                            Set<String> recipeIngredientNames = recipe.getIngredients().stream()
                                    .filter(ingredient -> ingredient != null && ingredient.getName() != null)
                                    .map(ingredient -> ingredient.getName().toLowerCase())
                                    .collect(Collectors.toSet());

                            // Check if all requested ingredients are in this recipe
                            return ingredients.stream()
                                    .filter(name -> name != null && !name.trim().isEmpty())
                                    .map(String::toLowerCase)
                                    .allMatch(recipeIngredientNames::contains);
                        })
                        .collect(Collectors.toList());
            }

            // Convert results to DTOs
            return results.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error searching recipes with filter: {}", filterDTO, e);
            throw new ServiceException("Failed to search recipes", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecipeDTO> getTopRatedRecipesByCuisine(String cuisine, int limit) {
        if (cuisine == null || cuisine.trim().isEmpty()) {
            throw new InvalidRequestException("Cuisine cannot be null or empty");
        }

        if (limit <= 0) {
            throw new InvalidRequestException("Limit must be greater than zero");
        }

        try {
            List<Recipe> recipes = recipeRepository.findByCuisineIgnoreCaseOrderByRatingDesc(cuisine);

            // Apply limit to results
            return recipes.stream()
                    .limit(limit)
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (InvalidRequestException e) {
            logger.error("Invalid parameters for getTopRatedRecipesByCuisine: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error retrieving top rated recipes for cuisine: {}", cuisine, e);
            throw new ServiceException("Failed to retrieve top rated recipes for cuisine: " + cuisine, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecipeDTO> searchRecipesByName(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return new ArrayList<>();
        }

        try {
            List<Recipe> recipes = recipeRepository.findByNameContainingIgnoreCase(searchTerm.trim());
            return recipes.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error searching recipes by name: {}", searchTerm, e);
            throw new ServiceException("Failed to search recipes by name", e);
        }
    }

    /**
     * Validates that an ID is not null, throwing an InvalidRequestException if it is
     */
    private void validateId(Long id, String message) {
        if (id == null) {
            throw new InvalidRequestException(message);
        }
    }

    /**
     * Check if filter is empty (no criteria specified)
     */
    private boolean isEmptyFilter(RecipeFilterDTO filter) {
        return filter == null ||
                (isNullOrEmpty(filter.getCuisine()) &&
                        isNullOrEmpty(filter.getPrepType()) &&
                        (filter.getIngredients() == null || filter.getIngredients().isEmpty()));
    }

    /**
     * Check if string is null or empty
     */
    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    private void updateRecipeFields(Recipe recipe, RecipeDTO dto) {
        // Update basic fields
        if (dto.getName() != null) {
            recipe.setName(dto.getName());
        }
        if (dto.getCuisine() != null) {
            recipe.setCuisine(dto.getCuisine());
        }
        if (dto.getPrepTime() != null) {
            recipe.setPrepTime(dto.getPrepTime());
        }
        if (dto.getCookTime() != null) {
            recipe.setCookTime(dto.getCookTime());
        }
        if (dto.getServings() != null) {
            recipe.setServings(dto.getServings());
        }
        if (dto.getInstructions() != null) {
            recipe.setInstructions(dto.getInstructions());
        }
        if (dto.getPrepType() != null) {
            recipe.setPrepType(dto.getPrepType());
        }
        if (dto.getRating() != null) {
            recipe.setRating(dto.getRating());
        }


        if (dto.getIngredients() != null) {
            updateIngredients(recipe, dto.getIngredients());
        }
    }


    private void updateIngredients(Recipe recipe, List<String> ingredientNames) {
        if (recipe == null) {
            throw new InvalidRequestException("Recipe cannot be null");
        }

        if (ingredientNames == null) {
            ingredientNames = new ArrayList<>();
        }

        try {

            if (recipe.getIngredients() == null) {
                recipe.setIngredients(new HashSet<>());
            }

            // Create a new set for the ingredients
            Set<Ingredient> updatedIngredients = new HashSet<>();

            for (String name : ingredientNames) {
                if (name == null || name.trim().isEmpty()) {
                    continue; // Skip empty ingredient names
                }

                String trimmedName = name.trim();

                // Try to find existing ingredient
                Ingredient ingredient = ingredientRepository.findByNameIgnoreCase(trimmedName)
                        .orElseGet(() -> {
                            // Create new if not exists
                            Ingredient newIngredient = new Ingredient();
                            newIngredient.setName(trimmedName);
                            return ingredientRepository.save(newIngredient);
                        });

                updatedIngredients.add(ingredient);
            }

            recipe.getIngredients().clear();
            recipe.getIngredients().addAll(updatedIngredients);
        } catch (Exception e) {
            logger.error("Error updating ingredients for recipe: {}", recipe.getName(), e);
            throw new ServiceException("Failed to update recipe ingredients", e);
        }
    }

    private RecipeDTO convertToDTO(Recipe recipe) {
        if (recipe == null) {
            throw new InvalidRequestException("Cannot convert null recipe to DTO");
        }

        RecipeDTO dto = new RecipeDTO();
        dto.setId(recipe.getId());
        dto.setName(recipe.getName());
        dto.setCuisine(recipe.getCuisine());
        dto.setPrepTime(recipe.getPrepTime());
        dto.setCookTime(recipe.getCookTime());
        dto.setServings(recipe.getServings());
        dto.setInstructions(recipe.getInstructions());
        dto.setPrepType(recipe.getPrepType());
        dto.setRating(recipe.getRating());

        // Convert ingredients to strings
        List<String> ingredientList = new ArrayList<>();
        if (recipe.getIngredients() != null) {
            ingredientList = recipe.getIngredients().stream()
                    .filter(ingredient -> ingredient != null && ingredient.getName() != null)
                    .map(Ingredient::getName)
                    .collect(Collectors.toList());
        }
        dto.setIngredients(ingredientList);

        return dto;
    }



    private Recipe convertToEntity(RecipeDTO dto) {
        if (dto == null) {
            throw new InvalidRequestException("Cannot convert null DTO to recipe");
        }

        Recipe recipe = new Recipe();

        if (dto.getId() != null) {
            recipe.setId(dto.getId());
        }

        recipe.setName(dto.getName());
        recipe.setCuisine(dto.getCuisine());
        recipe.setPrepTime(dto.getPrepTime());
        recipe.setCookTime(dto.getCookTime());
        recipe.setServings(dto.getServings());
        recipe.setInstructions(dto.getInstructions());
        recipe.setPrepType(dto.getPrepType());
        recipe.setRating(dto.getRating());

        recipe.setIngredients(new HashSet<>());

        // Process ingredients if provided
        if (dto.getIngredients() != null && !dto.getIngredients().isEmpty()) {
            updateIngredients(recipe, dto.getIngredients());
        }

        return recipe;
    }
}