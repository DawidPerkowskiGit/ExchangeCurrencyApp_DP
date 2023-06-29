package dpapps.exchangecurrencyapp.exchange.entities;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "api_user")
public class ApiUser implements UserDetails {
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

    @Enumerated(EnumType.STRING)
    private Role role;

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
