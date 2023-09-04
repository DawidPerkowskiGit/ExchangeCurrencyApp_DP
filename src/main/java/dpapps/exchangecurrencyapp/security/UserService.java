package dpapps.exchangecurrencyapp.security;

import dpapps.exchangecurrencyapp.exchange.model.User;

import java.util.List;

/**
 * Interface for service which adds user to the database, or finds one user by username
 */
public interface UserService {
    void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();

    public User getCurrentUser();
}