package cm.sji.encuisine.Controller.ViewController;

import cm.sji.encuisine.Model.Entities.User;
import cm.sji.encuisine.Model.Services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final PersonService personService;

    @Autowired
    public LoginController(AuthenticationManager authenticationManager, PersonService personService) {
        this.authenticationManager = authenticationManager;
        this.personService = personService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signUp(Model model) {
        model.addAttribute("newUser", new User());
//        System.out.println(new User());
        return "signup";
    }

    @PostMapping("/signup/save")
    public String createUser(@Validated @ModelAttribute User user, BindingResult result, Model model) {
        Boolean emailExists = personService.emailExists(user.getEmail());

        if (personService.findByName(user.getName()) != null && emailExists) {
           result.rejectValue("email", null, "This account already exists");
           model.addAttribute("message", "This account already exists");
        }
        if (result.hasErrors()) {
            model.addAttribute("newUser", user);
            return "signup";
        }

        personService.savePerson(user);
        System.out.println("User created");
        authenticateUser(user);
        return "redirect:/user/home";
    }

    private void authenticateUser(User user) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                user.getName(),
                user.getPassword()
        );
        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println("Authenticated");
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }
//    @GetMapping("/home")
//    public String homePage(Model model, Authentication authentication) {
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        User user = (User) personService.findByName(userDetails.getUsername());
//        model.addAttribute("user", user);
//
//        return "home";
//    }
}
