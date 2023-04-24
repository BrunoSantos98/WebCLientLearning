package com.webclientdevtools.webclientdevtoolscustomer.services.implementation;

import com.webclientdevtools.webclientdevtoolscustomer.dto.AddressDto;
import com.webclientdevtools.webclientdevtoolscustomer.dto.UserAddressDto;
import com.webclientdevtools.webclientdevtoolscustomer.dto.UserDto;
import com.webclientdevtools.webclientdevtoolscustomer.exception.UserExistsConflictException;
import com.webclientdevtools.webclientdevtoolscustomer.exception.UserNotFoundException;
import com.webclientdevtools.webclientdevtoolscustomer.model.UserModel;
import com.webclientdevtools.webclientdevtoolscustomer.repository.UserRepository;
import com.webclientdevtools.webclientdevtoolscustomer.services.UserServices;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImplementation implements UserServices {

    private final UserRepository repository;
    private final WebClient webClient;

    public UserServiceImplementation(UserRepository repository, WebClient.Builder webClientBuilder) {
        this.repository = repository;
        this.webClient = webClientBuilder.baseUrl("http://localhost:8090").build();
    }

    private UserDto userModelToUserDto(UserModel userModel){
        UserDto userDto = new UserDto(
                userModel.getName(),
                userModel.getCpf(),
                userModel.getEmail(),
                userModel.getPhone(),
                userModel.getAddressId()
        );
        return userDto;
    }

    public UserDto getUserById(UUID userId) {
        Optional<UserModel> user = repository.findById(userId);
        if(user.isPresent())
            return userModelToUserDto(user.get());
        else
            throw new UserNotFoundException("Usuario nao lozalizado pelo ID informado");
    }

    public UserDto getUserByEmail(String userEmail) {
        if(existsUserByEmail(userEmail)){
            return userModelToUserDto(repository.findByEmail(userEmail));
        }else{
            throw new UserNotFoundException("Usuario nao localizado pelo email informado");
        }
    }

    public UserDto getUserByCpf(String userCpf) {
        if(existsUserByCpf(userCpf)){
            return userModelToUserDto(repository.findByCpf(userCpf));
        }else{
            throw new UserNotFoundException("Usuario nao localizado pelo CPF informado");
        }
    }

    @Transactional
    @Override
    public UserDto createUser(UserAddressDto user) {
        System.out.println(user);
        if(existsUserByEmail(user.email())){
            throw new UserExistsConflictException("Email ja cadastrado na base de dados para outro usuario");
        }else if(existsUserByCpf(user.cpf())){
            throw new UserExistsConflictException("CPF ja cadastrado na base de dados para outro usuario");
        }else{
            UUID id = createNewUserAddress(user.address());
            UserModel userModel = new UserModel(null, user.name(),user.cpf(),user.email(),user.phone(),id);
            System.out.println(user.toString());
            System.out.println(userModel.toString());
            return userModelToUserDto(repository.save(userModel));
        }
    }

    @Override
    public UUID createNewUserAddress(AddressDto address) {
            webClient.post()
                    .uri("/address")
                    .body(BodyInserters.fromValue(address))
                    .retrieve()
                    .bodyToMono(AddressDto.class);

           Mono<UUID> response =  webClient.get()
                    .uri(uriBuilder ->
                        uriBuilder.path("/address/get-id")
                                .queryParam("cep",address.cep())
                                .queryParam("logradouro",address.logradouro())
                                .queryParam("numero",address.numero())
                                .build())
                    .retrieve()
                    .bodyToMono(UUID.class);

           return response.block();
    }

    @Override
    public boolean existsUserByEmail(String userEmail) {
        return repository.existsByEmail(userEmail);
    }

    @Override
    public boolean existsUserByCpf(String userCpf) {
        return repository.existsByCpf(userCpf);
    }

    @Override
    public UserDto getUser(String info, String value) {
        if(value.equals("email")){
            return getUserByEmail(info);
        }else if(value.equals("cpf")){
            return getUserByCpf(info);
        }else{
            return getUserById(UUID.fromString(info));
        }

    }

    @Override
    public List<UserDto> getAllUsers() {
        return repository.findAll().stream().map(this::userModelToUserDto).toList();
    }

    @Transactional
    @Override
    public UserDto updateUser(UserDto user, String cpf) {
        if(existsUserByCpf(cpf)){
            UserModel userModel = repository.findByCpf(cpf);
            userModel.setName(user.name());
            userModel.setEmail(user.email());
            userModel.setPhone(user.phone());
            return userModelToUserDto(repository.save(userModel));
        }else{
            throw new UserNotFoundException("Usuario nao localizado pelo CPF informado");
        }

    }

    @Transactional
    @Override
    public void deleteUser(String userInformation, String information) {
        if(information.equals("email")){
            if(existsUserByEmail(userInformation)){
                repository.deleteByEmail(userInformation);
            }else{
                throw new UserNotFoundException("Usuario nao localizado pelo email informado");
            }
        }else if(information.equals("cpf")){
            if(existsUserByCpf(userInformation)){
                repository.deleteByCpf(userInformation);
            }else{
                throw new UserNotFoundException("Usuario nao localizado pelo CPF informado");
            }
        }else{
            Optional<UserModel> user = repository.findById(UUID.fromString(userInformation));
            if(user.isPresent()){
                repository.delete(user.get());
            }else{
                throw new UserNotFoundException("Usuario nao localizado pelo ID informado");
            }
        }
    }
}