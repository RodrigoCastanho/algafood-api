package com.algaworks.algafood.core.openapi;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLStreamHandler;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.http.HttpStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.algaworks.algafood.api.controller.exceptionhandler.Problem;
import com.algaworks.algafood.api.model.CidadeModel;
import com.algaworks.algafood.api.model.CidadesModelOpenApi;
import com.algaworks.algafood.api.model.CozinhaModel;
import com.algaworks.algafood.api.model.EstadoModel;
import com.algaworks.algafood.api.model.FormaPagamentoModel;
import com.algaworks.algafood.api.model.GrupoModel;
import com.algaworks.algafood.api.model.LinksModelOpenApi;
import com.algaworks.algafood.api.model.PedidoResumoModel;
import com.algaworks.algafood.api.model.PermissaoModel;
import com.algaworks.algafood.api.model.ProdutoModel;
import com.algaworks.algafood.api.model.ProdutosModelOpenApi;
import com.algaworks.algafood.api.model.RestauranteBasicoModel;
import com.algaworks.algafood.api.model.UsuarioModel;
import com.algaworks.algafood.api.v1.openapi.model.CozinhasModelOpenApi;
import com.algaworks.algafood.api.v1.openapi.model.EstadosModelOpenApi;
import com.algaworks.algafood.api.v1.openapi.model.FormasPagamentoModelOpenApi;
import com.algaworks.algafood.api.v1.openapi.model.GruposModelOpenApi;
import com.algaworks.algafood.api.v1.openapi.model.PageableModelOpenApi;
import com.algaworks.algafood.api.v1.openapi.model.PedidosResumoModelOpenApi;
import com.algaworks.algafood.api.v1.openapi.model.PermissoesModelOpenApi;
import com.algaworks.algafood.api.v1.openapi.model.RestaurantesBasicoModelOpenApi;
import com.algaworks.algafood.api.v1.openapi.model.UsuariosModelOpenApi;
import com.fasterxml.classmate.TypeResolver;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.ResourceOwnerPasswordCredentialsGrant;
import springfox.documentation.service.Response;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.service.SecurityScheme;



@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class SpringFoxConfig implements WebMvcConfigurer {
	
	@Bean
	public Docket apiDocket() {
		TypeResolver typeResolver = new TypeResolver();
		
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("v1")
				.select()
					.apis(RequestHandlerSelectors.basePackage("com.algaworks.algafood.api"))
//					.paths(PathSelectors.ant("/restaurantes/*")) busca como referencia só endpoint com nome restaurantes em diante
					.paths(PathSelectors.ant("/v1/**"))
					.build()
				.useDefaultResponseMessages(false)
				.globalResponses(org.springframework.http.HttpMethod.GET, globalGetResponseMessages())
				.globalResponses(org.springframework.http.HttpMethod.POST, globalPostPutResponseMessages())
				.globalResponses(org.springframework.http.HttpMethod.PUT, globalPostPutResponseMessages())
				.globalResponses(org.springframework.http.HttpMethod.DELETE, globalDeleteResponseMessages())
				.additionalModels(typeResolver.resolve(Problem.class)) //Lista a class Problem na documentação
				.ignoredParameterTypes(ServletWebRequest.class,
	                    URL.class, URI.class, URLStreamHandler.class, Resource.class,
	                    File.class, InputStream.class)
				.directModelSubstitute(Pageable.class, PageableModelOpenApi.class)
				.directModelSubstitute(Links.class, LinksModelOpenApi.class)
//				.globalRequestParameters(Arrays.asList(new RequestParameterBuilder()
//						.name("campos")
//						.description("Nomes das propriedades para filtrar na resposta, separados por vígula")
//						.build()))
				.alternateTypeRules(AlternateTypeRules.newRule(
						typeResolver.resolve(PagedModel.class, CozinhaModel.class), 
						CozinhasModelOpenApi.class))	
				.alternateTypeRules(AlternateTypeRules.newRule(
					    typeResolver.resolve(PagedModel.class, PedidoResumoModel.class),
					    PedidosResumoModelOpenApi.class))
				.alternateTypeRules(AlternateTypeRules.newRule(
	                    typeResolver.resolve(CollectionModel.class, CidadeModel.class),
	                    CidadesModelOpenApi.class))
				.alternateTypeRules(AlternateTypeRules.newRule(
				        typeResolver.resolve(CollectionModel.class, EstadoModel.class),
				        EstadosModelOpenApi.class))
				.alternateTypeRules(AlternateTypeRules.newRule(
					    typeResolver.resolve(CollectionModel.class, FormaPagamentoModel.class),
					    FormasPagamentoModelOpenApi.class))
				.alternateTypeRules(AlternateTypeRules.newRule(
					    typeResolver.resolve(CollectionModel.class, GrupoModel.class),
					    GruposModelOpenApi.class))
			    .alternateTypeRules(AlternateTypeRules.newRule(
					        typeResolver.resolve(CollectionModel.class, PermissaoModel.class),
					        PermissoesModelOpenApi.class))
			    .alternateTypeRules(AlternateTypeRules.newRule(
			    	    typeResolver.resolve(CollectionModel.class, ProdutoModel.class),
			    	    ProdutosModelOpenApi.class))
			    .alternateTypeRules(AlternateTypeRules.newRule(
			    	    typeResolver.resolve(CollectionModel.class, RestauranteBasicoModel.class),
			    	    RestaurantesBasicoModelOpenApi.class))

			     .alternateTypeRules(AlternateTypeRules.newRule(
			    	        typeResolver.resolve(CollectionModel.class, UsuarioModel.class),
			    	        UsuariosModelOpenApi.class))
			     .securitySchemes(Arrays.asList(securityScheme()))
				 .securityContexts(Arrays.asList(securityContext()))
				//.ignoredParameterTypes(ServletWebRequest.class)
				.apiInfo(apiInfo())
				.tags(new Tag("Cidades", "Gerencia as cidades"),
					  new Tag("Grupos", "Gerencia os grupos de usuários"),
					  new Tag("Cozinhas", "Gerencia as cozinhas"),
					  new Tag("Formas de pagamento", "Gerencia as formas de pagamento"),
					  new Tag("Pedidos", "Gerencia os pedidos"),
					  new Tag("Restaurantes", "Gerencia os restaurantes"),
					  new Tag("Estados", "Gerencia os estados"),
					  new Tag("Produtos", "Gerencia os produtos de restaurantes"),
					  new Tag("Usuários", "Gerencia os usuários"),
					  new Tag("Estatísticas", "Estatísticas da AlgaFood"),
					  new Tag("Permissões", "Gerencia as permissões"));
					
	}
	
	private SecurityScheme securityScheme() {
		return new OAuthBuilder()
				.name("AlgaFood")
				.grantTypes(grantTypes())
				.scopes(scopes())
				.build();
	}
	
	@SuppressWarnings("deprecation")
	private SecurityContext securityContext() {
		var securityReference = SecurityReference.builder()
				.reference("AlgaFood")
				.scopes(scopes().toArray(new AuthorizationScope[0]))
				.build();
		
		return SecurityContext.builder()
				.securityReferences(Arrays.asList(securityReference))
				.forPaths(PathSelectors.any())
				.build();
	}
	
	private List<GrantType> grantTypes() {
		return Arrays.asList(new ResourceOwnerPasswordCredentialsGrant("/oauth/token"));
	}
	
	private List<AuthorizationScope> scopes() {
		return Arrays.asList(new AuthorizationScope("READ", "Acesso de leitura"),
				new AuthorizationScope("WRITE", "Acesso de escrita"));
	}
	
	private List<Response> globalGetResponseMessages() {
		return Arrays.asList(
				new ResponseBuilder()
					.code(String.valueOf(HttpStatus.SC_INTERNAL_SERVER_ERROR))
					.description("Erro interno do servidor")
					.build(),
				new ResponseBuilder()
					.code(String.valueOf(HttpStatus.SC_NOT_ACCEPTABLE))
					.description("Recurso não possui representação que poderia ser aceita pelo consumidor")				
					.build()
	        );
	}
	
	private List<Response> globalPostPutResponseMessages() {
		return Arrays.asList(
			new ResponseBuilder()
				.code(String.valueOf(HttpStatus.SC_BAD_REQUEST))
				.description("Requisição inválida(erro do cliente)")
				.build(),
			new ResponseBuilder()
				.code(String.valueOf(HttpStatus.SC_INTERNAL_SERVER_ERROR))
				.description("Erro interno no servidor")
				.build(),
		   new ResponseBuilder()
				.code(String.valueOf(HttpStatus.SC_NOT_ACCEPTABLE))
				.description("Recurso não possui representação que poderia ser aceita pelo consumidor")
				.build(),	
		new ResponseBuilder()
				.code(String.valueOf(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE))
				.description("Requisição recusada porque o corpo está em um formato não suportado")
				.build()							 	
			);
			
	}
	
	private List<Response> globalDeleteResponseMessages() {
		return Arrays.asList(
		    new ResponseBuilder()
				.code(String.valueOf(HttpStatus.SC_BAD_REQUEST))
				.description("Requisição inválida(erro do cliente)")
				.build(),
			new ResponseBuilder()
				.code(String.valueOf(HttpStatus.SC_INTERNAL_SERVER_ERROR))
				.description("Erro interno no servidor")
				.build()
			);		
	}
	
	public ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("AlgaFood API")
				.description("API aberta para cliente e restaurantes")
				.version("1")
				.contact(new Contact("AlgaWorks", "https://www.algaworks.com", "contato@algaworks.com"))
				.build();		
		
//		http://localhost:8080/swagger-ui/index.html link acesso
		
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    registry.addResourceHandler("index.html")
	    	.addResourceLocations("classpath:/META-INF/resources/");
	    
	}

}

