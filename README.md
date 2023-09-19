# Content of Project
* [General info](#general-info)
* [Technologies](#technologies)
* [More detailed information about application](#more-detailed-information-about-application)
* [Available endpoints](#available-endpoints)
* [Application view](#application-view)
## General info
A currency exchange application developed with a CI/CD workflow, providing REST API endpoints that return selected exchange rates in JSON format. The application automatically builds and deploys with every push to the repository using Render. Docker containers are used for the deployment process. Hosted on Render's free plan.

I created frontend application developed in Angular, which displays requested data.  https://github.com/DawidPerkowskiGit/DP_Exchange_Currency_App_NG

## Technologies

Java 17, Spring 6, Spring Boot, Spring Web, Spring Data, Spring Security, PostgresSQL 15, Maven, Docker, Git, JUnit, Render, Jackson, Thymeleaf.

## More detailed information about application

The application and its database PostgresSQL is hosted on Render.com. Database stores over 30 different currency types and their exchange rates, first entry is from 1999-01-04. Every four hours application performs automatic REST API request to exchangeratesapi.io in search of new exchange rates. If received exchanges are new, the data is inserted to the database. Users have to create account and use their API key to retrieve data.

URL of the application: https://exchangecurrencyapp-dp-render.onrender.com

## Available endpoints   

### /api/currencies

The user can access this endpoint without providing api key. Returns list of all available currencies (including ones no longer used) in JSON format.

Available optional URL parameters:
- date - provide date when you want to fetch currencies, which were actively used at specified time

```json
{
    "currencies": {
        "0": "EUR",
        "1": "USD",
        (...)
        "40": "PLN",
        "41": "ISK"
    }
}
```

### /api/currencies/locations

The user can access this endpoint without providing api key. Returns list of available currencies and locations they can be used in JSON format, for example:
```json
[
    {
        "isoName": "CHF",
        "fullName": "Swiss franc",
        "locationList": [
            "Switzerland",
            "Liechtenstein"
        ]
    },
    {
        "isoName": "HRK",
        "fullName": "Croatian kuna",
        "locationList": [
            "Croatia"
        ]
    },
(...)
]
```

### /api/exchange

Returns requested exchange rates.

#### Parameters

Required URL parameters:
- apiKey - required key to perform API request

Available optional URL parameters:
- currency - exchange rates of chosen currency types
- baseCurrency - changing the base currency(default is EUR)
- startDate - return exchanges starting from the chosen date
- finishDate - return exchanges finishing at the chosen date
- currencyValue - calculate and return value of exchange rates of requested currency from a single day

Available currencies:
```
AUD,BGN,BRL,CAD,CHF,CNY,CYP,CZK,DKK,EEK,EUR,GBP,HKD,HRK,HUF,IDR,ILS,INR,ISK,JPY,KRW,LTL,LVL,MTL,MXN,MYR,NOK,NZD,PHP,PLN,ROL,RON,RUB,SEK,SGD,SIT,SKK,THB,TRL,TRY,USD,ZAR
```

Examples of valid currency or baseCurrency parameters:
```
AUD
```
```
EUR,USD
```

Date format is as follows:
```
yyyy-MM-dd
```

Example of valid date format:
```
2020-05-15
```
#### Valid request

The user can access this endpoint only when providing an api key. Returns exchange rates list in JSON format, for example:
```json
{
  "exchangeList": [
    {
      "success": true,
      "date": "2023-08-30",
      "base": "EUR",
      "rates": {
        "CHF": 0.955214,
        "HRK": 7.482101,
        "MXN": 18.257594,
        (...)
        "CZK": 24.096473,
        "SEK": 11.829196,
        "NZD": 1.821493,
        "BRL": 5.275342
      }
    }
  ]
}
```

When the 'currencyValue' parameter was provided and requesting single day data. Requires both 'currency' and 'baseCurrency' to be valid.
```json
{
    "message": "5.0 EUR = 22.330545 PLN; Date of exchange 2023-08-30",
    "exchangeDate": "2023-08-30",
    "requestedValue": 5.0,
    "rate": 4.466109,
    "calculatedValue": 22.330545,
    "baseCurrency": "EUR",
    "requestedCurrency": "PLN"
}
```
#### Invalid requests

You will receive following responses while not providing valid values to requested, optional parameters and when data does not exist in the database.

##### API KEY was not provided
```json
{
  "success": false,
  "status": 403,
  "message": "You did not provide an API KEY"
}
```

##### API KEY is invalid
```json
{
    "success": false,
    "status": 403,
    "message": "Provided API KEY is invalid"
}
```

##### Requested currency is unrecognizable
```json
{
    "success": false,
    "status": 404,
    "message": "Cannot perform your request. Requested currency is not found"
}
```

##### Base currency is not recognizable
````json
{
    "success": false,
    "status": 404,
    "message": "Cannot perform your request. Base currency is not found"
}
````

##### Start date format is invalid
```json
{
    "success": false,
    "status": 400,
    "message": "Cannot perform your request. Invalid start date format"
}
```

##### Finish date format is invalid
```json
{
    "success": false,
    "status": 400,
    "message": "Cannot perform your request. Invalid finish date format"
}
```

##### Exchange rate from specific date does not exist in the database
```json
{
    "success": false,
    "status": 500,
    "message": "Failed to return exchange rates. There is no data matching your request criteria"
}
```

## Application view

Frontend application which displays REST API data: https://github.com/DawidPerkowskiGit/DP_Exchange_Currency_App_NG.


Result of requesting the latest exchange rates data.

![obraz](https://github.com/DawidPerkowskiGit/ExchangeCurrencyApp_DP/assets/87314459/1e6ca59d-10b7-4080-b04a-9719e8a9ed18)




Api key section available to logged-in users.

![obraz](https://github.com/DawidPerkowskiGit/ExchangeCurrencyApp_DP/assets/87314459/4db0bdc6-087a-438f-9287-867802ea9b58)
