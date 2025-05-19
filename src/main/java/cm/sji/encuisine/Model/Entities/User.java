package cm.sji.encuisine.Model.Entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("USER")
@AllArgsConstructor
@Getter
@Setter
public class User extends Person {
}
