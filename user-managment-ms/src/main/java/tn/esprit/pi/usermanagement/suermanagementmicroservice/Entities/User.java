package tn.esprit.pi.usermanagement.suermanagementmicroservice.Entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.persistence.PrePersist;
import java.util.List;
import java.util.UUID;

@Document(collection = "users")
@Getter
@Setter
public class User {
    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;
    private String name;
    private String email;
    private String password;
    private Roles role;
    private String phone;
    private Boolean is2FAEnabled = false;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonIgnore
    private String secretKey; // Store user's unique TOTP secret key

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonIgnore
    private String salt;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String token;

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }
}
