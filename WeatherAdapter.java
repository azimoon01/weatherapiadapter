import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherAdapter {
    public JSONObject fetchWeatherData(String apiName, String location) {
        if (apiName.equals("OpenWeatherMap")) {
            String apiUrl = "https://api.openweathermap.org/data/2.5/weather";
            String apiKey = "33303dc54f80f74d8711528dbf63a497";

            try {
                URL url = new URL(apiUrl + "?q=" + location + "&appid=" + apiKey);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();

                if (responseCode == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;
                    StringBuilder response = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    JSONObject apiData = new JSONObject(response.toString());
                    JSONObject commonData = mapToCommonModel(apiData);
                    return commonData;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private JSONObject mapToCommonModel(JSONObject apiData) {
        JSONObject commonData = new JSONObject();
        double temperature = apiData.getJSONObject("main").getDouble("temp") - 273.15; // Convert from Kelvin to Celsius
        double humidity = apiData.getJSONObject("main").getDouble("humidity");
        String condition = apiData.getJSONArray("weather").getJSONObject(0).getString("description");
        String location = apiData.getString("name");

        commonData.put("temperature", temperature + "°C");
        commonData.put("humidity", humidity + "%");
        commonData.put("condition", condition);
        commonData.put("location", location);

        return commonData;
    }

    public static void main(String[] args) {
        WeatherAdapter adapter = new WeatherAdapter();
        JSONObject weatherData = adapter.fetchWeatherData("OpenWeatherMap", "Aktobe");

        if (weatherData != null) {
            System.out.println("Common Weather Data:");
            System.out.println("Temperature: " + weatherData.getString("temperature"));
            System.out.println("Humidity: " + weatherData.getString("humidity"));
            System.out.println("Condition: " + weatherData.getString("condition"));
            System.out.println("Location: " + weatherData.getString("location"));
        }
    }
}
