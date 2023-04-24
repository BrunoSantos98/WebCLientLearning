package com.webclientdevtools.webclientdevtoolscustomer.repository;

import com.webclientdevtools.webclientdevtoolscustomer.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, UUID> {

    boolean existsByCpf(String cpf);
    UserModel findByCpf(String cpf);
    boolean existsByEmail(String email);
    UserModel findByEmail(String email);
    void deleteByCpf(String cpf);
    void deleteByEmail(String email);
}
