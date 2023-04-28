package com.webclientdevtools.webclientdevtoolscustomer.services.implementation;

import com.webclientdevtools.webclientdevtoolscustomer.dto.AddressDto;
import com.webclientdevtools.webclientdevtoolscustomer.dto.UserAddressDto;
import com.webclientdevtools.webclientdevtoolscustomer.dto.UserDto;
import com.webclientdevtools.webclientdevtoolscustomer.exception.UserExistsConflictException;
import com.webclientdevtools.webclientdevtoolscustomer.exception.UserNotFoundException;
import com.webclientdevtools.webclientdevtoolscustomer.model.UserModel;
import com.webclientdevtools.webclientdevtoolscustomer.repository.UserRepository;
import com.webclientdevtools.webclientdevtoolscustomer.services.AddressService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceImplementationTest {

    @Mock
    private UserRepository repository;
    @Mock
    private AddressService addressService;
    @InjectMocks
    private UserServiceImplementation userService;

    AddressDto address = new AddressDto("01001-010","Rua teste", "lado par", (short) 12, "Bairro", "Municipio", "Estado");
    UserAddressDto userAddressDto = new UserAddressDto("Bruno", "123.456.789-00", "teste@teste.com", "(11)93269-5874)", address);
    UserDto userDto = new UserDto("Bruno", "123.456.789-00", "teste@teste.com", "(11)93269-5874)", UUID.randomUUID());
    UserModel userModel = new UserModel(UUID.randomUUID(), "Bruno", "123.456.789-00", "teste@teste.com", "(11)93269-5874)", UUID.randomUUID());


    @Test
    @DisplayName("Cria um novo user")
    void ShouldBeCreateUser() {
        given(repository.existsByEmail(userDto.email())).willReturn(false);
        given(repository.existsByCpf(userDto.cpf())).willReturn(false);
        given(addressService.createOrUpdateAddressANdReturnId(address)).willReturn(UUID.randomUUID());
        given(repository.save(any(UserModel.class))).willReturn(userModel);

        userService.createUser(userAddressDto);

        verify(repository,times(1)).existsByEmail(userDto.email());
        verify(repository,times(1)).existsByCpf(userDto.cpf());
        verify(addressService,times(1)).createOrUpdateAddressANdReturnId(address);
        verify(repository,times(1)).save(any(UserModel.class));
    }

    @Test
    @DisplayName("Email lança exception ao criar um novo user")
    void ShouldBeTryCreateUserButEmailExists() {
        given(repository.existsByEmail(userDto.email())).willReturn(true);


        UserExistsConflictException e = assertThrows(UserExistsConflictException.class,
                () ->userService.createUser(userAddressDto));

        verify(repository,times(1)).existsByEmail(userDto.email());
        verify(repository,times(0)).existsByCpf(userDto.cpf());
        verify(addressService,times(0)).createOrUpdateAddressANdReturnId(address);
        verify(repository,times(0)).save(any(UserModel.class));
        assertEquals("Email ja cadastrado na base de dados para outro usuario", e.getMessage());
    }

    @Test
    @DisplayName("CPF lança exception ao criar um novo user")
    void ShouldBeTryCreateUserButCpfExists() {
        given(repository.existsByEmail(userDto.email())).willReturn(false);
        given(repository.existsByCpf(userDto.cpf())).willReturn(true);


        UserExistsConflictException e = assertThrows(UserExistsConflictException.class,
                () ->userService.createUser(userAddressDto));

        verify(repository,times(1)).existsByEmail(userDto.email());
        verify(repository,times(1)).existsByCpf(userDto.cpf());
        verify(addressService,times(0)).createOrUpdateAddressANdReturnId(address);
        verify(repository,times(0)).save(any(UserModel.class));
        assertEquals("CPF ja cadastrado na base de dados para outro usuario", e.getMessage());
    }

    @Test
    @DisplayName("Envia o endereço para microservices e retorna ID")
    void shouldBeCreateOrUpdateUserAddress() {
        given(addressService.createOrUpdateAddressANdReturnId(address)).willReturn(UUID.randomUUID());

        userService.createOrUpdateUserAddress(address);

        verify(addressService,times(1)).createOrUpdateAddressANdReturnId(address);
    }

    @Test
    @DisplayName("Testa a existencia do email e retorna true")
    void shouldBeExistsUserByEmail() {
        given(repository.existsByEmail(userDto.email())).willReturn(true);

        boolean result = userService.existsUserByEmail(userDto.email());

        verify(repository,times(1)).existsByEmail(userDto.email());
        assertTrue(result);
    }

    @Test
    @DisplayName("Testa a existencia do email e retorna false")
    void shouldBeNotExistsUserByEmail() {
        given(repository.existsByEmail(userDto.email())).willReturn(false);

        boolean result = userService.existsUserByEmail(userDto.email());

        verify(repository,times(1)).existsByEmail(userDto.email());
        assertFalse(result);
    }

    @Test
    @DisplayName("Testa a existencia do CPF e retorna true")
    void shouldBeExistsUserByCpf() {
        given(repository.existsByCpf(userDto.cpf())).willReturn(true);

        boolean result = userService.existsUserByCpf(userDto.cpf());

        verify(repository,times(1)).existsByCpf(userDto.cpf());
        assertTrue(result);
    }

    @Test
    @DisplayName("Testa a existencia do CPF e retorna false")
    void shouldBeNotExistsUserByCpf() {
        given(repository.existsByCpf(userDto.cpf())).willReturn(false);

        boolean result = userService.existsUserByCpf(userDto.cpf());

        verify(repository,times(1)).existsByCpf(userDto.cpf());
        assertFalse(result);
    }

    @Test
    @DisplayName("Retorna um usuario pelo email")
    void ShouldBeGetUserByEmail() {
        given(repository.existsByEmail(userDto.email())).willReturn(true);
        given(repository.findByEmail(userDto.email())).willReturn(userModel);

        userService.getUser(userDto.email(),"email");

        verify(repository,times(1)).existsByEmail(userDto.email());
        verify(repository,times(1)).findByEmail(userDto.email());

    }

    @Test
    @DisplayName("Retorna Exception ao tentar retornar um usuario pelo email")
    void ShouldBeTryGetUserByEmail() {
        given(repository.existsByEmail(userDto.email())).willReturn(false);

        UserNotFoundException e = assertThrows(UserNotFoundException.class,
                () -> userService.getUser(userDto.email(),"email"));

        verify(repository,times(1)).existsByEmail(userDto.email());
        verify(repository,times(0)).findByEmail(userDto.email());
        assertEquals("Usuario nao localizado pelo email informado", e.getMessage());
    }

    @Test
    @DisplayName("Retorna um usuario pelo CPF")
    void ShouldBeGetUserByCpf() {
        given(repository.existsByCpf(userDto.cpf())).willReturn(true);
        given(repository.findByCpf(userDto.cpf())).willReturn(userModel);

        userService.getUser(userDto.cpf(),"cpf");

        verify(repository,times(1)).existsByCpf(userDto.cpf());
        verify(repository,times(1)).findByCpf(userDto.cpf());

    }

    @Test
    @DisplayName("Retorna Exception ao tentar retornar um usuario pelo CPF")
    void ShouldBeTryGetUserByCpf() {
        given(repository.existsByCpf(userDto.cpf())).willReturn(false);

        UserNotFoundException e = assertThrows(UserNotFoundException.class,
                () -> userService.getUser(userDto.cpf(),"cpf"));

        verify(repository,times(1)).existsByCpf(userDto.cpf());
        verify(repository,times(0)).findByCpf(userDto.cpf());
        assertEquals("Usuario nao localizado pelo CPF informado", e.getMessage());
    }

    @Test
    @DisplayName("Retorna um usuario pelo ID")
    void ShouldBeGetUserById() {
        given(repository.findById(userModel.getId())).willReturn(Optional.of(userModel));

        userService.getUser(userModel.getId().toString(),"ID");

        verify(repository,times(1)).findById(userModel.getId());
    }

    @Test
    @DisplayName("Retorna Exception ao tentar retornar um usuario pelo ID")
    void ShouldBeTryGetUserById() {
        given(repository.findById(userModel.getId())).willReturn(Optional.empty());

        UserNotFoundException e = assertThrows(UserNotFoundException.class,
                () -> userService.getUser(userModel.getId().toString(),"ID"));

        verify(repository,times(1)).findById(userModel.getId());
        assertTrue(e.getMessage() == "Usuario nao lozalizado pelo ID informado");
    }

    @Test
    @DisplayName("retorna lista de todos  os usuarios")
    void shouldBeGetAllUsers() {
        given(repository.findAll()).willReturn(Collections.singletonList(userModel));

        userService.getAllUsers();

        verify(repository,times(1)).findAll();
    }

    @Test
    @DisplayName("Atualiza um usuario")
    void shouldBeUpdateUser() {
        given(repository.existsByCpf(userAddressDto.cpf())).willReturn(true);
        given(repository.findByCpf(userAddressDto.cpf())).willReturn(userModel);
        given(repository.existsByEmail(userAddressDto.email())).willReturn(true);
        given(repository.findByEmail(userAddressDto.email())).willReturn(userModel);
        given(repository.save(any(UserModel.class))).willReturn(userModel);

        userService.updateUser(userAddressDto, userAddressDto.cpf());

        verify(repository,times(2)).existsByCpf(userAddressDto.cpf());
        verify(repository,times(2)).findByCpf(userAddressDto.cpf());
        verify(repository,times(1)).existsByEmail(userAddressDto.email());
        verify(repository,times(1)).findByEmail(userAddressDto.email());
        verify(repository,times(1)).save(any(UserModel.class));
    }

    @Test
    @DisplayName("tenta atualizar um usuario mas email ja  existe")
    void shouldBeTryUpdateUserButEmailExists() {
        UserModel userModelForException = new UserModel(
                UUID.randomUUID(), "Bruno", "987.654.321-00", "teste@teste.com", "(11)93269-5874)", UUID.randomUUID());
        given(repository.existsByCpf(userAddressDto.cpf())).willReturn(true);
        given(repository.existsByEmail(userAddressDto.email())).willReturn(true);
        given(repository.findByEmail(userAddressDto.email())).willReturn(userModelForException);

        UserExistsConflictException e = assertThrows(UserExistsConflictException.class,
                () ->userService.updateUser(userAddressDto, userAddressDto.cpf()));

        verify(repository,times(1)).existsByEmail(userAddressDto.email());
        verify(repository,times(1)).findByEmail(userAddressDto.email());
        verify(repository,times(0)).save(any(UserModel.class));
        assertEquals("Email ja cadastrado na base de dados para outro usuario", e.getMessage());
    }

    @Test
    @DisplayName("tenta atualizar um usuario mas cpf ja  existe")
    void shouldBeTryUpdateUserButCpfExists() {
        UserModel userModelForException = new UserModel(
                UUID.randomUUID(), "carlos", userAddressDto.cpf(), "teste@teste.com", "(11)93269-5874)", UUID.randomUUID());
        given(repository.existsByCpf(userAddressDto.cpf())).willReturn(true);
        given(repository.findByCpf(userAddressDto.cpf())).willReturn(userModelForException);
        given(repository.existsByEmail(userAddressDto.email())).willReturn(true);
        given(repository.findByEmail(userAddressDto.email())).willReturn(userModel);

        UserExistsConflictException e = assertThrows(UserExistsConflictException.class,
                () ->userService.updateUser(userAddressDto, userAddressDto.cpf()));

        verify(repository,times(2)).existsByCpf(userAddressDto.cpf());
        verify(repository,times(1)).findByCpf(userAddressDto.cpf());
        verify(repository,times(1)).existsByEmail(userAddressDto.email());
        verify(repository,times(1)).findByEmail(userAddressDto.email());
        verify(repository,times(0)).save(any(UserModel.class));
        assertEquals("CPF ja cadastrado na base de dados para outro usuario", e.getMessage());
    }

    @Test
    @DisplayName("Deleta usuario pelo email")
    void shouldBeDeleteUserByEmail(){
        given(repository.existsByEmail(userDto.email())).willReturn(true);

        userService.deleteUser(userDto.email(), "email");

        verify(repository,times(1)).existsByEmail(userDto.email());
        verify(repository,times(1)).deleteByEmail(userDto.email());
    }

    @Test
    @DisplayName("Deleta usuario pelo cpf")
    void shouldBeDeleteUserByCpf(){
        given(repository.existsByCpf(userDto.cpf())).willReturn(true);

        userService.deleteUser(userDto.cpf(), "cpf");

        verify(repository,times(1)).existsByCpf(userDto.cpf());
        verify(repository,times(1)).deleteByCpf(userDto.cpf());
    }

    @Test
    @DisplayName("Deleta usuario pelo ID")
    void shouldBeDeleteUserById(){
        given(repository.findById(userModel.getId())).willReturn(Optional.of(userModel));

        userService.deleteUser(userModel.getId().toString(), "id");

        verify(repository,times(1)).findById(userModel.getId());
        verify(repository,times(1)).delete(any(UserModel.class));
    }
}