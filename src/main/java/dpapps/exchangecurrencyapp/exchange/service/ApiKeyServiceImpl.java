package dpapps.exchangecurrencyapp.exchange.service;

import dpapps.exchangecurrencyapp.configuration.AppConstants;
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
public class ApiKeyServiceImpl implements ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;

    @Autowired
    public ApiKeyServiceImpl(ApiKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }

    public int canUseTheApiKey(ApiKey apiKey, User user) {
        if (doesUserHaveSpecificRole(AppConstants.ROLE_ADMIN, user)) {
            return AppConstants.API_KEY_ADMIN;
        }
        if (user.isRequestLimitIsReached()) {
            return AppConstants.API_KEY_USE_LIMIT_REACHED;
        }
        if (!apiKey.isActive()) {
            return AppConstants.API_KEY_INACTIVE;
        }
        if (!user.isNonLocked()) {
            return AppConstants.API_KEY_USER_NOT_LOCKED;
        }
        return AppConstants.API_KEY_VALID;
    }

    public boolean doesUserHaveSpecificRole(String role, User user) {
        for (Role singleRole : user.getRoles()) {
            if (singleRole.getName().equals(role)) {
                return true;
            }
        }
        return false;
    }


    public String addNewKey(User user) {
        if (!user.isNonLocked()) {
            return AppConstants.USER_IS_LOCKED;
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

    public ApiKey returnActiveKey(User user) {
        for (ApiKey key : user.getApiKeys()) {
            if (key.isActive()) {
                return key;
            }
        }
        return new ApiKey();
    }

    public void addApiKey(ApiKey apiKey, User user) {
        user.getApiKeys().add(apiKey);
    }

    public int doesKeyExist(String apiKey) {
        if (apiKey == null) {
            return AppConstants.API_KEY_NOT_PROVIDED;
        }
        if (!apiKeyRepository.existsByValue(apiKey)) {
            return AppConstants.API_KEY_INVALID;
        }

        return AppConstants.API_KEY_VALID;
    }


}
