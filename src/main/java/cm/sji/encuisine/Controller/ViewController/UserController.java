package cm.sji.encuisine.Controller.ViewController;

import cm.sji.encuisine.Model.Entities.Ingredient;
import cm.sji.encuisine.Model.Entities.Recipe;
import cm.sji.encuisine.Model.Entities.User;
import cm.sji.encuisine.Model.Repositories.IngredientRepository;
import cm.sji.encuisine.Model.Services.IngredientService;
import cm.sji.encuisine.Model.Services.PersonService;
import cm.sji.encuisine.Model.Services.RecipeImporter;
import cm.sji.encuisine.Model.Services.RecipeService;
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

import java.util.List;
import java.util.Objects;

@Controller
public class UserController {

    private final PersonService personService;
    private final RecipeImporter recipeImporter;
    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final IngredientRepository ingredientRepository;
    AuthenticationManager authenticationManager;

    @Autowired
    public UserController(PersonService personService, AuthenticationManager authenticationManager, RecipeImporter recipeImporter, RecipeService recipeService, IngredientService ingredientService, IngredientRepository ingredientRepository) {
        this.personService = personService;
        this.authenticationManager = authenticationManager;
        this.recipeImporter = recipeImporter;
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.ingredientRepository = ingredientRepository;
    }

    @GetMapping("/main")
    public String home(Model model, Authentication authentication) {
        System.out.println("Hello World");
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = (User) personService.findByName(userDetails.getUsername());
        model.addAttribute("user", user);

        model.addAttribute("recipes", recipeService.getAllRecipes());
        return "main";
    }

    @PostMapping("/search")
    public String search(Model model,@RequestParam("recipe") String recipeName, Authentication authentication) {
        model.addAttribute("recipes", recipeService.getByNameContaining(recipeName));
        return "main";
    }

    @GetMapping("/custom")
    public String custom(Model model, Authentication authentication) {
        model.addAttribute("ingredients", ingredientService.getUniqueIngredients());
        model.addAttribute("categories", recipeService.findAllCategories());
        model.addAttribute("recipes", List.of());
        model.addAttribute("cuisineType", recipeService.findAllAreas());
//        model.addAttribute("recommendations", recipeService.findSomeRecipes() );
        return "custom";
    }
    @GetMapping("/recipeDetails/{recipeId}")
    public String recipeDetails(Model model, Authentication authentication, @PathVariable Long recipeId) {
        Recipe recipe = recipeService.getRecipe(recipeId);
        model.addAttribute("recipe", recipe);
        model.addAttribute("instructions", recipe.getInstructions().split("\\."));
        return "recipeDetails";
    }

    @PostMapping("/import")
    public String importRecipe(Model model, @RequestParam("letter") String letter) {
        recipeImporter.importRecipes(letter);
        return "redirect:/main";
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("recipes", recipeService.findSomeRecipes());
        return "index";
    }

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("recipes", recipeService.findSomeRecipes());
        return "index";
    }

    @PostMapping("/customSearch")
    public String customSearch(@RequestParam("ingredients") String search, //Selected Ingredients
                               @RequestParam("cuisineType") String cuisineType, //Selected cuisineType
                               @RequestParam("category") String category, //Selected type of meal e.g. breakfast, vegan etc
                               Model model) {
        var selectedIng = search.split(","); //Split obtained string of selected ingredients into a list of ingredients
        List<Recipe> recipes = recipeService.getRecipeByIngredient(selectedIng[0]); //All recipes containing the first selected ingredient
        //For every other selected ingredient, find its recipes and keep those in common with recipes of the first ingredient
        for (int i=1; i<selectedIng.length; i++) {
            recipes.retainAll(recipeService.getRecipeByIngredient(selectedIng[i]));
        }
        model.addAttribute("ingredients", ingredientService.getUniqueIngredients());
        model.addAttribute("categories", recipeService.findAllCategories());
        model.addAttribute("cuisineType", recipeService.findAllAreas());

        //In case the user selected a category, keep the recipes corresponding to that category
        if (!Objects.equals(category, "all")){
            recipes = recipes.stream().filter(recipe -> recipe.getCategory().equals(category)).toList();
        }

        //In case the user selected a cuisineType, keep the recipes corresponding to that cuisineType
        if (!Objects.equals(cuisineType, "all")){
            recipes = recipes.stream().filter(recipe -> recipe.getArea().equals(cuisineType)).toList();
        }
        model.addAttribute("recipes", recipes);
        return "custom";
    }


}
