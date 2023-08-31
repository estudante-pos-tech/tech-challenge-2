package rm349040.techchallenge2.api;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import rm349040.techchallenge2.api.dtos.enderecos.DadosAtualizarEndereco;
import rm349040.techchallenge2.api.dtos.enderecos.DadosCadastroEndereco;
import rm349040.techchallenge2.api.dtos.enderecos.output.DadosEnderecoAtualizado;
import rm349040.techchallenge2.api.dtos.enderecos.output.DadosEnderecoCriado;
import rm349040.techchallenge2.api.dtos.enderecos.output.DadosEnderecoPorId;
import rm349040.techchallenge2.api.dtos.enderecos.output.DadosListagemEndereco;
import rm349040.techchallenge2.api.dtos.enderecos.output.EnderecoListagem;
import rm349040.techchallenge2.api.dtos.enderecos.output.ParentesQueMoramAquiListagem;
import rm349040.techchallenge2.domain.model.Endereco;
import rm349040.techchallenge2.domain.service.EnderecoCadastroService;
import rm349040.techchallenge2.util.Mapper;

@RestController
@RequestMapping("enderecos")
@Scope("prototype")
public class EnderecoController {

    @Autowired
    private EnderecoCadastroService enderecoCadastroService;

    @Autowired
    private Mapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DadosEnderecoCriado criar(@RequestBody @Valid DadosCadastroEndereco dados) {

        Endereco endereco = mapper.toDomain(dados, Endereco.class);

        endereco = enderecoCadastroService.criar(endereco);

        DadosEnderecoCriado output = mapper.toDto(endereco, DadosEnderecoCriado.class);

        return output;

    }

    @PutMapping("/{id}")
    public DadosEnderecoAtualizado atualizar(@PathVariable Long id, @RequestBody @Valid DadosAtualizarEndereco dados) {

        Endereco endereco = mapper.toDomain(dados, Endereco.class);

        endereco.setId(id);

        endereco = enderecoCadastroService.atualizarOuFalhar(endereco);

        DadosEnderecoAtualizado output = mapper.toDto(endereco,DadosEnderecoAtualizado.class);

        return output;

    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        enderecoCadastroService.excluirById(id);
    }


    @GetMapping
    public Page<DadosListagemEndereco> listar(Pageable pageable) {
        return mapper.toPage( enderecoCadastroService, pageable, DadosListagemEndereco.class);
    }


    @GetMapping("/{id}")
    public DadosEnderecoPorId listarById(@PathVariable Long id) {
    	return new DadosEnderecoPorId(enderecoCadastroService.listarById(id), mapper);
    }

    @GetMapping("/por-parametros")
    public Collection<DadosListagemEndereco> listarPorParametrosQuery(
    		String cep,
    		String logradouro,
    		String numero,
    		String bairro,
    		String cidade,
    		String estado) {

        Collection<Endereco> enderecos = enderecoCadastroService
        									.findByQueryParams(cep,logradouro,numero,bairro,cidade,estado);

        return mapper.toDtoCollection(enderecos, DadosListagemEndereco.class);

    }

    @GetMapping("/quem-mora-aqui/{id}")
    public Collection<ParentesQueMoramAquiListagem> listarQuemMoraAqui(@PathVariable Long id) {
    	return mapper.toDtoCollection(enderecoCadastroService.getParentesQueMoramAqui(id), ParentesQueMoramAquiListagem.class);
    }
    
    @GetMapping("/deste-usuario/{id}")
    public Collection<EnderecoListagem> listarEnderecosDesteUsuario(@PathVariable Long id) {
    	return mapper.toDtoCollection(enderecoCadastroService.getEnderecosDesteUsuario(id), EnderecoListagem.class);
    }

}
