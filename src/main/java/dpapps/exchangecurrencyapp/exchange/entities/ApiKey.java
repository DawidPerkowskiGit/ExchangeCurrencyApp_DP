package dpapps.exchangecurrencyapp.exchange.entities;

import dpapps.exchangecurrencyapp.configuration.AppVariables;
import dpapps.exchangecurrencyapp.exchange.tools.ApiKeyGenerator;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * Entity model of Api key
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ApiKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "value")
    private String value;

    @Column(name = "active")
    private boolean active = true;

    @ManyToOne
    private User user;

    public void generateNewValue() {
        setValue(ApiKeyGenerator.generateApiKey());
    }

    public void deactivateKey() {
        setActive(false);
    }

}
