package cm.sji.encuisine.Model.Entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="recipes")
@Data
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String instructions;
    private String imageURL;
    private String typeOfCuisine;
    private Double duration;
    private String difficulty;
}
