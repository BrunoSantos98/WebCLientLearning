package com.webclientdevtools.webclientdevtoolscustomer.services.implementation;

import com.webclientdevtools.webclientdevtoolscustomer.dto.AddressDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserAddressServiceImplementationTest {

    @Mock
    private WebClient webClient;
    @InjectMocks
    private UserAddressServiceImplementation service;
    AddressDto address = new AddressDto("01001-010","Rua teste", "lado par", (short) 12, "Bairro", "Municipio", "Estado");
    UUID id = UUID.randomUUID();

    @BeforeEach
    public void setUp() {
        when(webClient.post())
                .thenReturn(mock(WebClient.RequestBodyUriSpec.class));
        when(webClient.post().uri("/address/with-id"))
                .thenReturn(mock(WebClient.RequestBodySpec.class));
        when(webClient.post().uri("/address/with-id").bodyValue(address))
                .thenReturn(mock(WebClient.RequestHeadersSpec.class));
        when(webClient.post().uri("/address/with-id").bodyValue(address).retrieve())
                .thenReturn(mock(WebClient.ResponseSpec.class));
        when(webClient.post().uri("/address/with-id").bodyValue(address).retrieve().bodyToMono(UUID.class))
                .thenReturn(Mono.just(id));
    }

    @Test
    @DisplayName("Busca dados de endereço e retorna ID")
    void createOrUpdateAddressANdReturnId() throws InterruptedException {
      /* when(webClient.post().uri("http://localhost:8090/address/with-id")
                .body(BodyInserters.fromValue(address))
                .retrieve().bodyToMono(UUID.class)).thenReturn(Mono.just(id));*/

        UUID meuId = service.createOrUpdateAddressANdReturnId(address);

        assertEquals(id, meuId);

    }
}