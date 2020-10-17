package com.algaworks.algafood.domain.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.Restaurante;

@Repository
public interface RestauranteRepository extends CustomJpaRepository<Restaurante, Long>, RestauranteRepositoryCustom, 
																		      JpaSpecificationExecutor<Restaurante> {

	//Resolvendo problema do N+1 com fetch join na jpql
	//@Query("from Restaurante r join fetch r.cozinha left join fetch r.formasPagamento")
	@Query("from Restaurante r join fetch r.cozinha")
	List<Restaurante> findAll();
	
		
	//Keywords Pesquisa entre taxainicial e taxaFinal
	List<Restaurante> findByTaxaFreteBetween(BigDecimal taxaInicial, BigDecimal taxaFinal);
	
	
	//@Query("from Restaurante where nome like %:nome% and cozinha.id = :id")
	List<Restaurante> consultarPorNome(String nome, @Param("id") Long cozinha);
	
	//Keywords Pesquisa por nome e id do restaurante
	//List<Restaurante> findByNomeContainingAndCozinhaId(String nome, Long cozinha);
	
	//Keywords Pesquisa por um restaurante usando seu nome
	Optional<Restaurante> findFirstRestauranteByNomeContaining(String nome);
	
	//Keywords só os primeiros 2
	List<Restaurante> findTop2ByNomeContaining(String nome);
	
	int countByCozinhaId(Long cozinha);
	
	//Implementando repositório SDJ customizado
   
	
}
