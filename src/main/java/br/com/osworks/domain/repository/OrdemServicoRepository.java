package br.com.osworks.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.osworks.domain.model.OrdemDeServico;

@Repository
public interface OrdemServicoRepository extends JpaRepository<OrdemDeServico, Long> {

}
