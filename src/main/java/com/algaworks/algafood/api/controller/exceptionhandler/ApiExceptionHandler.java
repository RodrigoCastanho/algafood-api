package com.algaworks.algafood.api.controller.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler{
	
	
	private static final String MSG_ERRO_GENERICA_USUARIO_FINAL = "Ocorreu um erro interno inesperado no sistema. "
	               + "Tente novamente e se o problema persistir, entre em contato "
	               + "com o administrador do sistema.";
	
	@Autowired
	private MessageSource messageSource;
	
	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
	
	    return ResponseEntity.status(status).headers(headers).build();
	}
	
	@Override
	protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		
		return handleValidationInternal(ex, headers, status, request, ex.getBindingResult());
	}


	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		Throwable rootCause = ExceptionUtils.getRootCause(e);
		
		if(rootCause instanceof InvalidFormatException) {
			return handleInvalidFormat((InvalidFormatException) rootCause, headers, status, request);		
		} else if (rootCause instanceof PropertyBindingException) {
	        return handlePropertyBinding((PropertyBindingException) rootCause, headers, status, request); 
	    }
		
		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		String detail = "O corpo da requisição está inválidade. Verifique  erro de sintaxe";
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				                     .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
									.build();	
		
		return handleExceptionInternal(e, problem, new HttpHeaders(), status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException e, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		 if (e instanceof MethodArgumentTypeMismatchException) {
		        return handleMethodArgumentTypeMismatch(
		                (MethodArgumentTypeMismatchException) e, headers, status, request);
		    }
			
		return super.handleTypeMismatch(e, headers, status, request);
	}
	
	private ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e, HttpHeaders headers,
	        HttpStatus status, WebRequest request) {

	    ProblemType problemType = ProblemType.PARAMETRO_INVALIDO;

	    String detail = String.format("O parâmetro de URL '%s' recebeu o valor '%s', "
	            + "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
	            e.getName(), e.getValue(), e.getRequiredType().getSimpleName());

	    Problem problem = createProblemBuilder(status, problemType, detail)
	    		                    .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
	    							.build();

	    return handleExceptionInternal(e, problem, headers, status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException e, 
	        HttpHeaders headers, HttpStatus status, WebRequest request) {
	    
	    ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
	    String detail = String.format("O recurso %s, que você tentou acessar, é inexistente.", 
	            e.getRequestURL());
	    
	    Problem problem = createProblemBuilder(status, problemType, detail)
	    		            .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
	    					.build();
	    
	    return handleExceptionInternal(e, problem, headers, status, request);
	}
	
	
	private ResponseEntity<Object> handleInvalidFormat(InvalidFormatException e, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		String path = joinPath(e.getPath());
		
		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		String detail = String.format("A propriedade '%s' recebeu o valor '%s', "
				                    + "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.", path, e.getValue(),
				                    e.getTargetType().getSimpleName());
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
				.build();
		
		
		return handleExceptionInternal(e, problem, headers, status, request);
	}
	
	private ResponseEntity<Object> handlePropertyBinding(PropertyBindingException ex,
	        HttpHeaders headers, HttpStatus status, WebRequest request) {

	    String path = joinPath(ex.getPath());
	    
	    ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
	    String detail = String.format("A propriedade '%s' não existe. "
	            + "Corrija ou remova essa propriedade e tente novamente.", path);

	    Problem problem = createProblemBuilder(status, problemType, detail)
	    			.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
	    		          .build();
	      
	    return handleExceptionInternal(ex, problem, headers, status, request);
	} 
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
	    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;		
	    ProblemType problemType = ProblemType.ERRO_DE_SISTEMA;
	    String detail = MSG_ERRO_GENERICA_USUARIO_FINAL;

	    // Importante colocar o printStackTrace (pelo menos por enquanto, que não estamos
	    // fazendo logging) para mostrar a stacktrace no console
	    // Se não fizer isso, você não vai ver a stacktrace de exceptions que seriam importantes
	    // para você durante, especialmente na fase de desenvolvimento
	    ex.printStackTrace();
	    
	    Problem problem = createProblemBuilder(status, problemType, detail)
	    				.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
	    				.build();
	    				

	    return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	} 
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		 return handleValidationInternal(e, headers, status, request, e.getBindingResult());
	}


	private ResponseEntity<Object> handleValidationInternal(Exception e, HttpHeaders headers, HttpStatus status,
			WebRequest request, BindingResult bindingResult) {
		
		ProblemType problemType = ProblemType.DADOS_INVALIDOS;
		 String detail = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.";;
		 		 
		 List<Problem.Object> problemObjects = bindingResult.getAllErrors().stream() 
				 .map(objectError -> { 
					 String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());
					 
					 String name = objectError.getObjectName();
					 
					 if(objectError instanceof FieldError) {
						 name = ((FieldError) objectError).getField();
					 }
					 
					 return Problem.Object.builder()
						 .name(name) 
						 .userMessage(message)
						 .build();
				 }).collect(Collectors.toList());
		 
		 Problem problem = createProblemBuilder(status, problemType, detail)
				    .userMessage(detail)
				    .objects(problemObjects)
					.build();
				
		return handleExceptionInternal(e, problem, new HttpHeaders(), status, request);
	}
		

	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<?> handleEstadoNaoEncontrado(EntidadeNaoEncontradaException e, WebRequest request) {	
		
		HttpStatus status = HttpStatus.NOT_FOUND;
		ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
		String detail = e.getMessage();
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				                    .userMessage(detail)
									.build();
		
		return handleExceptionInternal(e, problem, new HttpHeaders(), status, request);
		
	}
	
	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<?> handleNegocio(NegocioException e, WebRequest request) {
		
		HttpStatus  status = HttpStatus.BAD_REQUEST;
		ProblemType problemType = ProblemType.ERRO_NEGOCIO;
		String detail = e.getMessage();
		
		Problem problem = createProblemBuilder(status, problemType, detail)
									.userMessage(detail)
									.build();
		
		return handleExceptionInternal(e, problem, new HttpHeaders(), status, request);
			
	}
		
	@ExceptionHandler(EntidadeEmUsoException.class)
	public ResponseEntity<?> handleEntidadeEmUso(EntidadeEmUsoException e, WebRequest request) {
		
		HttpStatus  status = HttpStatus.CONFLICT;
		ProblemType problemType = ProblemType.ENTIDADE_EM_USO;
		String detail = e.getMessage();
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessage(detail)
				.build();
				
		
		return handleExceptionInternal(e, problem, new HttpHeaders(), status, request);
					
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<?> handleEntidadeNaoEncontrada(AccessDeniedException ex, WebRequest request) {

	    HttpStatus status = HttpStatus.FORBIDDEN;
	    ProblemType problemType = ProblemType.ACESSO_NEGADO;
	    String detail = ex.getMessage();

	    Problem problem = createProblemBuilder(status, problemType, detail)
	            .userMessage(detail)
	            .userMessage("Você não possui permissão para executar essa operação.")
	            .build();

	    return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		  if(body == null) {
			 body = Problem.builder()
					 .timestamp(OffsetDateTime.now())
					 .title(status.getReasonPhrase())
					 .status(status.value())
					 .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
					 .build();
					
			} else if (body instanceof String) {
			 body = Problem.builder()
					 .timestamp(OffsetDateTime.now())
					 .title((String) body)
					 .status(status.value())
					 .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
					 .build();		
				
			}
		  
		   return super.handleExceptionInternal(ex, body, headers, status, request);
	}
	
	private String joinPath(List<Reference> references) {
	    return references.stream()
	        .map(ref -> ref.getFieldName())
	        .collect(Collectors.joining("."));
	}
	
	
	private Problem.ProblemBuilder createProblemBuilder(HttpStatus status, ProblemType problemType, String detail) {
		
		  return Problem.builder()
				  .timestamp(OffsetDateTime.now())
				  .status(status.value())
				  .type(problemType.getUri())
				  .title(problemType.getTitle())
				  .detail(detail);
		
		
	}
	

}
