package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

  private Integer id;

  @JsonProperty("first_name")
  private String firstName;

  @JsonProperty("last_name")
  private String lastName;

  private String email;

  private String avatar;

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return Objects.equals(id, user.id)
        && Objects.equals(firstName, user.firstName)
        && Objects.equals(lastName, user.lastName)
        && Objects.equals(email, user.email)
        && Objects.equals(avatar, user.avatar);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, firstName, lastName, email, avatar);
  }
}
