import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

public class WeatherApp {

    // Replace with your own OpenWeatherMap API key
    private static final String API_KEY = "YOUR_API_KEY";
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Enter city name: ");
        String city = scanner.nextLine();
        
        try {
            String weatherData = getWeatherData(city);
            if (weatherData != null) {
                parseAndDisplayWeatherData(weatherData);
            } else {
                System.out.println("Error: Unable to get weather data.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        scanner.close();
    }

    private static String getWeatherData(String city) throws Exception {
        String urlString = BASE_URL + "?q=" + city + "&appid=" + API_KEY + "&units=metric";
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        int responseCode = conn.getResponseCode();

        if (responseCode == 200) { // Success
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            in.close();
            return content.toString();
        } else {
            return null;
        }
    }

    private static void parseAndDisplayWeatherData(String weatherData) {
        JSONObject jsonObj = new JSONObject(weatherData);
        
        String cityName = jsonObj.getString("name");
        JSONObject main = jsonObj.getJSONObject("main");
        double temperature = main.getDouble("temp");
        int humidity = main.getInt("humidity");
        JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);
        String description = weather.getString("description");

        System.out.println("City: " + cityName);
        System.out.println("Temperature: " + temperature + "°C");
        System.out.println("Humidity: " + humidity + "%");
        System.out.println("Weather: " + description);
    }
}
