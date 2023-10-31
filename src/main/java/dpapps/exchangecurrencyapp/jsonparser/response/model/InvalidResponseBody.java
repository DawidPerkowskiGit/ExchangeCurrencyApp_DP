package dpapps.exchangecurrencyapp.jsonparser.response.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model of JSON body returned if an error occurred
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvalidResponseBody implements JsonConvertable {
    private Boolean success = false;
    private int status;
    private String message;
}
