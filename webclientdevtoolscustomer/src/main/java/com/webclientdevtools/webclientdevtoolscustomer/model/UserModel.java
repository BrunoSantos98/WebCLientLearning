package com.webclientdevtools.webclientdevtoolscustomer.model;

import com.webclientdevtools.webclientdevtoolscustomer.dto.AddressDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name="TB_USER")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false, length = 100)
    private String name;
    @Column(nullable = false, length = 14)
    private String cpf;
    @Column(nullable = false, length = 100)
    private String email;
    @Column(nullable = false, length = 16)
    private String phone;
    private UUID addressId;

}
