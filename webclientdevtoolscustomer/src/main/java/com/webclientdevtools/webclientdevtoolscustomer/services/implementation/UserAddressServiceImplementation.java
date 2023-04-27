package com.webclientdevtools.webclientdevtoolscustomer.services.implementation;

import com.webclientdevtools.webclientdevtoolscustomer.dto.AddressDto;
import com.webclientdevtools.webclientdevtoolscustomer.services.AddressService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class UserAddressServiceImplementation implements AddressService {

    private final WebClient webClient;

    public UserAddressServiceImplementation(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8090").build();
    }

    @Override
    public UUID createOrUpdateAddressANdReturnId(AddressDto address) {
        Mono<UUID> response = webClient.post()
                .uri("/address/with-id")
                .body(BodyInserters.fromValue(address))
                .retrieve()
                .bodyToMono(UUID.class);

        return response.block();
    }
}
