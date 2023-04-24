package com.webclientdevtools.webclientdevtoolscustomer.dto;

import java.util.UUID;

public record UserAddressDto (
        String name,
        String cpf,
        String email,
        String phone,
        AddressDto address
){
}
