package cm.sji.encuisine.Model.Entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "registered_people")
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String email;
    private String password;
    private String phone;


}
