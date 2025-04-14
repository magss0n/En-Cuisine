package cm.sji.encuisine.Model.Repositories;

import cm.sji.encuisine.Model.Entities.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}
