package dpapps.exchangecurrencyapp.jsonparser.responsepojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrencyNamesLocationObjectJsonReadyListCreator {
    public List<JsonConvertable> packCurrenciesLocationArrayToObject(Iterable<String[]> pojo) {

        Map<String, String> currencyFullNameAndIsoNameMap = new HashMap();
        Map<String, List<String>> currencyIsoNameAndLocationMap = new HashMap();

//        for (CurrencyNamesLocationObject singleEntry: pojo
//        ) {
//            if (currencyFullNameAndIsoNameMap.containsKey(singleEntry.getIso_name()) == false) {
//                currencyFullNameAndIsoNameMap.put(singleEntry.getIso_name(), singleEntry.getFull_name());
//            }
//            if (currencyIsoNameAndLocationMap.containsKey(singleEntry.getIso_name()) == false) {
//                currencyIsoNameAndLocationMap.put(singleEntry.getIso_name(), this.addValueReplaceMap(singleEntry.getIso_name(), currencyIsoNameAndLocationMap.get(singleEntry.getIso_name())));
//                currencyIsoNameAndLocationMap.put(singleEntry.getIso_name(), addValueReplaceMap(singleEntry.getIso_name() ,currencyIsoNameAndLocationMap.get(singleEntry.getIso_name())));
//            }
//
 //       }

        for (String[] singleEntry: pojo
        ) {
            if (currencyFullNameAndIsoNameMap.containsKey(singleEntry[0]) == false) {
                currencyFullNameAndIsoNameMap.put(singleEntry[0], singleEntry[1]);
            }
            if (currencyIsoNameAndLocationMap.containsKey(singleEntry[0]) == false) {
                currencyIsoNameAndLocationMap.put(singleEntry[0], new ArrayList<>());
            }
            currencyIsoNameAndLocationMap.put(singleEntry[0], addValueReplaceMap(singleEntry[2] ,currencyIsoNameAndLocationMap.get(singleEntry[0])));

        }

        List<JsonConvertable> jsonReadyObject = new ArrayList<>();

        for (String entry: currencyIsoNameAndLocationMap.keySet()
        ) {
            CurrencyNamesLocationObjectJsonReady singleObject = new CurrencyNamesLocationObjectJsonReady();
            singleObject.setIsoName(entry);
            singleObject.setFullName(currencyFullNameAndIsoNameMap.get(entry));
            singleObject.setLocationList(currencyIsoNameAndLocationMap.get(entry));
            jsonReadyObject.add(singleObject);
        }

        return jsonReadyObject;
    }

    /**
     *
     */
    List<String> addValueReplaceMap(String value, List<String> list) {
        list.add(value);
        return list;
    }
}
