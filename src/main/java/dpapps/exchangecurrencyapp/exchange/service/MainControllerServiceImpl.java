package dpapps.exchangecurrencyapp.exchange.service;

import dpapps.exchangecurrencyapp.configuration.AppVariables;
import dpapps.exchangecurrencyapp.exchange.model.User;
import dpapps.exchangecurrencyapp.exchange.repositories.UserRepository;
import dpapps.exchangecurrencyapp.security.UserDto;
import dpapps.exchangecurrencyapp.security.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MainControllerServiceImpl implements MainControllerService {

    private final UserService userService;

    private final UserRepository userRepository;

    private final ApiKeyService apiKeyService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public MainControllerServiceImpl(UserService userService, UserRepository userRepository, ApiKeyService apiKeyService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.apiKeyService = apiKeyService;
    }

    /**
     * Home mapping display service
     *
     * @return Homepage view
     */
    public String getHomePage() {
        return "homePage";
    }

    /**
     * Health check return service
     *
     * @return HTTP status 200 - OK
     */
    public ResponseEntity<Void> getHealthStatus() {
        return ResponseEntity.ok().build();
    }

    /**
     * Page index display service
     *
     * @return Index page view
     */
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
        String output = apiKeyService.generateNewKey(user);
        logger.info(output);
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
        model.addAttribute("apiKeyManager", apiKeyService);
        String apiRequestsString = "" + user.getCurrentRequestsCount() + "/" + AppVariables.DAILY_LIMIT_OF_DAILY_USAGES;
        model.addAttribute("apiRequestString", apiRequestsString);
        return "profile";
    }

    /**
     * Returns logs file as a List
     * @param model model of response, list of logs will be added
     * @return log view name
     */

    public String getLogs(Model model) {
        List<String> result = new ArrayList<>();
        String fileName="logs/application.log";
        try (Stream<String> lines = Files.lines(Paths.get(fileName))) {
            result = lines.collect(Collectors.toList());

        }
        catch (Exception e) {
            logger.error("Could not load log file. Exception: "+ e);
        }
        model.addAttribute("result", result);
        return "log";
    }
}
