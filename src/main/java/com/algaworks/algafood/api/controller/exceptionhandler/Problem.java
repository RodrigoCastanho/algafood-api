package com.algaworks.algafood.api.controller.exceptionhandler;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@ApiModel("Problema")
@JsonInclude(Include.NON_NULL) //Anotação igona valor null dos atributos.
@Getter
@Builder //É um padrão de projeto para contruir objetos de uma forma mais fluente, um contrutor de problema
public class Problem {
	
	@ApiModelProperty(example = "400")
	private Integer status;
	
	@ApiModelProperty(example = "2020-12-22T13:24:05.70844Z")
	private OffsetDateTime timestamp;
	
	@ApiModelProperty(example = "https://localhost:8080/dados-invalidos")
	private String type;
	
	@ApiModelProperty(example = "Dados inválidos")
	private String title;
	
	@ApiModelProperty(example = "Um ou mais campos estão inválidos")
	private String detail;
	
	@ApiModelProperty(example = "Um ou mais campos estão inválidos")
	private String userMessage; //Uma mensagem para usuario 
	
	@ApiModelProperty("Lista de objetos ou campos que geraram o erro(optional)")
	private List<Object> objects;
	
	@ApiModel("ObjetoProblema")
	@Getter
	@Builder
	public static class Object {
	   
	   @ApiModelProperty(example = "preço")
	   private String name;
	   @ApiModelProperty(example = "O preço é obrigatório")
	   private String userMessage;
		
	}
	
	

	

}
