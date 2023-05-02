package com.webclientdevtools.webclientdevtoolscustomer.services.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webclientdevtools.webclientdevtoolscustomer.dto.AddressDto;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserAddressServiceImplementationTest {
    public static MockWebServer server;
    private static ObjectMapper mapper = new ObjectMapper();
    @InjectMocks
    private UserAddressServiceImplementation service;
    AddressDto address = new AddressDto("01001-010","Rua teste", "lado par", (short) 12, "Bairro", "Municipio", "SP");
    UUID id = UUID.randomUUID();

    @BeforeAll
    static void setUp() throws IOException {
        server = new MockWebServer();
        server.start(8090);
    }

    @AfterAll
    static void tearDown() throws IOException {
        server.shutdown();
    }

    @Test
    @DisplayName("Busca dados de endereço e retorna ID")
    void createOrUpdateAddressANdReturnId() throws Exception {

        //Configura a resposta esperada pelo server
        MockResponse response = new MockResponse().setResponseCode(HttpStatus.OK.value())
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(mapper.writeValueAsString(id));

        //configura o servidor para retornar a resposta esperada
        server.enqueue(response);


        //chama a função
        service.createOrUpdateAddressANdReturnId(address);

        RecordedRequest request = server.takeRequest();

        assertEquals( "POST", request.getMethod());
        assertEquals("/address/with-id", request.getPath());
    }
}