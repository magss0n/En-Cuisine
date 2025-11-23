package cm.sji.encuisine.Model.Repositories;

import cm.sji.encuisine.Model.Entities.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    List<Ingredient> findAllByRecipe_Id(Long recipeId);


    Ingredient findByName(String name);

    @Query("select distinct name from Ingredient ")
    List<String> ingredientsUnique();
}
