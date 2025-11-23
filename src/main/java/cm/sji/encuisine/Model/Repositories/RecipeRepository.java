package cm.sji.encuisine.Model.Repositories;

import cm.sji.encuisine.Model.Entities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Recipe findByName(String name);

    List<Recipe> findAllByCategory(String category);

    @Query("select re from Recipe re order by re.name limit 6")
    List<Recipe> findSomeRecipes();

    @Query("select distinct area from Recipe ")
    List<String> findAllAreas();

    @Query("select distinct category from Recipe ")
    List<String> findAllCategories();

    @Query("select r from Recipe r where :ing in (select i.name from Ingredient i where i.recipe.id = r.id)")
    List<Recipe> findAllByIngredient(@Param("ing") String ingredient);

    @Query("select r from Recipe r where r.name like %:recipe%")
    List<Recipe> findByNameContaining(@Param("recipe") String name);


}
