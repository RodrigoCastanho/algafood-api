package com.algaworks.algafood.api.model;

import java.util.List;

import org.springframework.hateoas.Links;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel("CidadesModel")
@Data
public class CidadesModelOpenApi {
	
	private CidadesEmbeddeModelOpenApi _embedded;
	private Links _links;
	
	@ApiModel("CidadesEmbeddedModel")
	@Data
	public class CidadesEmbeddeModelOpenApi {
		
		private List<CidadeModel> cidades;
		
	}

}
