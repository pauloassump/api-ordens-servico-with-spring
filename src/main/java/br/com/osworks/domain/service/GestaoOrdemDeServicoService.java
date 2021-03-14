package br.com.osworks.domain.service;

import java.time.OffsetDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.osworks.domain.exception.NegocioException;
import br.com.osworks.domain.model.Cliente;
import br.com.osworks.domain.model.Comentario;
import br.com.osworks.domain.model.OrdemDeServico;
import br.com.osworks.domain.model.StatusOrdemServico;
import br.com.osworks.domain.repository.ClienteRepository;
import br.com.osworks.domain.repository.ComentarioRepository;
import br.com.osworks.domain.repository.OrdemServicoRepository;

@Service
public class GestaoOrdemDeServicoService {
	
	@Autowired
	private OrdemServicoRepository ordemServicoRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private ComentarioRepository comentarioRepository;
	
	public OrdemDeServico criar(OrdemDeServico ordemServico) {
		Cliente cliente = clienteRepository.findById(ordemServico.getCliente().getId())
				.orElseThrow(() -> new NegocioException("Cliente não encontrado!"));
		
		ordemServico.setCliente(cliente);
		ordemServico.setStatus(StatusOrdemServico.ABERTA);
		ordemServico.setDataAbertura(OffsetDateTime.now());
		
		return ordemServicoRepository.save(ordemServico);
	}
	
	public void finalizarOrdemDeServico(Long ordemDeServicoId) {
		OrdemDeServico ordemDeServico = buscar(ordemDeServicoId);
		
		ordemDeServico.finalizar();
		
		ordemServicoRepository.save(ordemDeServico);
	}

	public Comentario salvarComentario(Long ordemDeServicoId, String descricao) {
		OrdemDeServico ordemDeServico = buscar(ordemDeServicoId);
		
		Comentario comentario = new Comentario();
		comentario.setDataEnvio(OffsetDateTime.now());
		comentario.setDescricao(descricao);
		comentario.setOrdemDeServico(ordemDeServico);
		
		return comentarioRepository.save(comentario);
		
	}
	
	private OrdemDeServico buscar(Long ordemDeServicoId) {
		return ordemServicoRepository.findById(ordemDeServicoId)
				.orElseThrow(() -> new NegocioException("Ordem de serviço não encontrada!"));
	}

}
