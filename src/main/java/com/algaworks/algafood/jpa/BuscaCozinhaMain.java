package com.algaworks.algafood.jpa;

import org.springframework.context.ApplicationContext;

import java.util.List;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;

import com.algaworks.algafood.AlgafoodApiApplication;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

public class BuscaCozinhaMain {
	
	public static void main(String[] args) {
		ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApiApplication.class)
										.web(WebApplicationType.NONE)
										.run(args);
		
		CozinhaRepository cozinhaRepository = applicationContext.getBean(CozinhaRepository.class);
		RestauranteRepository restauranteRepository = applicationContext.getBean(RestauranteRepository.class);
		
		Cozinha cozinha = cozinhaRepository.porId(1L);
		
		Restaurante restaurante = restauranteRepository.porId(2L);
		
		System.out.println(cozinha.getNome());
		System.out.println(restaurante.getNome());
		System.out.println(restaurante.getTaxaFrete());
			
								
	}

}
