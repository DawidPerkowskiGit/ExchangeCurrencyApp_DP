package dpapps.exchangecurrencyapp.exchange.error;

import dpapps.exchangecurrencyapp.jsonparser.response.model.JsonConvertable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvalidRequestBody implements JsonConvertable {
    private Boolean success = false;
    private int status;

    private String message;
}
