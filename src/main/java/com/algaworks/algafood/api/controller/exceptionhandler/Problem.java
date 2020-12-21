package com.algaworks.algafood.api.controller.exceptionhandler;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL) //Anotação igona valor null dos atributos.
@Getter
@Builder //É um padrão de projeto para contruir objetos de uma forma mais fluente, um contrutor de problema
public class Problem {
	
	private Integer status;
	private String type;
	private String title;
	private String detail;
	private String userMessage; //Uma mensagem para usuario 
	private OffsetDateTime timestamp;
	private List<Object> objects;
	
	@Getter
	@Builder
	public static class Object {
		
	   private String name;
	   private String userMessage;
		
	}
	
	

	

}
