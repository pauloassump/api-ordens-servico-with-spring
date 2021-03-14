package br.com.osworks.api.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.osworks.api.model.OrdemDeServicoInput;
import br.com.osworks.api.model.OrdemDeServicoModel;
import br.com.osworks.domain.model.OrdemDeServico;
import br.com.osworks.domain.repository.OrdemServicoRepository;
import br.com.osworks.domain.service.GestaoOrdemDeServicoService;

@RestController
@RequestMapping("/ordens-servico")
public class OrdemDeServicoController {
	
	@Autowired
	private GestaoOrdemDeServicoService gestaoOrdemDeServico;
	
	@Autowired
	private OrdemServicoRepository ordemServicoRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public OrdemDeServicoModel criar(@Valid @RequestBody OrdemDeServicoInput ordemDeServicoInput) {
		OrdemDeServico ordemDeServico = toEntity(ordemDeServicoInput);
		
		return toModel(gestaoOrdemDeServico.criar(ordemDeServico));
	}
	
	@GetMapping
	public List<OrdemDeServicoModel> listar() {
		return toCollectionModel(ordemServicoRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<OrdemDeServicoModel> listarById(@PathVariable Long id) {
		Optional<OrdemDeServico> ordemDeServico = ordemServicoRepository.findById(id);
		
		if (ordemDeServico.isPresent()) {
			OrdemDeServicoModel ordemDeServicoModel = toModel(ordemDeServico.get());
			return ResponseEntity.ok(ordemDeServicoModel);
		}
		
		return ResponseEntity.notFound().build();
		
	}
	
	@PutMapping("/{ordemDeServicoId}/encerrar")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void finalizar(@PathVariable Long ordemDeServicoId) {
		gestaoOrdemDeServico.finalizarOrdemDeServico(ordemDeServicoId);
	}
	
	private OrdemDeServicoModel toModel(OrdemDeServico ordemDeServico) {
		return modelMapper.map(ordemDeServico, OrdemDeServicoModel.class);
	}
	
	private List<OrdemDeServicoModel> toCollectionModel(List<OrdemDeServico> ordensDeServico) {
		return ordensDeServico.stream()
				.map(ordemDeServico -> toModel(ordemDeServico))
				.collect(Collectors.toList());
	}
	
	private OrdemDeServico toEntity(OrdemDeServicoInput ordemDeServicoInput) {
		return modelMapper.map(ordemDeServicoInput, OrdemDeServico.class);
	}
	
}
