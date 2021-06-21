# Android-Client-Server-App
![Cover image of the project](https://raw.githubusercontent.com/teobot/Android-Client-Server-App/main/banner-main.png)

## Project Information
*Github Link*: [Link To Repo](https://github.com/teobot/Android-Client-Server-App)
*Unit title*: **Advanced Programming**  
*Project Language*: **Java**  
*Assignment set by*: **K. Welsh**  
*Assignment ID*: **1CWK50**  
*Assignment title*: **Android Client Server App**  
*Assessment weighting*: **50%**  
*Assessment Grade*: **🌟97%🌟**   
*Type*: (Group/Individual) **Individual**  
*Hand-in deadline*: **21:00 1st April 2020**  

## Table of Contents
- [Android-Client-Server-App](#android-client-server-app)
  - [Project Information](#project-information)
  - [Table of Contents](#table-of-contents)
  - [Introduction](#introduction)
  - [The App](#the-app)
    - [Geolocation functionality (25 Marks)](#geolocation-functionality-25-marks)
    - [Fetching and displaying data (25 Marks)](#fetching-and-displaying-data-25-marks)
    - [Code Quality (25 Marks)](#code-quality-25-marks)
    - [Displaying results on a mapBox map (20 Marks)](#displaying-results-on-a-mapbox-map-20-marks)
    - [Threading (20 Marks)](#threading-20-marks)
    - [Results](#results)
      - [Result Comments](#result-comments)

## Introduction
A hackathon is an event, often held over 1-2 days, where software developers brainstorm and rapidly
develop a prototype of a new piece of software. At MMU, we regularly hold hackathons for students
looking to work on interesting projects outside of their studies, and we even have a student society
that travels the country attending hackathons arranged by other universities and organizations. The
format of this assessment was inspired by attending several of the hackathons here at MMU.

The hackathon will be a single day assessment, with the full specification released at
9:00 am on the day of the hackathon, and all submissions due by 9:00 pm that same day. It will
involve the creation of a client-server application, with an android app that retrieves data from a
server, showcasing all the skills you have gained on Advanced Programming throughout the year.

When visiting places that you aren't quite so familiar with, it can sometimes be difficult to figure out
how to get around via public transport. In this hackathon the aim was to create an android app that can sense its
location using GPS, and find out the names and distance to the nearest railway stations. This project will be
incorporating the android geolocation API, and the standard java/android networking and JSON
parsing libraries.

## The App
The android client will supply its current location to the server by placing it the query string of a
URL that it shall request from the server via HTTP. The server will supply data back in a JSON
format, and the android app will decode this data and display it to the user. The app
should be designed for use on a Google Pixel 3 phone, running Android 28, with a minimum
supported version of Android 23.

The app was marked on the following areas:
- Geolocation functionality (25 Marks)
- Fetching and displaying data (25 Marks)
- Code quality (10 Marks)
- Displaying results on a mapBox map (20 Marks)
- Threading (20 Marks)

### Geolocation functionality (25 Marks)
The app needed to track the devices location, so that when the user clicks the search button the app will know what latitude and longitude to supply to the server. I used the android location service and the LocationListener interface to have the app receive updates every time that the devices GPS chip informed the phone of a new location.

### Fetching and displaying data (25 Marks)
I created a URLConnection to fetch the data from teh server, i used the built-in JSON decoding library to decode the return data, I also designed a suitable user interface in which to display the results. I implemented a solution to calculate the distance to each of returned locations.

### Code Quality (25 Marks)
I designed the architecture to be easily readable and maintainable for other developers that might continue on my work. I commented each function to help developers navigate tricky parts of the app, I also generated a JavaDoc for a more in-depth documentation of the project.

### Displaying results on a mapBox map (20 Marks)
I implemented a MapBox map so that it can display the nearby train stations to the user. I also implemented a zooming method where upon searching for any new locations the map would pan and smartly adjust to include all data being displayed.

I added a custom map marker and the name of the station in a associated info window when you click the marker. This was tricky and wasn't include in direct study, this required me to explain my knowledge outside the university units.

### Threading (20 Marks)
I implemented threading, this is where a method will be sent off to another system thread to be calculated, this method couldn't directly update the UI as this was on another thread. This background thread made the request and returned back to update the UI thread.

### Results
I achieved the overall mark on **97%** for this hackathon assignment. 
#### Result Comments
>This looks like a very good attempt at the hackathon task, almost completing everything within the time-frame
> 
> I particularly liked the use of a LatLngBounds to dynamically zoom the map camera to fit stations on the screen.
The only issue I spotted was that you didn't use the strings.xml file correctly to store your hard-coded strings.
You could perhaps have separated the mapping code out more effectively using the decorator pattern, too.
> 
> These really are very minor things, and given the time constraint under which you were working, I'm delighted
with the work.
