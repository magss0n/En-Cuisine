package cm.sji.encuisine.repositories;

import cm.sji.encuisine.entities.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {


    Optional<Ingredient> findByNameIgnoreCase(String name);


    boolean existsByNameIgnoreCase(String name);
}


