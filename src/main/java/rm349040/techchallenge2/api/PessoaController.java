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
import rm349040.techchallenge2.api.dtos.pessoas.DadosAtualizarParente;
import rm349040.techchallenge2.api.dtos.pessoas.DadosCadastroParente;
import rm349040.techchallenge2.api.dtos.pessoas.output.DadosListagemParentesDesteUsuario;
import rm349040.techchallenge2.api.dtos.pessoas.output.DadosListagemPessoa;
import rm349040.techchallenge2.api.dtos.pessoas.output.DadosListagemPessoaNesteEndereco;
import rm349040.techchallenge2.api.dtos.pessoas.output.DadosListagemPessoaPorId;
import rm349040.techchallenge2.api.dtos.pessoas.output.DadosParenteAtualizado;
import rm349040.techchallenge2.api.dtos.pessoas.output.DadosParenteCriado;
import rm349040.techchallenge2.domain.model.Parente;
import rm349040.techchallenge2.domain.service.PessoaCadastroService;
import rm349040.techchallenge2.util.Mapper;

@RestController
@RequestMapping("pessoas")
@Scope("prototype")
public class PessoaController  {


    @Autowired
    private PessoaCadastroService pessoaCadastroService;

    @Autowired
    private Mapper mapper;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DadosParenteCriado criar(@RequestBody @Valid DadosCadastroParente dados) {

        Parente parente = mapper.toDomain(dados, Parente.class);

        parente = pessoaCadastroService.criar(parente);

        DadosParenteCriado output = mapper.toDto(parente, DadosParenteCriado.class);

        return output;

    }

    @PutMapping("/{id}")
    public DadosParenteAtualizado atualizar(@PathVariable Long id, @RequestBody @Valid DadosAtualizarParente dados) {

    	Parente parente = mapper.toDomain(dados, Parente.class);

        parente.setId(id);

        parente = pessoaCadastroService.atualizarOuFalhar(parente);

        DadosParenteAtualizado output = mapper.toDto(parente,DadosParenteAtualizado.class);

        return output;

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        pessoaCadastroService.excluirById(id);
    }

    @GetMapping
    public Page<DadosListagemPessoa> listar(Pageable pageable) {
        return mapper.toPage(pessoaCadastroService, pageable, DadosListagemPessoa.class);
    }

    @GetMapping("/{id}")
    public DadosListagemPessoaPorId listarById(@PathVariable Long id) {
        return mapper.toDto(pessoaCadastroService.listarById(id),DadosListagemPessoaPorId.class);
    }

    @GetMapping("/por-parametros")
    public Collection<DadosListagemPessoa> listarPorParametrosQuery(
    		String nome,
    		String nascimento,
    		String sexo,
    		String parentesco) {


        Collection<Parente> parentes = pessoaCadastroService
        									.findByQueryParams(nome,nascimento,sexo, parentesco);

        return mapper.toDtoCollection(parentes, DadosListagemPessoa.class);

    }

    @GetMapping("/que-moram-comigo/{id}")
    public Collection<DadosListagemPessoa> listarQuemMoraComigo(@PathVariable Long id) {
        return mapper.toDtoCollection(pessoaCadastroService.getQuemMoraComigo(id), DadosListagemPessoa.class);
    }

    @GetMapping("/que-moram-neste-endereco/{id}")
    public Collection<DadosListagemPessoaNesteEndereco> listarPessoasNesteEndereco(@PathVariable Long id) {
        return mapper.toDtoCollection(pessoaCadastroService.getQuemMoraAqui(id), DadosListagemPessoaNesteEndereco.class);
    }

    @GetMapping("/deste-usuario/{id}")
    public Collection<DadosListagemParentesDesteUsuario> listarPessoasAssociadasAEsteUsuario(@PathVariable Long id) {
        return mapper.toDtoCollection(pessoaCadastroService.getParentesDesteUsuario(id), DadosListagemParentesDesteUsuario.class);
    }


  }//FIM CRUD
