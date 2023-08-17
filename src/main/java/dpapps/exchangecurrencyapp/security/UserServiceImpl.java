package dpapps.exchangecurrencyapp.security;

import dpapps.exchangecurrencyapp.exchange.entities.Role;
import dpapps.exchangecurrencyapp.exchange.entities.User;
import dpapps.exchangecurrencyapp.exchange.repositories.ApiKeyRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.RoleRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.UserRepository;
import dpapps.exchangecurrencyapp.exchange.services.ApiKeyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of UserService service, provides several functionalities like saving user to the database, finds user, gets user's roles.
 */
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    private ApiKeyRepository apiKeyRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, ApiKeyRepository apiKeyRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.apiKeyRepository = apiKeyRepository;
    }

    /**
     * Saves user in the database
     *
     * @param userDto User data from registration form
     */
    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setLogin(userDto.getLogin());
        // encrypt the password using spring security
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role role = roleRepository.findByName("ROLE_USER");
        if (role == null) {
            role = checkRoleExist();
        }
        user.setRoles(Arrays.asList(role));

        userRepository.save(user);

        ApiKeyManager apiKeyManager = new ApiKeyManager(apiKeyRepository, user);
        String output = apiKeyManager.generateNewKey();
    }

    /**
     * Finds user based on username
     *
     * @param username Users login/username
     * @return Requested user
     */
    @Override
    public User findUserByEmail(String username) {
        return userRepository.findByLogin(username);
    }

    /**
     * Gets all the users from the database
     *
     * @return List of all users
     */
    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map((user) -> mapToUserDto(user)).collect(Collectors.toList());
    }

    /**
     * Converts User entity data to registration/login data
     *
     * @param user User entity data
     * @return UserDTO data
     */

    private UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setLogin(user.getLogin());
        return userDto;
    }

    /**
     * Checks if role exists, if not adds it to the database. Grants user the role.
     *
     * @return Role added to the user
     */

    private Role checkRoleExist() {
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        roleRepository.save(role);
        Role role1 = new Role();
        role1.setName("ROLE_USER");
        return roleRepository.save(role1);
    }
}