package dpapps.exchangecurrencyapp.exchange.controllers;

import dpapps.exchangecurrencyapp.exchange.service.MainControllerService;
import dpapps.exchangecurrencyapp.security.UserDto;
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
public class MainApplicationController {

    private final MainControllerService controller;


    @Autowired
    public MainApplicationController(MainControllerService controller) {
        this.controller = controller;
    }

    /**
     * Displays homepage view - GET
     */

    @GetMapping("/")
    public String homeMain() {
        return controller.getHomePage();
    }

    /**
     * Displays homepage view - GET
     */

    @GetMapping("")
    public String homeMain2() {
        return controller.getHomePage();
    }

    /**
     * Displays health check view - GET
     */

    @GetMapping("/health")
    public ResponseEntity<Void> healthCheck() {
        return controller.getHealthStatus();
    }

    /**
     * Displays index view after logging in - GET
     */

    @GetMapping("/index")
    public String home() {
        return controller.getIndex();
    }

    /**
     * Displays user registration view - GET
     */

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        return controller.register(model);
    }

    /**
     * Process user registration and save data in the database - POST
     */
    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model) {
        return controller.processRegister(userDto, result, model);
    }

    /**
     * Display registered users, accessible only by admins - GET
     */
    @GetMapping("/users")
    public String users(Model model) {
        return controller.getUsers(model);
    }

    /**
     * Display user login view - GET
     */
    @GetMapping("/login")
    public String login() {
        return controller.processLogin();
    }

    /**
     * Display user profile view after generating new API key - GET
     */
    @GetMapping("/generate")
    public String generateApiKey() {
        return controller.generateNewApiKey();
    }

    /**
     * Display user profile view - GET
     */
    @GetMapping("/profile")
    public String profile(Model model) {
        return controller.getProfile(model);
    }

    /**
     * Display logs view, admin accessible - GET
     */
    @GetMapping("/log")
    public String showLogs(Model model) {
        return controller.getLogs(model);
    }
}
