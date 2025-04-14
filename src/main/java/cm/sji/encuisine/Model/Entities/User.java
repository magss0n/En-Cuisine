package cm.sji.encuisine.Model.Entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("USER")
@Getter
@Setter
public class User extends Person {
}
