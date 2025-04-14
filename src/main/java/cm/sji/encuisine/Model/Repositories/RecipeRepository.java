package cm.sji.encuisine.Model.Repositories;

import cm.sji.encuisine.Model.Entities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
