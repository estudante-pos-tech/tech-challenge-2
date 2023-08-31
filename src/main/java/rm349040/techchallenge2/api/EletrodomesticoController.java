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
import rm349040.techchallenge2.api.dtos.eltrodomesticos.DadosAtualizarEletrodomestico;
import rm349040.techchallenge2.api.dtos.eltrodomesticos.DadosCadastroEletrodomestico;
import rm349040.techchallenge2.api.dtos.eltrodomesticos.output.DadosConsumoEletrodomestico;
import rm349040.techchallenge2.api.dtos.eltrodomesticos.output.DadosEletromesticoAtualizado;
import rm349040.techchallenge2.api.dtos.eltrodomesticos.output.DadosEletromesticoCriado;
import rm349040.techchallenge2.api.dtos.eltrodomesticos.output.DadosListagemEletrodomestico;
import rm349040.techchallenge2.api.dtos.eltrodomesticos.output.DadosListagemEletrodomesticoDesteUsuario;
import rm349040.techchallenge2.api.dtos.eltrodomesticos.output.DadosListagemEletrodomesticoNesteEndereco;
import rm349040.techchallenge2.domain.model.Eletrodomestico;
import rm349040.techchallenge2.domain.service.EletrodomesticoCadastroService;
import rm349040.techchallenge2.util.Mapper;

@RestController
@RequestMapping("eletrodomesticos")
@Scope("prototype")
public class EletrodomesticoController {

    @Autowired
    private EletrodomesticoCadastroService eletrodomesticoCadastroService;

    @Autowired
    private Mapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DadosEletromesticoCriado criar(@RequestBody @Valid DadosCadastroEletrodomestico dados) {

        Eletrodomestico eletrodomestico = mapper.toDomain(dados, Eletrodomestico.class);

        eletrodomestico = eletrodomesticoCadastroService.criar(eletrodomestico);

        DadosEletromesticoCriado output = mapper.toDto(eletrodomestico, DadosEletromesticoCriado.class);

        return output;

    }


    @PutMapping("/{id}")
    public DadosEletromesticoAtualizado atualizar(@PathVariable Long id, @RequestBody @Valid DadosAtualizarEletrodomestico dados) {

        Eletrodomestico eletrodomestico = toDomain(dados);

        eletrodomestico.setId(id);

        eletrodomestico = eletrodomesticoCadastroService.atualizarOuFalhar(eletrodomestico);

        DadosEletromesticoAtualizado output = mapper.toDto(eletrodomestico,DadosEletromesticoAtualizado.class);

        return output;

    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        eletrodomesticoCadastroService.excluirById(id);
    }


    @GetMapping
    public Page<DadosListagemEletrodomestico> listar(Pageable pageable) {
    	return mapper.toPage(eletrodomesticoCadastroService, pageable, DadosListagemEletrodomestico.class);
    }


    @GetMapping("/{id}")
    public DadosListagemEletrodomestico listarById(@PathVariable Long id) {
        return mapper.toDto(eletrodomesticoCadastroService.listarById(id),DadosListagemEletrodomestico.class);
    }

    @GetMapping("/por-parametros")
    public Collection<DadosListagemEletrodomestico> listarPorParametrosQuery(
    		String nome,
    		String modelo,
    		String potencia)
    {

        Collection<Eletrodomestico> eletrodomesticos =
        					eletrodomesticoCadastroService.findByQueryParams(nome,modelo,potencia);

        return mapper.toDtoCollection(eletrodomesticos, DadosListagemEletrodomestico.class);

    }

    @GetMapping("/que-estao-neste-endereco/{id}")
    public Collection<DadosListagemEletrodomesticoNesteEndereco> listarEletrodomesticosNesteEndereco(@PathVariable Long id) {
        return mapper.toDtoCollection(eletrodomesticoCadastroService.getEletrodomesticosDesteEndereco(id), DadosListagemEletrodomesticoNesteEndereco.class);
    }

    @GetMapping("/deste-usuario/{id}")
    public Collection<DadosListagemEletrodomesticoDesteUsuario> listarEletrodomesticosAssociadosAEsteUsuario(@PathVariable Long id) {
        return mapper.toDtoCollection(eletrodomesticoCadastroService.getEletrodomesticosDesteUsuario(id), DadosListagemEletrodomesticoDesteUsuario.class);
    }

    @GetMapping("/consumo/{id}/{tempoDeUsoEmSegundos}")
    public DadosConsumoEletrodomestico getConsumoEletrodomesticoNessePeriodo(@PathVariable Long id, @PathVariable Long tempoDeUsoEmSegundos) {
        return new DadosConsumoEletrodomestico(eletrodomesticoCadastroService.getEletroById(id), tempoDeUsoEmSegundos);
    }

    private Eletrodomestico toDomain(Object dto){
        return mapper.toDomain(dto, Eletrodomestico.class);
    }


}//FIM CRUD ELETRODOMÉSTICOS
