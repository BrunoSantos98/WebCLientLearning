package com.webclientdevtools.webclientdevtoolscustomer.dto;

import java.util.UUID;

public record UserDto(
        String name,
        String cpf,
        String email,
        String phone,
        UUID addressId
) {}
