package com.webclientdevtools.webclientdevtoolscustomer.controller;

import com.webclientdevtools.webclientdevtoolscustomer.dto.UserAddressDto;
import com.webclientdevtools.webclientdevtoolscustomer.dto.UserDto;
import com.webclientdevtools.webclientdevtoolscustomer.services.UserServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    private final UserServices service;

    public UserController(UserServices service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserAddressDto user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createUser(user));
    }

    @GetMapping
    public ResponseEntity<UserDto> getUser(@RequestParam String info, @RequestParam String value) {
        return ResponseEntity.ok(service.getUser(info, value));
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(service.getAllUsers());
    }

    @PatchMapping
    public ResponseEntity<UserDto> updateUser(@RequestBody UserAddressDto user, @RequestParam String cpf) {
        return ResponseEntity.ok(service.updateUser(user, cpf));
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUser(@RequestParam String info, @RequestParam String value) {
        service.deleteUser(info, value);
        return ResponseEntity.ok("Usuario deletado com sucesso!\n\n");
    }

}
