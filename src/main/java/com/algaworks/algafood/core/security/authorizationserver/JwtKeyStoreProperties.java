package com.algaworks.algafood.core.security.authorizationserver;

//import javax.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Validated
@Component
@ConfigurationProperties("algafood.jwt.keystore")
public class JwtKeyStoreProperties {

	//@NotBNull
    private Resource jksLocation;
    
    //@NotBlank
    private String password;
    
    //@NotBlank
    private String keypairAlias;

}
