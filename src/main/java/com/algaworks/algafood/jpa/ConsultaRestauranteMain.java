package com.algaworks.algafood.jpa;

import org.springframework.context.ApplicationContext;

import java.util.List;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;

import com.algaworks.algafood.AlgafoodApiApplication;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

public class ConsultaRestauranteMain {
	
	public static void main(String[] args) {
		ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApiApplication.class)
										.web(WebApplicationType.NONE)
										.run(args);
		
		RestauranteRepository restauranteRepository = applicationContext.getBean(RestauranteRepository.class);

				
		List<Restaurante> todosRestaurante = restauranteRepository.listar();
		
		for(Restaurante restaurante : todosRestaurante) {
			System.out.printf("%s - %f - %s\n", restaurante.getNome(), 
							 restaurante.getTaxaFrete(), restaurante.getCozinha().getNome());
			
		}
		
								
	}

}
