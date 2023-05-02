package com.webclientdevtools.webclientdevtoolscustomer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webclientdevtools.webclientdevtoolscustomer.dto.AddressDto;
import com.webclientdevtools.webclientdevtoolscustomer.dto.UserAddressDto;
import com.webclientdevtools.webclientdevtoolscustomer.dto.UserDto;
import com.webclientdevtools.webclientdevtoolscustomer.services.UserServices;
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

@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    private UserServices services;
    @InjectMocks
    private UserController controller;
    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();

    private String url = "/user";

    AddressDto address = new AddressDto("01001-010","Rua teste", "lado par", (short) 12, "Bairro", "Municipio", "Estado");
    UserAddressDto userAddressDto = new UserAddressDto("Bruno", "123.456.789-00", "teste@teste.com", "(11)93269-5874)", address);
    UserDto userDto = new UserDto("Bruno", "123.456.789-00", "teste@teste.com", "(11)93269-5874)", UUID.randomUUID());

    @Test
    @DisplayName("Cria e retorna novo usuario")
    void shoudlBeCreateUser() throws Exception{
        given(services.createUser(userAddressDto)).willReturn(userDto);

        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userAddressDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string(mapper.writeValueAsString(userDto)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("Retorna um usuario atraves de um email")
    void shouldBetUserByEmail() throws Exception{
        given(services.getUser(userDto.email(),"email")).willReturn(userDto);

        mockMvc.perform(get(url).param("info", userDto.email()).param("value", "email"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(mapper.writeValueAsString(userDto)))
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("Retorna um usuario atraves de um cpf")
    void shouldBetUserByCpf() throws Exception{
        given(services.getUser(userDto.cpf(),"cpf")).willReturn(userDto);

        mockMvc.perform(get(url).param("info", userDto.cpf()).param("value", "cpf"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(mapper.writeValueAsString(userDto)))
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("Retorna um usuario atraves de um ID")
    void shouldBetUserById() throws Exception{
        String stringId = UUID.randomUUID().toString();
        given(services.getUser(stringId,"id")).willReturn(userDto);

        mockMvc.perform(get(url).param("info", stringId).param("value", "id"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(mapper.writeValueAsString(userDto)))
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("Retorna lista de todos os usuarios")
    void shouldBeGetAllUsers() throws Exception{
        given(services.getAllUsers()).willReturn(List.of(userDto));

        mockMvc.perform(get(url+"/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(mapper.writeValueAsString(List.of(userDto))))
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("Atualiza um usuario em especifico")
    void updateUser() throws Exception{
        UserAddressDto newuserAddressDto = new UserAddressDto("Carlos", "123.456.789-00", "teste@teste.com", "(11)93269-5874)", address);
        UserDto newUserDto = new UserDto("Carlos", "123.456.789-00", "teste@teste.com", "(11)93269-5874)", UUID.randomUUID());
        given(services.updateUser(newuserAddressDto, newUserDto.cpf())).willReturn(newUserDto);

        mockMvc.perform(patch(url)
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(newuserAddressDto))
                .param("cpf", newUserDto.cpf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(mapper.writeValueAsString(newUserDto)))
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("Deleta usuario por cpf")
    void shouldBeDeleteUserByCpf() throws Exception{

        mockMvc.perform(delete(url).contentType(MediaType.APPLICATION_JSON)
                .param("info",userDto.cpf()).param("value", "cpf"))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario deletado com sucesso!\n\n"))
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("Deleta usuario por email")
    void shouldBeDeleteUserByEmail() throws Exception{

        mockMvc.perform(delete(url).contentType(MediaType.APPLICATION_JSON)
                        .param("info",userDto.email()).param("value", "email"))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario deletado com sucesso!\n\n"))
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("Deleta usuario por ID")
    void shouldBeDeleteUserById() throws Exception{

        mockMvc.perform(delete(url).contentType(MediaType.APPLICATION_JSON)
                        .param("info",String.valueOf(UUID.randomUUID())).param("value", "id"))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario deletado com sucesso!\n\n"))
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
                .andDo(print())
                .andReturn();
    }
}