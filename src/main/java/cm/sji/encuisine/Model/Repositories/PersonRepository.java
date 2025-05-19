package cm.sji.encuisine.Model.Repositories;

import cm.sji.encuisine.Model.Entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Person findByName(String name);

    boolean existsByEmail(String email);
}
