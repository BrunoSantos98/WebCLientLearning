package com.webclientdevtools.webclientdevtoolscustomer.services;

import com.webclientdevtools.webclientdevtoolscustomer.dto.AddressDto;
import com.webclientdevtools.webclientdevtoolscustomer.dto.UserAddressDto;
import com.webclientdevtools.webclientdevtoolscustomer.dto.UserDto;
import com.webclientdevtools.webclientdevtoolscustomer.exception.UserExistsConflictException;
import com.webclientdevtools.webclientdevtoolscustomer.model.UserModel;
import com.webclientdevtools.webclientdevtoolscustomer.repository.UserRepository;
import com.webclientdevtools.webclientdevtoolscustomer.services.implementation.UserServiceImplementation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServicesTest {

    @Mock
    private  UserRepository repository;
    @InjectMocks
    private  UserServiceImplementation userService;
    @Mock
    private  AddressService addressService;

    AddressDto address = new AddressDto("01001-010","Rua teste", "lado par", (short) 12, "Bairro", "Municipio", "Estado");
    UserAddressDto userAddressDto = new UserAddressDto("bruno" , "123.456.789-00", "teste@teste.com", "(91)98050-6080",address );
    UserDto userDto = new UserDto("bruno" , "123.456.789-00", "teste@teste.com", "(91)98050-6080", UUID.randomUUID());
    UserModel userModel = new UserModel(UUID.randomUUID(),"bruno" , "123.456.789-00", "teste@teste.com", "(91)98050-6080", UUID.randomUUID());

    @Test
    @DisplayName("Cria um usuario")
    void shouldBeCreateUser(){
        given(repository.existsByEmail(userAddressDto.email())).willReturn(false);
        given(repository.existsByCpf(userAddressDto.cpf())).willReturn(false);
        given(addressService.createOrUpdateAddressANdReturnId(address)).willReturn(UUID.randomUUID());
        given(repository.save(any(UserModel.class))).willReturn(userModel);

        UserDto user = userService.createUser(userAddressDto);

        verify(repository, times(1)).existsByEmail(userAddressDto.email());
        verify(repository, times(1)).existsByCpf(userAddressDto.cpf());
        verify(addressService, times(1)).createOrUpdateAddressANdReturnId(address);
        verify(repository, times(1)).save(any(UserModel.class));
    }

    @Test
    @DisplayName("Não cria um usuario e lança exceção de email ja existente")
    void shouldBeNotCreateUserWithEmailAlreadyExists(){
        given(repository.existsByEmail(userAddressDto.email())).willReturn(true);

        UserExistsConflictException e = assertThrows(UserExistsConflictException.class,
                () -> userService.createUser(userAddressDto));

        verify(repository, times(1)).existsByEmail(userAddressDto.email());
        assertEquals("Email ja cadastrado na base de dados para outro usuario",e.getMessage());
    }

    @Test
    @DisplayName("Não cria um usuario e lança exceção de CPF ja existente")
    void shouldBeNotCreateUserWithCpfAlreadyExists(){
        given(repository.existsByEmail(userAddressDto.email())).willReturn(false);
        given(repository.existsByCpf(userAddressDto.cpf())).willReturn(true);

        UserExistsConflictException e = assertThrows(UserExistsConflictException.class,
                () -> userService.createUser(userAddressDto));

        verify(repository, times(1)).existsByEmail(userAddressDto.email());
        verify(repository, times(1)).existsByCpf(userAddressDto.cpf());
        assertEquals("CPF ja cadastrado na base de dados para outro usuario",e.getMessage());

    }


    @Test
    void createOrUpdateUserAddress() {
    }

    @Test
    void existsUserByEmail() {
    }

    @Test
    void existsUserByCpf() {
    }

    @Test
    void getUser() {
    }

    @Test
    void getAllUsers() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }
}