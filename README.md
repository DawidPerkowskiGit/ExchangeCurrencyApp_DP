# Content of Project
* [General info](#general-info)
* [Technologies](#technologies)
* [More detailed information about modules](#more-detailed-information-about-modules)

## General info

This is project of exchange currency application. 

## Technologies

Java 17, Spring 3.1.0, PostgreSQL 15, Maven, Docker, GitHub, Render.

## More detailed information about project

Every push action to the main repository triggers Render's deploying process. The service uses docker to build and deploy Spring application. All sensitive data like database connection credential are stored in local environment variables and Render's web service secrets.
