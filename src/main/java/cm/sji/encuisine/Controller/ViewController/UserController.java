package cm.sji.encuisine.Controller.ViewController;

import cm.sji.encuisine.Model.Entities.User;
import cm.sji.encuisine.Model.Services.PersonService;
import cm.sji.encuisine.config.CustomUserDetails;
import cm.sji.encuisine.config.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private final PersonService personService;
    AuthenticationManager authenticationManager;

    @Autowired
    public UserController(PersonService personService, AuthenticationManager authenticationManager) {
        this.personService = personService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/main")
    public String home(Model model, Authentication authentication) {
        System.out.println("Hello World");
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = (User) personService.findByName(userDetails.getUsername());
        model.addAttribute("user", user);
        return "main";
    }

    @GetMapping("/custom")
    public String custom(Model model, Authentication authentication) {
        return "custom";
    }
    @GetMapping("/recipeDetails")
    public String recipeDetails(Model model, Authentication authentication) {
        return "recipeDetails";
    }

}
