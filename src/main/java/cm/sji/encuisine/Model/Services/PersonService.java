package cm.sji.encuisine.Model.Services;

import cm.sji.encuisine.Model.Entities.Person;
import cm.sji.encuisine.Model.Repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Person findById(Long id) {
        return personRepository.findById(id).orElse(null);
    }

    public void savePerson(Person person) {
        personRepository.save(person);
    }

    public Person findByName(String personName) {return personRepository.findByName(personName);}

    public void deletePerson(Long personId) {
        personRepository.deleteById(personId);
    }

    public Boolean emailExists(String email) {
        return personRepository.existsByEmail(email);
    }
}
