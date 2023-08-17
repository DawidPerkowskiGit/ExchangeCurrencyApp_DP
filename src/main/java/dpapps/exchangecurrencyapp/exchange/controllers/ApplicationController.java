package dpapps.exchangecurrencyapp.exchange.controllers;

import dpapps.exchangecurrencyapp.configuration.AppVariables;
import dpapps.exchangecurrencyapp.exchange.entities.User;
import dpapps.exchangecurrencyapp.exchange.repositories.ApiKeyRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.UserRepository;
import dpapps.exchangecurrencyapp.exchange.services.ApiKeyManager;
import dpapps.exchangecurrencyapp.security.UserDto;
import dpapps.exchangecurrencyapp.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.validation.Valid;

import java.util.List;

/**
 * Controller for non REST API endpoints
 */
@Controller
public class ApplicationController {

    private final UserService userService;
    private final ApiKeyRepository apiKeyRepository;
    private final UserRepository userRepository;


    @Autowired
    public ApplicationController(UserService userService, ApiKeyRepository apiKeyRepository, UserRepository userRepository) {
        this.userService = userService;
        this.apiKeyRepository = apiKeyRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String homeMain() {
        return "homePage";
    }

    @GetMapping("")
    public String homeMain2() {
        return "homePage";
    }

    @GetMapping("/health")
    public ResponseEntity<Void> healthCheck() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/index")
    public String home() {
        return "index";
    }

    /**
     * Register user endpoint
     *
     * @param model User registration fields model
     * @return register view
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        // create model object to store form data
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    /**
     * Register user and save its info to database
     *
     * @param userDto User data transfer object
     * @param result  Attribute that enables checking if login already exists
     * @param model   Registration data model
     * @return "/register" view if registration did not result in success
     * "/index" view if registration was a success
     */
    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model) {
        User existingUser = userService.findUserByEmail(userDto.getLogin());

        if (existingUser != null && existingUser.getLogin() != null && !existingUser.getLogin().isEmpty()) {
            result.rejectValue("login", null, "There is already an account registered with the same login");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "/register";
        }


        userService.saveUser(userDto);
        return "redirect:/index";
    }

    /**
     * Admin only view that displays list of users
     *
     * @param model View model
     * @return Users view
     */
    @GetMapping("/users")
    public String users(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "users";
    }

    /**
     * Handler method for logging in
     *
     * @return Login view
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * Method that generates new API key
     *
     * @return User profile view
     */
    @GetMapping("/generate")
    public String generate() {
        User user = getCurrentUser();
        ApiKeyManager apiKeyManager = new ApiKeyManager(apiKeyRepository, user);
        String output = apiKeyManager.generateNewKey();
        System.out.println(output);
        return "redirect:/profile";
    }

    /**
     * Users profile view
     *
     * @param model Model whoch includes user and API key data
     * @return Profile view
     */
    @GetMapping("/profile")
    public String profile(Model model) {
        User user = getCurrentUser();
        ApiKeyManager apiKeyManager = new ApiKeyManager(this.apiKeyRepository, user);
        model.addAttribute("user", apiKeyManager);
        String apiRequestsString = "" + user.getCurrentRequestsCount() + "/" + AppVariables.DAILY_LIMIT_OF_DAILY_USAGES;
        model.addAttribute("apiRequestString", apiRequestsString);
        return "profile";
    }

    /**
     * Method which returns currently authenticated user
     *
     * @return Authenticated user
     */
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        return user;
    }

}
