package com.example.chatgptjokes.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class WeatherService {

    private final WebClient webClient;

    public WeatherService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://vejr.eu/api.php").build();
    }

    public String getWeatherData(String city) {
        return webClient.get().uri(uriBuilder -> uriBuilder.queryParam("location", city).queryParam("degree", "C").build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
