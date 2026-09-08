# Real-World Weather Integration

> Original coursework proposal; proposed mechanics may differ from the final implementation.

Idea
The feature will be using a weather API known as Open-Meteo (https://open-meteo.com/). 
The idea is to collect current temperature, air quality, wind speed, uv index, and amount of precipitation,
at random real-world locations to force certain weather conditions to occur in game. 

Mechanics
•	Check on weather conditions in various real-world locations
•	Depending on specific parameters cause certain weather conditions to occur like: Rain, Gust of Garbage, and Acid rain.
•	When some parameters are extreme enough a natural disaster condition could occur like a Hurricane.
•	Depending on the uv index the player character might get sun burnt and require applying sunscreen.

Architecture
Abstract class: Weather condition class (A class for the general weather with the necessary
attributes and methods that would cause the map or actor to be effected )
Interface class:
Concrete classes for Weather:
•	Gust of Garbage (AQI of 140)
•	Fire rain (Temperature of 37C and 4mm/daily of rainfall)
•	Hurricane (55mph of windspeed)
Concrete classes for Rain: Rain, Fire rain, WeatherData (class for the data to be stored)
MapWeather(Class to check if WeatherCondition or Rain class should be called)
Rain (a class to implement raining in the map)
Higher level classes: Sunscreen (New Item class), Sunburnt (New Status class) (UV index of 3).

Request
Below is the api request to find the daily uv index and temperature for kl, this would be used to check if the character needs to wear sunscreen and if they get sun burnt.
https://api.open-meteo.com/v1/forecast?latitude=3.006&longitude=101.621&hourly=uv_index,temperature_2m&timezone=auto&forecast_days=1. 

Schema
{
  "latitude": 3.006,
  "longitude": 101.621,
  "timezone": "Asia/Kuala_Lumpur",
  "hourly": {
    "time": ["2026-05-31T10:00", "2026-05-31T11:00", "2026-05-31T12:00", "2026-05-31T13:00"],
    "temperature_2m": [31.2, 32.5, 33.1, 32.8],
    "uv_index": [4.1, 7.5, 9.2, 8.4]
  }
}
