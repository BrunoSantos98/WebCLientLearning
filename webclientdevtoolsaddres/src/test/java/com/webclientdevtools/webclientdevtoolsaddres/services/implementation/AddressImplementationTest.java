package com.webclientdevtools.webclientdevtoolsaddres.services.implementation;

import com.webclientdevtools.webclientdevtoolsaddres.dto.AddressDto;
import com.webclientdevtools.webclientdevtoolsaddres.exception.AddressNotFoundException;
import com.webclientdevtools.webclientdevtoolsaddres.models.AddressModel;
import com.webclientdevtools.webclientdevtoolsaddres.repository.AddressRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AddressImplementationTest {

    @Mock
    AddressRepository repository;
    @InjectMocks
    AddressImplementation service;

    AddressDto addressDto = new AddressDto("01001-010","Rua teste", "lado par", (short) 12, "Bairro", "Municipio", "Estado");
    AddressModel addressModel = new AddressModel(UUID.randomUUID(), "01001-010","Rua teste", "lado par", (short) 12, "Bairro", "Municipio", "Estado");

    @Test
    @DisplayName("Cria um novo endereço e retorna seu DTO")
    void ShouldBecreateAddress() {
        given(repository.existsByCepAndLogradouroAndNumero(addressDto.cep(), addressDto.logradouro(), addressDto.numero())).willReturn(false);
        given(repository.save(any(AddressModel.class))).willReturn(addressModel);

        AddressDto address = service.createAddress(addressDto);

        verify(repository,times(1)).existsByCepAndLogradouroAndNumero(addressDto.cep(),addressDto.logradouro(), addressDto.numero());
        verify(repository,times(1)).save(any(AddressModel.class));
        assertEquals(address.cep(), addressModel.getCep());
    }

    @Test
    @DisplayName("Apenas envia endedreço ja criado anteriormente")
    void ShouldBeSendcreatedAddress() {
        given(repository.existsByCepAndLogradouroAndNumero(addressDto.cep(), addressDto.logradouro(), addressDto.numero())).willReturn(true);

        service.createAddress(addressDto);

        verify(repository,times(1)).existsByCepAndLogradouroAndNumero(addressDto.cep(),addressDto.logradouro(), addressDto.numero());
        verify(repository,times(0)).save(addressModel);
    }

    @Test
    @DisplayName("Cria um novo endereço e retorna seu ID")
    void ShouldBecreateAddressAndReturnId() {
        given(repository.existsByCepAndLogradouroAndNumero(addressDto.cep(), addressDto.logradouro(), addressDto.numero())).willReturn(false);
        given(repository.save(any(AddressModel.class))).willReturn(addressModel);

        UUID id = service.createOrUpdateAddressWithId(addressDto);

        verify(repository,times(1)).existsByCepAndLogradouroAndNumero(addressDto.cep(),addressDto.logradouro(), addressDto.numero());
        verify(repository,times(1)).save(any(AddressModel.class));
        assertEquals(id, addressModel.getId());
    }

    @Test
    @DisplayName("Apenas envia endedreço ja criado anteriormente e retorna seu ID")
    void ShouldBeSendcreatedAddressAndReturnId() {
        given(repository.existsByCepAndLogradouroAndNumero(addressDto.cep(), addressDto.logradouro(), addressDto.numero())).willReturn(true);
        given(repository.findByCepAndLogradouroAndNumero(addressDto.cep(), addressDto.logradouro(), addressDto.numero())).willReturn(addressModel);

        UUID id = service.createOrUpdateAddressWithId(addressDto);

        verify(repository,times(1)).existsByCepAndLogradouroAndNumero(addressDto.cep(),addressDto.logradouro(), addressDto.numero());
        verify(repository,times(1)).findByCepAndLogradouroAndNumero(addressDto.cep(), addressDto.logradouro(), addressDto.numero());
        assertEquals(id, addressModel.getId());
    }

    @Test
    @DisplayName("Verifica se o endereço existe e retorna true")
    void ShouldBe_is_existAddressAndReturnTrue() {
        given(repository.existsByCepAndLogradouroAndNumero(addressDto.cep(), addressDto.logradouro(), addressDto.numero())).willReturn(true);

        boolean response = service.is_existAddress(addressDto.cep(), addressDto.logradouro(), addressDto.numero());

        verify(repository,times(1)).existsByCepAndLogradouroAndNumero(addressDto.cep(),addressDto.logradouro(), addressDto.numero());
        assertEquals(true, response);
    }

    @Test
    @DisplayName("Verifica se o endereço existe e retorna false")
    void ShouldBe_is_existAddressAndReturnFalse() {
        given(repository.existsByCepAndLogradouroAndNumero(addressDto.cep(), addressDto.logradouro(), addressDto.numero())).willReturn(false);

        boolean response = service.is_existAddress(addressDto.cep(), addressDto.logradouro(), addressDto.numero());

        verify(repository,times(1)).existsByCepAndLogradouroAndNumero(addressDto.cep(),addressDto.logradouro(), addressDto.numero());
        assertEquals(false, response);
    }

    @Test
    @DisplayName("Retorna o endereço pelos dados (cep, logradouro, numero)")
    void shouldBegetAddress() {
        given(repository.existsByCepAndLogradouroAndNumero(addressDto.cep(), addressDto.logradouro(), addressDto.numero())).willReturn(true);
        given(repository.findByCepAndLogradouroAndNumero(addressDto.cep(), addressDto.logradouro(), addressDto.numero())).willReturn(addressModel);

        service.getAddress(addressDto.cep(), addressDto.logradouro(), addressDto.numero());

        verify(repository,times(1)).existsByCepAndLogradouroAndNumero(addressDto.cep(),addressDto.logradouro(), addressDto.numero());
        verify(repository,times(1)).findByCepAndLogradouroAndNumero(addressDto.cep(),addressDto.logradouro(), addressDto.numero());
    }

    @Test
    @DisplayName("Lança exceção por nao encontrar endereço na base de dados")
    void shouldBeThrowExcecptionByNotFindAddress() {
        given(repository.existsByCepAndLogradouroAndNumero(addressDto.cep(), addressDto.logradouro(), addressDto.numero())).willReturn(false);

        AddressNotFoundException e = assertThrows(AddressNotFoundException.class,
                () ->service.getAddress(addressDto.cep(), addressDto.logradouro(), addressDto.numero()));

        verify(repository,times(1)).existsByCepAndLogradouroAndNumero(addressDto.cep(),addressDto.logradouro(), addressDto.numero());
        assertEquals("Endereço não encontrado. Por favor verifique os dados e tente novamente", e.getMessage());
    }

    @Test
    @DisplayName("Retorna o ID de um endereço especifico")
    void shoudlBeGetAddressId() {
        given(repository.existsByCepAndLogradouroAndNumero(addressDto.cep(), addressDto.logradouro(), addressDto.numero())).willReturn(true);
        given(repository.findByCepAndLogradouroAndNumero(addressDto.cep(), addressDto.logradouro(), addressDto.numero())).willReturn(addressModel);

        service.getAddressId(addressDto.cep(), addressDto.logradouro(), addressDto.numero());

        verify(repository,times(1)).existsByCepAndLogradouroAndNumero(addressDto.cep(),addressDto.logradouro(), addressDto.numero());
        verify(repository,times(1)).findByCepAndLogradouroAndNumero(addressDto.cep(),addressDto.logradouro(), addressDto.numero());
    }

    @Test
    @DisplayName("Lança exceção por nao encontrar endereço na base de dados (ID)")
    void shoudlBeTryGetAddressIdAndThrowException() {
        given(repository.existsByCepAndLogradouroAndNumero(addressDto.cep(), addressDto.logradouro(), addressDto.numero())).willReturn(false);

        AddressNotFoundException e = assertThrows(AddressNotFoundException.class,
                () ->service.getAddressId(addressDto.cep(), addressDto.logradouro(), addressDto.numero()));

        verify(repository,times(1)).existsByCepAndLogradouroAndNumero(addressDto.cep(),addressDto.logradouro(), addressDto.numero());
        verify(repository,times(0)).findByCepAndLogradouroAndNumero(addressDto.cep(),addressDto.logradouro(), addressDto.numero());
        assertEquals("Endereço não encontrado. Por favor verifique os dados e tente novamente",e.getMessage());
    }

    @Test
    @DisplayName("Retorna todos os endereços")
    void shouldBeGetAllAddresses() {
        given(repository.findAll()).willReturn(List.of(addressModel));

        List<AddressDto> response = service.getAllAddresses();

        verify(repository,times(1)).findAll();
        assertEquals(1, response.size());
    }

    @Test
    @DisplayName("Deleta um endereço")
    void shoudlBeDeleteAddress() {
        given(repository.existsByCepAndLogradouroAndNumero(addressDto.cep(), addressDto.logradouro(), addressDto.numero())).willReturn(true);
        given(repository.findByCepAndLogradouroAndNumero(addressDto.cep(), addressDto.logradouro(), addressDto.numero())).willReturn(addressModel);

        service.deleteAddress(addressDto.cep(), addressDto.logradouro(), addressDto.numero());

        verify(repository,times(1)).existsByCepAndLogradouroAndNumero(addressDto.cep(), addressDto.logradouro(), addressDto.numero());
        verify(repository,times(1)).findByCepAndLogradouroAndNumero(addressDto.cep(), addressDto.logradouro(), addressDto.numero());
        verify(repository,times(1)).delete(any(AddressModel.class));
    }

    @Test
    @DisplayName("Lança uma exception ao tentar deletar um endereço")
    void shoudlBeTryDeleteAddressAndThrowException() {
        given(repository.existsByCepAndLogradouroAndNumero(addressDto.cep(), addressDto.logradouro(), addressDto.numero())).willReturn(false);

        AddressNotFoundException e = assertThrows(AddressNotFoundException.class,
                () -> service.deleteAddress(addressDto.cep(), addressDto.logradouro(), addressDto.numero()));

        verify(repository,times(1)).existsByCepAndLogradouroAndNumero(addressDto.cep(), addressDto.logradouro(), addressDto.numero());
        verify(repository,times(0)).findByCepAndLogradouroAndNumero(addressDto.cep(), addressDto.logradouro(), addressDto.numero());
        verify(repository,times(0)).delete(any(AddressModel.class));
        assertEquals("Endereço não encontrado. Por favor verifique os dados e tente novamente",e.getMessage());
    }
}