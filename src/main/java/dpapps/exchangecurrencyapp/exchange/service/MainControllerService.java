package dpapps.exchangecurrencyapp.exchange.service;

import dpapps.exchangecurrencyapp.security.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

public interface MainControllerService {

    /**
     * Home mapping display service
     *
     * @return Homepage view
     */
    public String getHomePage();

    /**
     * Health check return service
     *
     * @return HTTP status 200 - OK
     */
    ResponseEntity<Void> getHealthStatus();

    /**
     * Page index display service
     *
     * @return Index page view
     */
    public String getIndex();

    /**
     * Register user service
     *
     * @param model User registration fields model
     * @return registration view
     */
    public String register(Model model);


    /**
     * Register user and save its info to database service
     *
     * @param userDto User data transfer object
     * @param result  Attribute that enables checking if login already exists
     * @param model   Registration data model
     * @return "/register" view if registration did not result in success
     * "/index" view if registration was a success
     */
    public String processRegister(UserDto userDto, BindingResult result, Model model);


    /**
     * Admin only service which list of users
     *
     * @param model View model
     * @return Users view
     */
    public String getUsers(Model model);


    /**
     * Handler method for logging in
     *
     * @return Login view
     */
    public String processLogin();


    /**
     * Service that generates new API key
     *
     * @return User profile view
     */
    public String generateNewApiKey();

    /**
     * Users profile service
     *
     * @param model Model which includes user and API key data
     * @return Profile view
     */

    public String getProfile(Model model);


}
