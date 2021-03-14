package br.com.osworks.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.osworks.api.model.ComentarioInput;
import br.com.osworks.api.model.ComentarioModel;
import br.com.osworks.domain.exception.NegocioException;
import br.com.osworks.domain.model.Comentario;
import br.com.osworks.domain.model.OrdemDeServico;
import br.com.osworks.domain.repository.OrdemServicoRepository;
import br.com.osworks.domain.service.GestaoOrdemDeServicoService;

@RestController
@RequestMapping("/ordens-servico/{ordemDeServicoId}/comentarios")
public class ComentarioController {
	
	@Autowired
	private GestaoOrdemDeServicoService gestaoOrdemDeServicoService;
	
	@Autowired
	private OrdemServicoRepository ordemDeServicoRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@GetMapping
	public List<ComentarioModel> listar(@PathVariable Long ordemDeServicoId) {
		OrdemDeServico ordemDeServico = ordemDeServicoRepository.findById(ordemDeServicoId)
				.orElseThrow(() -> new NegocioException("Ordem de serviço não encontrada!"));
		
		return toCollectionModel(ordemDeServico.getComentarios());
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ComentarioModel salvar(@PathVariable Long ordemDeServicoId, 
			@Valid @RequestBody ComentarioInput comentarioInput) {
		Comentario comentario = gestaoOrdemDeServicoService.salvarComentario(ordemDeServicoId, comentarioInput.getDescricao());
		
		return toModel(comentario);
	}
	
	private ComentarioModel toModel(Comentario comentario) {
		return modelMapper.map(comentario, ComentarioModel.class);
	}
	
	private List<ComentarioModel> toCollectionModel(List<Comentario> comentarios) {
		return comentarios.stream()
				.map(comentario -> toModel(comentario))
				.collect(Collectors.toList());
	}
}
