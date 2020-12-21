package com.algaworks.algafood.api.model.input;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CozinhaInput {
	
	@NotBlank
	private String nome;
	
    
}
