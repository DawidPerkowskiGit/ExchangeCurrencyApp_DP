package dpapps.exchangecurrencyapp.security;

import dpapps.exchangecurrencyapp.exchange.entities.ApiUser;
import dpapps.exchangecurrencyapp.exchange.repositories.ApiUserRepository;
import org.springframework.stereotype.Service;

@Service
public class ApiUserServiceImplementation implements ApiUserService{

    private final ApiUserRepository apiUserRepository;

    public ApiUserServiceImplementation(ApiUserRepository apiUserRepository) {
        this.apiUserRepository = apiUserRepository;
    }


    @Override
    public String addUser(ApiUser apiUser) {
        try {
            apiUserRepository.save(apiUser);
        }
        catch (Exception e) {
            return "Could not add user to the database. Exception: "+ e;
        }
        return "User added successfully";
    }
}
