package com.webclientdevtools.webclientdevtoolscustomer.services;

import com.webclientdevtools.webclientdevtoolscustomer.dto.AddressDto;

import java.util.UUID;

public interface AddressService {
    UUID createAddressANdReturnId(AddressDto address);
}
