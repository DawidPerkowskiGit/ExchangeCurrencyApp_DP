# Content of Project
* [General info](#general-info)
* [Technologies](#technologies)
* [More detailed information about application](#more-detailed-information-about-application)
* [Application view](#application-view)
* [Other projects based on this application](#other-projects-based-on-this-application)
* [Todo](#todo)
## General info
Project of exchange rates web service. Provides REST API endpoint which returns chosen exchange rates. Every push to this repository triggers Render's build and deploy process which uses Docker containers to deploy final applciation.

## Technologies

Java 17, Spring 3.1.0, PostgreSQL 15, Maven, Docker, GitHub, JUnit, Render.

## More detailed information about application

The application uses PostgreSQL to store data of over 30 different currency types and their exchange rates, first entry is from 1999-01-04. Every four hours application performs automatic REST API request to exchangeratesapi.io in search of new exchange rates. If received exchanges are new, the data is inserted to the database. 

URL of the application: https://exchangecurrencyapp-dp-render.onrender.com

The user can access those rates by sending request to /api/exchange endpoint.
Available optional URL parameters:
 - currency - exchange rates of chosen currency type
 - baseCurrency - changing the base currency(default is EUR)
 - startDate - return exchanges starting from the chosen date
 - finishDate - return exchanges finishing at the chosen date

Not providing values to these parameters will result in receiving latest exchange date from all the currencies, when EUR is the base.
## Application view

![img.png](img.png)

## Todo

- Authorization and authentication
- UI graphic interface - another application developed with Angular. Sends REST API requests to this app and displays exchange ratios graphs and statistics.
