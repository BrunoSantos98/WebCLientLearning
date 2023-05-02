package com.webclientdevtools.webclientdevtoolsaddres.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webclientdevtools.webclientdevtoolsaddres.dto.AddressDto;
import com.webclientdevtools.webclientdevtoolsaddres.services.AddressService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AddressController.class)
class AddressControllerTest {

    @MockBean
    private AddressService services;
    @InjectMocks
    private AddressController controller;
    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();

    AddressDto address = new AddressDto("01001-010","Rua teste", "lado par", (short) 12, "Bairro", "Municipio", "Estado");
    UUID id = UUID.randomUUID();
    String url = "/address";

    @Test
    @DisplayName("Cria endereço ou retorna endereço existente")
    void shouldBeCreateAddress() throws Exception {
        given(services.createAddress(address)).willReturn(address);

        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(address)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(mapper.writeValueAsString(address)))
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("Cria ID de endereço ou retorna ID de endereço existente")
    void shouldBeCreateAddressWithId() throws Exception{
        given(services.createOrUpdateAddressWithId(address)).willReturn(id);

        mockMvc.perform(post(url+"/with-id").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(address)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(mapper.writeValueAsString(id)))
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("Retorna lista de endereços existentes")
    void shouldBeGetAllAddress() throws Exception{
        given(services.getAllAddresses()).willReturn(List.of(address));

        mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(address)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(mapper.writeValueAsString(List.of(address))))
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("Retorna ID de endereço existente")
    void getAddressId() throws Exception{
        given(services.getAddressId(address.cep(), address.logradouro(), address.numero())).willReturn(id);

        mockMvc.perform(get(url+"/get-id").contentType(MediaType.APPLICATION_JSON)
                        .param("cep", address.cep())
                        .param("logradouro", address.logradouro())
                        .param("numero", String.valueOf(address.numero())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(mapper.writeValueAsString(id)))
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("Retorna Endereço pelas informações")
    void shouldBeetAddressByInformations() throws Exception{
        given(services.getAddress(address.cep(), address.logradouro(), address.numero())).willReturn(address);

        mockMvc.perform(get(url+"/find-address").contentType(MediaType.APPLICATION_JSON)
                        .param("cep", address.cep())
                        .param("logradouro", address.logradouro())
                        .param("numero", String.valueOf(address.numero())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(mapper.writeValueAsString(address)))
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("Deleta endereço")
    void deleteAddress() throws Exception{

        mockMvc.perform(delete(url).contentType(MediaType.APPLICATION_JSON)
                        .param("cep", address.cep())
                        .param("logradouro", address.logradouro())
                        .param("numero", String.valueOf(address.numero())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
                .andExpect(content().string("Address succes deleted"))
                .andDo(print())
                .andReturn();
    }
}