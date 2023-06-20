package dpapps.exchangecurrencyapp.jsonparser;

import com.fasterxml.jackson.databind.ObjectMapper;
import dpapps.exchangecurrencyapp.exchange.entities.Currency;
import dpapps.exchangecurrencyapp.exchange.entities.Exchange;
import dpapps.exchangecurrencyapp.exchange.repositories.CurrencyRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.ExchangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ResponseBodyJsonParser {

    private final boolean DEBUG = true;



    public ResponseBodyPojo parseJsonFromFile(String filePath) {

        ResponseBodyPojo responseBodyPojo = new ResponseBodyPojo();
        String jsonInString = "";

        try {
            jsonInString = readFileAsString(filePath);
        }
        catch (Exception e) {
            System.out.println("Could not load file. Exception "+e);
            return null;
        }

        return jsonDeserialization(jsonInString);
    }


/*    public ResponseBodyPojo parseJsonFromApi() {

        ResponseBodyPojo responseBodyPojo;

        String retrieveApiResponse = new ResponseBodyRetriever().retrieveApiResponse();
        if (retrieveApiResponse.equals("")) {
            return null;
        }

        return jsonDeserialization(retrieveApiResponse);
    }*/

    public ResponseBodyPojo jsonDeserialization(String jsonInString) {
        ResponseBodyPojo responseBodyPojo;
        try {
            ObjectMapper mapper = new ObjectMapper();
            responseBodyPojo = mapper.readValue(jsonInString, ResponseBodyPojo.class);
            if (DEBUG) {
                String pojoToString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseBodyPojo);
                System.out.println(pojoToString);
            }
        }
        catch (Exception e) {
            System.out.println("Could not parse JSON body. Exception: " + e);
            return null;
        }
        return responseBodyPojo;
    }


    public String readFileAsString(String file)throws Exception
    {
        return new String(Files.readAllBytes(Paths.get(file)));
    }


}