package dpapps.exchangecurrencyapp.exchange.controllers;

import dpapps.exchangecurrencyapp.exchange.entities.ApiUser;
import dpapps.exchangecurrencyapp.exchange.entities.Role;
import dpapps.exchangecurrencyapp.exchange.repositories.ApiUserRepository;
import dpapps.exchangecurrencyapp.security.ApiUserService;
import dpapps.exchangecurrencyapp.security.ApiUserServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ApplicationController {

    private final ApiUserRepository apiUserRepository;

    @Autowired
    public ApplicationController(ApiUserRepository apiUserRepository) {
        this.apiUserRepository = apiUserRepository;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/health")
    public ResponseEntity<Void> helthCheack() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login")
        public String login() {
            return "login";
        }

    @ResponseBody
    @GetMapping("/register")
    public ModelAndView showRegisterForm(Model model) {

        model.addAttribute("user", new ApiUser());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register_form.html");

        return modelAndView;
    }

    @PostMapping("/process_register")
    public String processRegistration(ApiUser apiUser) {

//        ApiUser user = new ApiUser();
        if (apiUserRepository.existsByUserName(apiUser.getUsername())) {
            return "register?error";
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(apiUser.getPassword());
        apiUser.setPassword(encodedPassword);
        apiUser.setUserName(apiUser.getUserName());
        apiUser.setRole(Role.USER);

        ApiUserServiceImplementation apiUserServiceImplementation = new ApiUserServiceImplementation(this.apiUserRepository);
        apiUserServiceImplementation.addUser(apiUser);

        return "register_success";
    }
}
