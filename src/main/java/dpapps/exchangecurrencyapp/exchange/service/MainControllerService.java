package dpapps.exchangecurrencyapp.exchange.service;

import dpapps.exchangecurrencyapp.security.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

public interface MainControllerService {

    /**
     * Returns Homepage view
     */
    String getHomePage();

    /**
     * Returns HTTP status 200 - OK
     */
    ResponseEntity<Void> getHealthStatus();

    /**
     * Return index page view
     */
    String getIndex();

    /**
     * Return register user service view
     */
    String register(Model model);


    /**
     * Register user and save its info to database service
     */
    String processRegister(UserDto userDto, BindingResult result, Model model);


    /**
     * Admin only service which returns list of registered users
     */
    String getUsers(Model model);


    /**
     * Validates user data, performs authentication
     */
    String processLogin();


    /**
     * New API key generation service
     */
    String generateNewApiKey();

    /**
     * Returns user profile view
     */

    String getProfile(Model model);


    /**
     * Returns logs view
     */
    String getLogs(Model model);

}
