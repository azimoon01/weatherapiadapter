import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.json.JSONObject;

public class WeatherApp {
    private WeatherAdapter weatherAdapter;
    private JFrame frame;
    private JTextField locationTextField;
    private JButton getWeatherButton;
    private JTextArea resultTextArea;

    public WeatherApp() {
        weatherAdapter = new WeatherAdapter();
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Weather App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        // Top Panel for User Input
        JPanel topPanel = new JPanel();
        frame.add(topPanel, BorderLayout.NORTH);

        JLabel locationLabel = new JLabel("Enter City:");
        locationTextField = new JTextField(20);
        getWeatherButton = new JButton("Get Weather");
        topPanel.add(locationLabel);
        topPanel.add(locationTextField);
        topPanel.add(getWeatherButton);

        // Center Panel for Result Display
        resultTextArea = new JTextArea(10, 30);
        resultTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(resultTextArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Button Action Listener
        getWeatherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String location = locationTextField.getText().trim();
                if (!location.isEmpty()) {
                    JSONObject weatherData = weatherAdapter.fetchWeatherData("OpenWeatherMap", location);
                    if (weatherData != null) {
                        displayWeatherData(weatherData);
                    } else {
                        displayErrorMessage("Error fetching weather data.");
                    }
                } else {
                    displayErrorMessage("Please enter a city.");
                }
            }
        });
    }

    private void displayWeatherData(JSONObject data) {
        String location = data.getString("location");
        double temperature = Double.parseDouble(data.getString("temperature").replaceAll("[^\\d.]", ""));
        double humidity = Double.parseDouble(data.getString("humidity").replaceAll("[^\\d.]", ""));
        String condition = data.getString("condition");

        String formattedResult = "Location: " + location + "\n" +
                "Temperature: " + String.format("%.1f", temperature) + " °C\n" +
                "Humidity: " + String.format("%.0f", humidity) + " %\n" +
                "Condition: " + condition;

        resultTextArea.setText(formattedResult);
    }


    private void displayErrorMessage(String message) {
        resultTextArea.setText(message);
    }

    private void displayGUI() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        WeatherApp weatherApp = new WeatherApp();
        weatherApp.displayGUI();
    }
}

