package com.algaworks.algafood;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;

import javax.validation.ConstraintViolationException;

import org.flywaydb.core.Flyway;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.algaworks.algafood.Util.DatabaseCleaner;
import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class CadastroCozinhaIT {
	
	@LocalServerPort
	private int port;
	
	@Autowired
	private DatabaseCleaner databaseCleaner;
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
//	@Autowired
//	private Flyway flyway;
	
	@Before
	public void setUp() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = "/cozinhas";
		
		databaseCleaner.clearTables();
		prepararDados();
		
//		flyway.migrate();

	}
	
	
	@Test
	public void deveRetornarStatus200QuandoConsultarCozinha() {	
		  given()
			.accept(ContentType.JSON)
		  .when()
		  	.get()
		  .then()
		  	.statusCode(HttpStatus.OK.value());
		
	}
	
	@Test
	public void deveConter2CozinhasQuandoConsultarCozinha() {		
		  given()
			.accept(ContentType.JSON)
		  .when()
		  	.get()
		  .then()
		  	.body("", Matchers.hasSize(2));
		  	
		
	}
	
	@Test
	public void deveRetornarStatus201QuandoCadastrarCozinha() {
		given()
			.body("{\"nome\": \"Chinesa\" }")
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.CREATED.value());
	}
	
	@Test
	public void deveRetornarRespostaEStatusCorretosQuandoConsultarCozinhaExistente() {
		 given()
		 	.pathParam("cozinhaId", 2)
			.accept(ContentType.JSON)
		  .when()
		  	.get("/{cozinhaId}")
		  .then()
		  	.statusCode(HttpStatus.OK.value())
		  	.body("nome", equalTo("Americana"));
		
	}
	
	@Test
	public void deveRetornarStatus404QuandoConsultarCozinhaInexistente() {
		 given()
		 	.pathParam("cozinhaId", 100)
			.accept(ContentType.JSON)
		  .when()
		  	.get("/{cozinhaId}")
		  .then()
		  	.statusCode(HttpStatus.NOT_FOUND.value());
		  	
		
	}
	
	private void prepararDados() {
		Cozinha cozinha1 = new Cozinha();
		cozinha1.setNome("Tailandesa");
		cozinhaRepository.save(cozinha1);
		
		Cozinha cozinha2 = new Cozinha();
		cozinha2.setNome("Americana");
		cozinhaRepository.save(cozinha2);
		
	}
	
	
//** Teste de Integração **
	
//	@Autowired
//	private CadastroCozinhaService cadastroCozinha;
//	
//	@Test
//	public void deveCadastrarCozinhaComSucesso() {
//	  	// cenário
//		Cozinha novaCozinha = new Cozinha();
//		novaCozinha.setNome("Chinesa");
//		
//		// ação
//		novaCozinha  = cadastroCozinha.salvar(novaCozinha);
//		
//		// validação
//		assertThat(novaCozinha).isNotNull();
//		assertThat(novaCozinha.getId()).isNotNull();
//	}
//	
//	@Test(expected = ConstraintViolationException.class)
//	public void deveFalharAoCadastrarCozinhaQuandoSemNome() {
//		Cozinha novaCozinha = new Cozinha();
//		novaCozinha.setNome(null);
//		
//		novaCozinha = cadastroCozinha.salvar(novaCozinha);
//		
//	}
//	
//	@Test(expected = EntidadeEmUsoException.class)
//	public void deveFalharQuandoExcluirCozinhaEmUso() {
//		 cadastroCozinha.excluir(1L);
//	}
//	
// 	@Test(expected = CozinhaNaoEncontradaException.class)
//    public void deveFalharQuandoExcluirCozinhaInexistente() {
//		 cadastroCozinha.excluir(10L);
//
//	}

}
