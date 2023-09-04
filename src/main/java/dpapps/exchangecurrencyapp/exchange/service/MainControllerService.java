package dpapps.exchangecurrencyapp.exchange.service;

import dpapps.exchangecurrencyapp.configuration.AppVariables;
import dpapps.exchangecurrencyapp.exchange.model.User;
import dpapps.exchangecurrencyapp.exchange.repositories.UserRepository;
import dpapps.exchangecurrencyapp.security.UserDto;
import dpapps.exchangecurrencyapp.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service
public class MainControllerService {

    private final UserService userService;

    private final UserRepository userRepository;

    private final ApiKeyManager apiKeyManager;

    @Autowired
    public MainControllerService(UserService userService, UserRepository userRepository, ApiKeyManager apiKeyManager) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.apiKeyManager = apiKeyManager;
    }

    public String getHomePage() {
        return "homePage";
    }
    public ResponseEntity<Void> getHealthStatus() {
        return ResponseEntity.ok().build();
    }

    public String getIndex() {
        return "index";
    }

    /**
     * Register user service
     *
     * @param model User registration fields model
     * @return registration view
     */
    public String register(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    /**
     * Register user and save its info to database service
     *
     * @param userDto User data transfer object
     * @param result  Attribute that enables checking if login already exists
     * @param model   Registration data model
     * @return "/register" view if registration did not result in success
     * "/index" view if registration was a success
     */
    public String processRegister(UserDto userDto, BindingResult result, Model model) {
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
     * Admin only service which list of users
     *
     * @param model View model
     * @return Users view
     */
    public String getUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "users";
    }

    /**
     * Handler method for logging in
     *
     * @return Login view
     */

    public String processLogin() {
        return "login";
    }

    /**
     * Service that generates new API key
     *
     * @return User profile view
     */

    public String generateNewApiKey() {
        User user = userService.getCurrentUser();
        String output = apiKeyManager.generateNewKey(user);
        System.out.println(output);
        return "redirect:/profile";
    }

    /**
     * Users profile service
     *
     * @param model Model which includes user and API key data
     * @return Profile view
     */
    public String getProfile(Model model) {
        User user = userService.getCurrentUser();
        model.addAttribute("user", user);
        model.addAttribute("apiKeyManager", apiKeyManager);
        String apiRequestsString = "" + user.getCurrentRequestsCount() + "/" + AppVariables.DAILY_LIMIT_OF_DAILY_USAGES;
        model.addAttribute("apiRequestString", apiRequestsString);
        return "profile";
    }
}
