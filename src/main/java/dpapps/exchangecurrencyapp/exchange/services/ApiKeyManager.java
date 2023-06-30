package dpapps.exchangecurrencyapp.exchange.services;

import dpapps.exchangecurrencyapp.exchange.entities.ApiKey;
import dpapps.exchangecurrencyapp.exchange.entities.Role;
import dpapps.exchangecurrencyapp.exchange.entities.User;
import dpapps.exchangecurrencyapp.exchange.repositories.ApiKeyRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class ApiKeyManager {

    private User user;

    ApiKeyRepository apiKeyRepository;

    public ApiKeyManager(ApiKeyRepository apiKeyRepository, User user) {
        this.apiKeyRepository = apiKeyRepository;
        this.user = user;
    }


    public boolean canUseTheApiKey(ApiKey apiKey) {
        if (doesUserHaveSpecificRole("ROLE_ADMIN")) {
            return true;
        }
        if (user.isRequestLimitIsReached()) {
            return false;
        }
        if (apiKey.isActive() == false) {
            return false;
        }
        if (user.isNonLocked() == false) {
            return false;
        }
        return true;
    }

    public boolean doesUserHaveSpecificRole(String role) {
        for (Role singleRole: user.getRoles()
        ) {
            if (singleRole.getName().equals(role)) {
                return true;
            }
        }
        return false;
    }

    public String generateNewKey() {
        if (user.isNonLocked() == false) {
            return "Could not generate new API key, account is locked";
        }
        for (ApiKey singleKey: user.getApiKeys()
        ) {
            if (singleKey.isActive()) {
                singleKey.deactivateKey();
            }
        }
        ApiKey apiKey = new ApiKey();
        try {
            apiKey.generateNewValue();
            apiKey.setUser(user);
            addApiKey(apiKey);
            apiKeyRepository.save(apiKey);
        }
        catch (Exception e) {
            return "Could not generate new API key. Exception: "+e;
        }
        return "Generated new API key: " + apiKey.getValue();
    }

    public void addApiKey(ApiKey apiKey) {
        user.getApiKeys().add(apiKey);
    }



}
