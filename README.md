# Content of Project
* [General info](#general-info)
* [Technologies](#technologies)
* [More detailed information about application](#more-detailed-information-about-application)
* [Application view](#application-view)
## General info
A currency exchange application developed with a CI/CD workflow, providing REST API endpoints that return selected exchange rates in JSON format. The application automatically builds and deploys with every push to the repository using Render. Docker containers are used for the deployment process. Hosted on Render's free plan which results in app's instance spun down after 15 minutes of inactivity. 

## Technologies

Java 17, Spring 6, Spring Boot, Spring Web, Spring Data, Spring Security, PostgreSQL 15, Maven, Docker, Git, JUnit, Render, Jackson, Thymeleaf.

## More detailed information about application

The application and its database PostgreSQL is hosted on Render.com. Database stores over 30 different currency types and their exchange rates, first entry is from 1999-01-04. Every four hours application performs automatic REST API request to exchangeratesapi.io in search of new exchange rates. If received exchanges are new, the data is inserted to the database. Users have to create account and use their API key to retrieve data.

All sensitive data like database connection credentials are stored in local machine's environment variables for development and Render's web service secrets for deployment.

URL of the application: https://exchangecurrencyapp-dp-render.onrender.com

The apiUser can access those rates by sending request to /api/exchange endpoint.
Required URL parameter:
 - apiKey - required key to perform API request

Available optional URL parameters:
 - currency - exchange rates of chosen currency type
 - baseCurrency - changing the base currency(default is EUR)
 - startDate - return exchanges starting from the chosen date
 - finishDate - return exchanges finishing at the chosen date

Not providing values to these parameters will result in receiving the latest exchange date from all the currencies and Euro as the base.
## Application view

I created separate application that communicates using REST API and displays requested eschage rates data. Link to the repository: https://github.com/DawidPerkowskiGit/DP_Exchange_Currency_App_NG

![img.png](img.png)

![obraz](https://github.com/DawidPerkowskiGit/ExchangeCurrencyApp_DP/assets/87314459/4db0bdc6-087a-438f-9287-867802ea9b58)
