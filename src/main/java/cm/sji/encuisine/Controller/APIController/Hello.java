package cm.sji.encuisine.Controller.APIController;

import cm.sji.encuisine.Model.Entities.Meal;
import cm.sji.encuisine.Model.Entities.MealDBResponse;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Hello {

    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }

    @PostMapping(value="/meals", consumes = "application/json")
    public ResponseEntity<String> meals(@RequestBody MealDBResponse mealDBResponse) {
        System.out.println(mealDBResponse);
        return ResponseEntity.ok("ok!");
    }
}
