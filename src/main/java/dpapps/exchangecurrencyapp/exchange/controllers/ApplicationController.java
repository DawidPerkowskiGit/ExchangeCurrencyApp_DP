package dpapps.exchangecurrencyapp.exchange.controllers;

import dpapps.exchangecurrencyapp.configuration.AppVariables;
import dpapps.exchangecurrencyapp.exchange.model.User;
import dpapps.exchangecurrencyapp.exchange.repositories.UserRepository;
import dpapps.exchangecurrencyapp.exchange.service.ApiKeyManager;
import dpapps.exchangecurrencyapp.exchange.service.MainControllerService;
import dpapps.exchangecurrencyapp.security.UserDto;
import dpapps.exchangecurrencyapp.security.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Controller for non REST API endpoints
 */
@Controller
public class ApplicationController {

    private final MainControllerService controller;


    @Autowired
    public ApplicationController(MainControllerService controller) {
        this.controller = controller;
    }

    /**
     * Home mapping endpoint
     * @return Homepage view
     */

    @GetMapping("/")
    public String homeMain() {
        return controller.getHomePage();
    }

    /**
     * Homepage mapping endpoint
     * @return Homepage view
     */

    @GetMapping("")
    public String homeMain2() {
        return controller.getHomePage();
    }

    /**
     * Health check endpoint
     *
     * @return HTTP status 200 - OK
     */

    @GetMapping("/health")
    public ResponseEntity<Void> healthCheck() {
        return controller.getHealthStatus();
    }

    /**
     * Page index endpoint
     *
     * @return Index page view
     */

    @GetMapping("/index")
    public String home() {
        return controller.getIndex();
    }

    /**
     * Register user endpoint
     *
     * @param model User registration fields model
     * @return register view
     */

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        return controller.register(model);
    }

    /**
     * Register user and save its info to database endpoint
     *
     * @param userDto User data transfer object
     * @param result  Attribute that enables checking if login already exists
     * @param model   Registration data model
     * @return "/register" view if registration did not result in success
     * "/index" view if registration was a success
     */
    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model) {
        return controller.processRegister(userDto, result, model);
    }

    /**
     * Admin only view that displays list of users
     *
     * @param model View model
     * @return Users view
     */
    @GetMapping("/users")
    public String users(Model model) {
        return controller.getUsers(model);
    }

    /**
     * User logging in endpoint
     *
     * @return Login view
     */
    @GetMapping("/login")
    public String login() {
        return controller.processLogin();
    }

    /**
     * Accessing this endpoint generates new API key
     *
     * @return User profile view
     */
    @GetMapping("/generate")
    public String generateApiKey() {
        return controller.generateNewApiKey();
    }

    /**
     * Users profile view
     *
     * @param model Model which includes user and API key data
     * @return Profile view
     */
    @GetMapping("/profile")
    public String profile(Model model) {
        return controller.getProfile(model);
    }
}
