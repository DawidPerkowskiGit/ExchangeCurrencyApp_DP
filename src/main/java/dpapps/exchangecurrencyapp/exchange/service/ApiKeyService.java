package dpapps.exchangecurrencyapp.exchange.service;

import dpapps.exchangecurrencyapp.exchange.model.ApiKey;
import dpapps.exchangecurrencyapp.exchange.model.User;

public interface ApiKeyService {

    /**
     * Method which determines if user can use this API key
     *
     * @param apiKey Api key
     * @param user User which uses the ApiKey
     * @return Boolean result
     */
    public boolean canUseTheApiKey(ApiKey apiKey, User user);

    /**
     * Method checks if user has specific role
     *
     * @param role Check for this role
     * @param user User which needs the role checked
     * @return Boolean result
     */
    public boolean doesUserHaveSpecificRole(String role, User user);

    /**
     * Generates new Api key for the user and deactivates all other active keys
     * @param user User who receives the ApiKey
     * @return String result of generating new API key
     */
    public String generateNewKey(User user);

    /**
     * Returns this users active API key
     *
     * @param user User
     * @return active API key
     */
    public ApiKey returnActiveKey(User user);

    /**
     * Add this API key to users account
     *
     * @param apiKey API key
     * @param user User
     */
    public void addApiKey(ApiKey apiKey, User user);

    /**
     * Method that checks if API key is valid
     *
     * @param apiKey API key
     * @return result of API key check
     */
    public String checkApiKey(String apiKey);
}
