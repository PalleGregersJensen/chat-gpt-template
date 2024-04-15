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
@RequestMapping("/api/v1/joke")
@CrossOrigin(origins = "*")
public class JokeController {

  private final OpenAiService openAiService;
  private final WeatherService weatherService;

  public JokeController(OpenAiService openAiService, WeatherService weatherService) {
    this.openAiService = openAiService;
    this.weatherService = weatherService;
  }

  /**
   * This contains the message to the ChatGPT API, telling the AI how it should act in regard to the requests it gets.
   */
  final static String SYSTEM_MESSAGE = "You are a helpful assistant. Show me exactly what you receive and in what format e.g. JSON. ";


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
  public MyResponse getJoke(@RequestParam String about) {
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


//  @GetMapping
//  public MyResponse getJoke(@RequestParam String about) {
//  public MyResponse getJoke(@RequestParam String about, @RequestParam String city) {
//    String weatherData = weatherService.getWeatherData(about);
//    MyResponse response = openAiService.makeRequest(about + ". Weather: " //+ weatherData, SYSTEM_MESSAGE);
    // Parse weather data and extract relevant information
//    try{JsonNode weatherNode = new ObjectMapper().readTree(weatherData);
//    String temperature = weatherNode.get("CurrentData").get("temperature").asText();
//    String skyText = weatherNode.get("CurrentData").get("skyText").asText();
    // You can extract other weather parameters as needed

    // Combine weather information with user prompt
//    String prompt = about + ". Weather: Temperature is " + temperature + "°C. Sky: " + skyText;

//    MyResponse response = openAiService.makeRequest(about + ". Weather: ", SYSTEM_MESSAGE);
//    return response;
//  }catch (Exception e) {
// System.out.print("Something went wrong " + e);}
//    return new MyResponse("An error occurred while fetching weather data.");
//  }
//}
