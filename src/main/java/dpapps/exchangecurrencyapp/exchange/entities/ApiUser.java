package dpapps.exchangecurrencyapp.exchange.entities;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "api_user")
public class ApiUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NonNull
    @Column(name="user_name")
    private String userName;

    @NonNull
    @Column(name="password")
    private String password;

    @NonNull
    @Column(name="is_locked")
    private boolean isLocked;

    @OneToMany
    @Column(name="api_keys")
    private List<ApiKey> apiKeys = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @NonNull
    public String getUserName() {
        return userName;
    }

    public void setUserName(@NonNull String userName) {
        this.userName = userName;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public List<ApiKey> getApiKeys() {
        return apiKeys;
    }

    public void setApiKeys(List<ApiKey> apiKeys) {
        this.apiKeys = apiKeys;
    }

    public void addApiKeyToList(ApiKey apiKey) {
        this.apiKeys.add(apiKey);
    }

    public boolean isKeyPresentInThisList(String value) {
        for (ApiKey apiKey: this.apiKeys
             ) {
            if (apiKey.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

    public ApiKey getAnApiKey(String value) {
        for (ApiKey apiKey: this.apiKeys
        ) {
            if (apiKey.getValue().equals(value)) {
                return apiKey;
            }
        }
        return new ApiKey();
    }
}
