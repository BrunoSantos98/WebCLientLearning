package com.webclientdevtools.webclientdevtoolsaddres.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "TB_ADDRESS")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false, length = 9)
    private String cep;
    @Column(nullable = false, length = 90)
    private String logradouro;
    @Column(length = 40)
    private String complemento;
    @Column(nullable = false, length = 7)
    private short numero;
    @Column(nullable = false, length = 30)
    private String bairro;
    @Column(nullable = false, length = 30)
    private String localidade;
    @Column(nullable = false, length = 2)
    private String uf;

}
