package com.webclientdevtools.webclientdevtoolsaddres.controller;

import com.webclientdevtools.webclientdevtoolsaddres.dto.AddressDto;
import com.webclientdevtools.webclientdevtoolsaddres.services.AddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/address")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AddressController {

    private final AddressService service;

    public AddressController(AddressService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AddressDto> createAddress(@RequestBody AddressDto address) {
        return ResponseEntity.ok(service.createAddress(address));

    }

    @PostMapping("/with-id")
    public ResponseEntity<UUID> createAddressWithId(@RequestBody AddressDto address) {
        return ResponseEntity.ok(service.createOrUpdateAddressWithId(address));
    }

    @GetMapping
    public ResponseEntity<List<AddressDto>> getAllAddress() {
        return ResponseEntity.ok(service.getAllAddresses());
    }

    @GetMapping("/get-id")
    public ResponseEntity<UUID> getAddressId(@RequestParam String cep, @RequestParam String logradouro,
                                             @RequestParam short numero) {
        return ResponseEntity.ok(service.getAddressId(cep, logradouro,numero));
    }

    @GetMapping("/find-address")
    public ResponseEntity<AddressDto> getAddressByInformations(@RequestParam String cep,
                                                               @RequestParam String logradouro,
                                                               @RequestParam short numero) {
        return ResponseEntity.ok(service.getAddress(cep, logradouro, numero));
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAddress(@RequestParam String cep,
                                                @RequestParam String logradouro,
                                                @RequestParam short numero) {
        service.deleteAddress(cep, logradouro, numero);
        return ResponseEntity.ok("Address succes deleted");
    }
}
