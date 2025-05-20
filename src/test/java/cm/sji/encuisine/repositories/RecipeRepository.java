package cm.sji.encuisine.repositories;

import cm.sji.encuisine.dto.RecipeFilterDTO;
import cm.sji.encuisine.entities.Recipe;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {


    List<Recipe> findByCuisineIgnoreCase(String cuisine);


    @Query(value = "SELECT DISTINCT r.* FROM recipe r " +
            "JOIN recipe_ingredient ri ON r.id = ri.recipe_id " +
            "JOIN ingredient i ON ri.ingredient_id = i.id " +
            "WHERE i.name IN :ingredients " +
            "GROUP BY r.id " +
            "HAVING COUNT(DISTINCT i.name) >= :count",
            nativeQuery = true)
    List<Recipe> findByIngredientsContainingAll(
            @Param("ingredients") List<String> ingredients,
            @Param("count") Long count);


    List<Recipe> findByPrepTypeIgnoreCase(String prepType);


    default List<Recipe> findByFilter(RecipeFilterDTO filterDTO) {
        throw new UnsupportedOperationException("This method should be implemented in the service layer");
    }


    @Query(value = "SELECT * FROM recipe WHERE cuisine = :cuisine ORDER BY rating DESC LIMIT :limit",
            nativeQuery = true)
    List<Recipe> findTopRatedByCuisine(@Param("cuisine") String cuisine, @Param("limit") int limit);


    List<Recipe> findByNameContainingIgnoreCase(String searchTerm);

    List<Recipe> findByCuisineIgnoreCaseOrderByRatingDesc(String cuisine);
}