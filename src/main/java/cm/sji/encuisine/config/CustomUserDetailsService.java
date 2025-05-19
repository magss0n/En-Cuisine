package cm.sji.encuisine.config;

import cm.sji.encuisine.Model.Entities.Person;
import cm.sji.encuisine.Model.Repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final PersonRepository personRepository;

    @Autowired
    public CustomUserDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = personRepository.findByName(username);

        if (person == null) {
            System.out.println("Username" + username + "not found");
            throw new UsernameNotFoundException(username + " not found");
        }
        System.out.println("Person: " + person);
        return new CustomUserDetails(person);
    }
}
