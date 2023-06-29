package dpapps.exchangecurrencyapp.security;

import dpapps.exchangecurrencyapp.exchange.entities.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();
}