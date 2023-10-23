package dpapps.exchangecurrencyapp.exchange.service;

import dpapps.exchangecurrencyapp.configuration.AppConstants;
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

    public String getHomePage() {
        return "homePage";
    }

    public ResponseEntity<Void> getHealthStatus() {
        return ResponseEntity.ok().build();
    }

    public String getIndex() {
        return "index";
    }

    public String register(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

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

    public String getUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "users";
    }

    public String processLogin() {
        return "login";
    }

    public String generateNewApiKey() {
        User user = userService.getCurrentUser();
        String output = apiKeyService.addNewKey(user);
        logger.info(output);
        return "redirect:/profile";
    }

    public String getProfile(Model model) {
        User user = userService.getCurrentUser();
        String apiRequestsString = "" + user.getCurrentRequestsCount() + "/" + AppConstants.DAILY_USE_LIMIT;
        model.addAttribute("apiRequestString", apiRequestsString);
        String apiKeyValue = apiKeyService.returnActiveKey(user).getValue();
        model.addAttribute("apiKeyValue", apiKeyValue);
        return "profile";
    }

    public String getLogs(Model model) {
        List<String> result = new ArrayList<>();
        String fileName = "logs/application.log";
        try (Stream<String> lines = Files.lines(Paths.get(fileName))) {
            result = lines.collect(Collectors.toList());

        } catch (Exception e) {
            logger.error("Could not load log file. Exception: " + e);
        }
        model.addAttribute("result", result);
        return "log";
    }
}
