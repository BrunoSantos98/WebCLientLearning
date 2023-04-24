package com.webclientdevtools.webclientdevtoolsaddres.repository;

import com.webclientdevtools.webclientdevtoolsaddres.models.AddressModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<AddressModel, UUID> {
    AddressModel findByCepAndLogradouroAndNumero(String cep, String logradouro, short numero);
    boolean existsByCepAndLogradouroAndNumero(String cep, String logradouro, short numero);
}
