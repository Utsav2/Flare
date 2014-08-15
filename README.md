Flare does everything a Mine reporting application would need, and is easily customizable for any developers willing to make changes.

Major advantages
================

It works on Android, which is widely used in developing countries. and by the introduction of cheap smartphones, is rising in growth.
It detects whether the user has internet connection or not, and accordingly changes the information sent to the authorities. Reporting a mine is very quick and easy.
It provides an API access to the mine database it creates, easily allowing other developers to create applications using information from the database.
An SMS gateway to receive messages is not needed, which is expensive to set up and hard to maintain. Instead, messages can be routed to Android phones that are connected to internet who then add them to the database.
Built on Heroku, which is very easy to scale on the click of a button if the application grows. It currently supports 10,000 reports saved.

Minor Advantages
================

Simple user interface for authorities to perform actions. No need to train anyone to use the application, as it is intuitive. Tested on Windows and Mac, and screen sizes 11 - 15 inches, will work great on big monitors.
Latitude and Longitude attached to Internet reports, thus easily pinpointed data for 2G or 3G data

The user
========

Flare is aimed at people with very slow smartphones, which cannot support a large overbearing application running on their phone. Having 2G just opens an instance of Google Maps, which attempts to triangulate the user position. The ability to take pictures and upload it is already coded into the application, but is disabled.

The authority
=============

Flare is aimed at authorities who do not have large budgets for fancy servers receiving messages, and for countries where internet access is very very limited. The android app does not need internet at any stage at all, even for registration. Simply on setting it up, the set up page can be used to set the number which is used to upload the reports. Instead of having an SMS server, the Flare application has a Volunteer option. A phone with the Volunteer option opened and connected to the internet receives messages and adds them to the database. So, a cheap Android phone connected to the internet can be used as a server. This makes it a sustainable solution

Technical details - Android
============================

The mechanism to send reports is built to use as less performance power as possible. One activity is created, called MainActivity, that sets up Fragments for each component. For example, opening a Google map is in a fragment class called LocationFragment. The phone is locked from rotation and the instance state is saved in each fragment, thus, the activity is never created after its initial creation. Thus, the app needs very less processing speed. Also, a camera and image view fragment is coded to help authorities determine the type of device, but is disabled assuming low internet capabilities.

To switch around the type of data collected is really simple, and adds very marginal load to user devices. The Fragments already coded are:

Description Fragment, which allows users to send information. As many of these can be added, for example one for location, and one for a message regarding the mine.
Location Fragment, which lets users select their location by opening a Google map.
Camera and Image View Fragment which lets users take a picture and upload it.
All the fragments interact with the MainActivity to save information. A feature that could be added on demand is a voice record fragment, but on 2G, along with pictures, is impractical.

For internet reports, Apache HTTP client is used in conjunction with Google Volley, which is designed for low volume requests. The timeout is set to 60 seconds, after which the request is sent again. This can be easily changed.

Technical details - Web
========================

The web application is developed in Python using Flask for the server side, and Angular JS as a framework on the front end. A PostgresSQL database is used using SQLAlchemy. The configuration is minimal, and as a web application, can be used on any computer. Reports are categorized by country and administrative area. The API is consistent and simple. For example, all requests to the application have the base url "calm-beach-2980.herokuapp.com/". To get mine data, make a call to base_url + "getMineData" with a "country" parameter and/or an "area" parameter.

Therefore, a request like "calm-beach-2980.herokuapp.com/getMineData?country=India&area=Maharashtra" will return in JSON all the mines categorized in that area.

Note, as SMS requests can be from anywhere in the country, np SMS report is returned if an area parameter is specified. Omit the "area" parameter to get all the SMS and Internet reports from a certain country. For example, "calm-beach-2980.herokuapp.com/getMineData?country=United+States" will return all the reports from the USA, or an empty JSON array [] if there are no reports from there.

Note
====

Although Flare already packs many features, it is just a backbone. I do not know anyone with experience with countries with many landmines, so I do not know what specific feature would be required by them. Thus, the application has been made very generic and easy to add to. As the framework for sending messages and receiving messages has already been completed for Android, it could easily integrate features like receive reports of mines in their vicinity and receive instant notifications. The web application saves an extensive amount of information for each report: the IMEI number of the sender, the exact time of sending and the phone number of the recipient. This could be easily be used to block users that send false reports. Also, as the web application provides a simple API, authorities on ground can be instantly notified with data about each mine. All these features though, are subjective and might be used by some and not by others. So Flare is designed to easily adapt to any use case a specific organization might need.

