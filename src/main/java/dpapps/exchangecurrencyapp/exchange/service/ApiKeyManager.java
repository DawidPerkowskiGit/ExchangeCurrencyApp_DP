package dpapps.exchangecurrencyapp.exchange.service;

import dpapps.exchangecurrencyapp.configuration.AppVariables;
import dpapps.exchangecurrencyapp.exchange.model.ApiKey;
import dpapps.exchangecurrencyapp.exchange.model.Role;
import dpapps.exchangecurrencyapp.exchange.model.User;
import dpapps.exchangecurrencyapp.exchange.repositories.ApiKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for managing API keys
 */
@Service
public class ApiKeyManager {

    private final ApiKeyRepository apiKeyRepository;

    @Autowired
    public ApiKeyManager(ApiKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }

    /**
     * Method which determines if user can use this API key
     *
     * @param apiKey Api key
     * @param user User which uses the ApiKey
     * @return Boolean result
     */
    public boolean canUseTheApiKey(ApiKey apiKey, User user) {
        if (doesUserHaveSpecificRole("ROLE_ADMIN", user)) {
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

    /**
     * Method checks if user has specific role
     *
     * @param role Check for this role
     * @param user User which needs the role checked
     * @return Boolean result
     */
    public boolean doesUserHaveSpecificRole(String role, User user) {
        for (Role singleRole : user.getRoles()) {
            if (singleRole.getName().equals(role)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Generates new Api key for the user and deactivates all other active keys
     * @param user User who receives the ApiKey
     * @return String result of generating new API key
     */

    public String generateNewKey(User user) {
        if (user.isNonLocked() == false) {
            return "Could not generate new API key, account is locked";
        }
        for (ApiKey singleKey : user.getApiKeys()) {
            if (singleKey.isActive()) {
                singleKey.deactivateKey();
            }
        }
        ApiKey apiKey = new ApiKey();
        try {
            apiKey.generateNewValue();
            apiKey.setUser(user);
            addApiKey(apiKey, user);
            apiKeyRepository.save(apiKey);
        } catch (Exception e) {
            return "Could not generate new API key. Exception: " + e;
        }
        return "Generated new API key: " + apiKey.getValue();
    }

    /**
     * Returns this users active API key
     *
     * @param user User
     * @return active API key
     */
    public ApiKey returnActiveKey(User user) {
        for (ApiKey key : user.getApiKeys()) {
            if (key.isActive()) {
                return key;
            }
        }
        return new ApiKey();
    }

    /**
     * Add this API key to users account
     *
     * @param apiKey API key
     * @param user User
     */

    public void addApiKey(ApiKey apiKey, User user) {
        user.getApiKeys().add(apiKey);
    }

    /**
     * Method that checks if API key is valid
     *
     * @param apiKey API key
     * @return result of API key check
     */
    public String checkApiKey(String apiKey) {
        if (apiKey == null) {
            return "You didnt provide api key";
        }

        if (apiKeyRepository.existsByValue(apiKey) == false) {
            return "Api key is invalid";
        }

        return AppVariables.VALID_API_KEY_MESSAGE;
    }


}
