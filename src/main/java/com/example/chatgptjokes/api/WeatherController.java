package com.example.chatgptjokes.api;

import com.example.chatgptjokes.dtos.MyResponse;
import com.example.chatgptjokes.service.OpenAiService;
import com.example.chatgptjokes.service.WeatherService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

/**
 * This class handles fetching a joke via the ChatGPT API
 */
@RestController
@RequestMapping("/api/v1/weather")
@CrossOrigin(origins = "*")

public class WeatherController {

        private final OpenAiService openAiService;
        private final WeatherService weatherService;

        public WeatherController(OpenAiService openAiService, WeatherService weatherService) {
            this.openAiService = openAiService;
            this.weatherService = weatherService;
        }

        /**
         * This contains the message to the ChatGPT API, telling the AI how it should act in regard to the requests it gets.
         */
        final static String SYSTEM_MESSAGE = "You are a helpful weather assistant. Tell me whether this weather data is normal to the area at this time of year. \n" +
                "Please, type out answer like this:\n" +
                "location: \n" +
                "temperature:\n" +
                "descriptionOfWeather:\n" +
                "WeatherConditions. Give explanation at around 100 characters: \n" +
                "You also return the lat and lng of the city, that has been queried, e.g. \n" +
                "position: lat: 55.6761, lng: 12.5683.\n" +
                "Return all data in English and return data in JSON-format like this:\n" +
                "{\n" +
                "  \"location\": \"CityName\", Country,\n" +
                "  \"temperature\": \"TemperatureValue\",\n" +
                "  \"descriptionOfWeather\": \"WeatherDescription\",\n" +
                "  \"weatherConditions\": \"Explanation\",\n" +
                "  \"position\": {\n" +
                "    \"lat\": LatitudeValue,\n" +
                "    \"lng\": LongitudeValue\n" +
                "  }"
                ;


        /**
         * The controller called from the browser client.
         * @param service
         */
//  public JokeController(OpenAiService service) {
//    this.service = service;
//  }

        /**
         * Handles the request from the browser client.
         *
         * @param about contains the input that ChatGPT uses to make a joke about.
         * @return the response from ChatGPT.
         */

        @GetMapping
        public MyResponse getWeather(@RequestParam String about) {
            System.out.print(about);
            try {
                String weatherData = weatherService.getWeatherData(about);
                System.out.println(weatherData);
                JsonNode weatherNode = new ObjectMapper().readTree(weatherData);
                String temperature = weatherNode.get("CurrentData").get("temperature").asText();
                String skyText = weatherNode.get("CurrentData").get("skyText").asText();

                // Combine weather information with user prompt
                String prompt = about + ". Weather: Temperature is " + temperature + "°C. Sky: " + skyText;

                if (prompt != null && SYSTEM_MESSAGE != null) {
                    MyResponse response = openAiService.makeRequest(prompt, SYSTEM_MESSAGE);
                    return response;
                } else {
                    throw new IllegalArgumentException("Prompt or SYSTEM_MESSAGE is null");
                }
            } catch (Exception e) {
                System.out.println("Something went wrong " + e);
                return new MyResponse("An error occurred while fetching weather data.");
            }
        }
    }

