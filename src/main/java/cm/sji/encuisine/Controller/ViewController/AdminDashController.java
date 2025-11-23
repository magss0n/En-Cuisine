package cm.sji.encuisine.Controller.ViewController;

import jakarta.persistence.Column;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminDashController {

    @GetMapping("/admin")
    public String admin() {
        return "admin/adminDash";
    }
}
