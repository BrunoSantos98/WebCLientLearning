package com.webclientdevtools.webclientdevtoolsaddres.services.implementation;

import com.webclientdevtools.webclientdevtoolsaddres.dto.AddressDto;
import com.webclientdevtools.webclientdevtoolsaddres.exception.AddressNotFoundException;
import com.webclientdevtools.webclientdevtoolsaddres.models.AddressModel;
import com.webclientdevtools.webclientdevtoolsaddres.repository.AddressRepository;
import com.webclientdevtools.webclientdevtoolsaddres.services.AddressService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AddressImplementation implements AddressService {

    private final AddressRepository repository;

    public AddressImplementation(AddressRepository repository) {
        this.repository = repository;
    }

    public AddressDto addressModelToAddressDto(AddressModel addressModel) {
        AddressDto addressDto = new AddressDto(addressModel.getCep(), addressModel.getLogradouro(),
                addressModel.getComplemento(), addressModel.getNumero(), addressModel.getBairro(),
                addressModel.getLocalidade(), addressModel.getUf());
        return addressDto;
    }

    @Transactional
    @Override
    public AddressDto createAddress(AddressDto address) {

        if(is_existAddress(address.cep(), address.logradouro(), address.numero())) {
            return address;
        } else{
            return addressModelToAddressDto(repository.save(new AddressModel(null, address.cep(), address.logradouro(),
                    address.complemento(), address.numero(), address.bairro(), address.localidade(), address.uf())));
        }
    }

    @Transactional
    @Override
    public UUID createOrUpdateAddressWithId(AddressDto address) {
        if(is_existAddress(address.cep(), address.logradouro(), address.numero())) {
            return repository.findByCepAndLogradouroAndNumero(address.cep(), address.logradouro(), address.numero()).getId();
        } else{
            AddressModel addressModel =repository.save(new AddressModel(null, address.cep(), address.logradouro(),
                    address.complemento(), address.numero(), address.bairro(), address.localidade(), address.uf()));
            return addressModel.getId();
        }
    }

    @Override
    public boolean is_existAddress(String cep, String logradouro, short numero) {
        return repository.existsByCepAndLogradouroAndNumero(cep, logradouro, numero);
    }

    @Override
    public AddressDto getAddress(String cep, String logradouro, short numero) {
        if(is_existAddress(cep, logradouro, numero)) {
            AddressModel addressModel = repository.findByCepAndLogradouroAndNumero(cep, logradouro, numero);
            return addressModelToAddressDto(addressModel);
        }else{
            throw new AddressNotFoundException("Endereço não encontrado. Por favor verifique os dados e tente novamente");
        }

    }

    @Override
    public UUID getAddressId(String cep, String logradouro, short numero) {
        if(is_existAddress(cep, logradouro, numero)) {
            AddressModel addressModel = repository.findByCepAndLogradouroAndNumero(cep, logradouro, numero);
            return addressModel.getId();
        }else{
            throw new AddressNotFoundException("Endereço não encontrado. Por favor verifique os dados e tente novamente");
        }
    }

    @Override
    public List<AddressDto> getAllAddresses() {
        List<AddressModel> addressModels = repository.findAll();
        return addressModels.stream().map(this::addressModelToAddressDto).toList();
    }

    @Transactional
    @Override
    public void deleteAddress(String cep, String logradouro, short numero){
        AddressModel address = new AddressModel();
        address.setCep(cep);
        address.setLogradouro(logradouro);
        address.setNumero(numero);
        if(is_existAddress(address.getCep(), address.getLogradouro(), address.getNumero())) {
            address = repository.findByCepAndLogradouroAndNumero(cep, logradouro, numero);
            repository.delete(address);
        }else {
            throw new AddressNotFoundException("Endereço não encontrado. Por favor verifique os dados e tente novamente");
        }
    }
}

