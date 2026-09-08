package game.weather;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Random;

/**
 * This is the class to hold the information of a random locations weather information
 * and to return it.
 */
public class WeatherData {
    // This class was made with the assistance of AI, especially the parsing of the JSON file
    private int sum_of_rain = 0;

    /**
     * Constructor for the WeatherData class which records the necessary data
     */
    public  WeatherData() {
        Random random = new Random();

        double latitude = random.nextInt(-90, 90);
        double longitude = random.nextInt(-180, 180);
        String rainURL = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude
                + "&longitude=" + longitude
                + "&daily=precipitation_sum&timezone=auto";

        try {
            // 1. Initialize the built-in HTTP client
            HttpClient client = HttpClient.newHttpClient();

            // 2. Build the request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(rainURL))
                    .GET()
                    .build();

            // 3. Send the request and get the text response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // 4. Use the Maven-imported Gson library to parse the text response
                JsonObject rootObject = JsonParser.parseString(response.body()).getAsJsonObject();

                // Navigate into the "daily" object block
                JsonObject dailyData = rootObject.getAsJsonObject("daily");

                // Extract the JSON arrays for dates and precipitation values
                JsonArray precipitationArray = dailyData.getAsJsonArray("precipitation_sum");

                for (int i = 0; i < precipitationArray.size(); i++) {
                    sum_of_rain += precipitationArray.get(i).getAsInt();
                }

            } else {
                System.out.println("Error: Unable to connect to API. Status code: " + response.statusCode());
            }

        } catch (Exception e) {
            System.out.println("An exception occurred while fetching weather data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public int getSum_of_rain(){
        return sum_of_rain;
    }
}