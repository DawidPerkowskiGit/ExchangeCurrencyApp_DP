package dpapps.exchangecurrencyapp.exchange.service;

import dpapps.exchangecurrencyapp.exchange.model.ApiKey;
import dpapps.exchangecurrencyapp.exchange.model.User;

public interface ApiKeyService {

    /**
     * Determines if the user can use this API key
     */
    int canUseTheApiKey(ApiKey apiKey, User user);

    /**
     * Determines if the user has specific a role
     */
    boolean doesUserHaveSpecificRole(String role, User user);

    /**
     * User receives new active API key. This also means deactivating other keys
     */
    String addNewKey(User user);

    /**
     * Returns users active API key
     */
    ApiKey returnActiveKey(User user);

    /**
     * Add this API key to users account
     */
    void addApiKey(ApiKey apiKey, User user);

    /**
     * Method checks if key exists in the database
     */
    int doesKeyExist(String apiKey);
}
