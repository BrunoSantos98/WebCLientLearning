package com.webclientdevtools.webclientdevtoolscustomer.services;

import com.webclientdevtools.webclientdevtoolscustomer.dto.AddressDto;
import com.webclientdevtools.webclientdevtoolscustomer.dto.UserAddressDto;
import com.webclientdevtools.webclientdevtoolscustomer.dto.UserDto;

import java.util.List;
import java.util.UUID;

public interface UserServices {

    UserDto createUser(UserAddressDto user);
    UUID createNewUserAddress(AddressDto address);
    boolean existsUserByEmail(String userEmail);
    boolean existsUserByCpf(String userCpf);
    UserDto getUser(String info, String value);
    List<UserDto> getAllUsers();
    UserDto updateUser(UserDto user, String cpf);
    void deleteUser(String userInformation, String information);
}
