package com.webclientdevtools.webclientdevtoolsaddres.services;

import com.webclientdevtools.webclientdevtoolsaddres.dto.AddressDto;
import com.webclientdevtools.webclientdevtoolsaddres.models.AddressModel;

import java.util.List;
import java.util.UUID;

public interface AddressService {

    AddressDto createAddress(AddressDto address);
    UUID createOrUpdateAddressWithId(AddressDto address);
    boolean is_existAddress(String cep, String logradouro, short numero);
    AddressDto getAddress(String cep, String logradouro, short numero);
    UUID getAddressId(String cep, String logradouro, short numero);
    List<AddressDto> getAllAddresses();
    void deleteAddress(String cep, String logradouro, short numero);
}
