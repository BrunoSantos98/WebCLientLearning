package com.webclientdevtools.webclientdevtoolsaddres.dto;

public record AddressDto(
        String cep,
        String logradouro,
        String complemento,
        short numero,
        String bairro,
        String localidade,
        String uf
) {
}
