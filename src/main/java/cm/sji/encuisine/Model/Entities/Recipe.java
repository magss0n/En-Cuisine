package cm.sji.encuisine.Model.Entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="recipes")
@Data
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "instructions")
    private String instructions;

    @Column(name = "cuisineType")
    private String typeOfCuisine;

    @Column(name = "duration")
    private Double duration;

    @Column(name = "difficulty")
    private String difficulty;
}
