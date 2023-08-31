package rm349040.techchallenge2.api;

import java.util.Collection;
import java.util.Optional;

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
import rm349040.techchallenge2.api.dtos.eltrodomesticos.output.DadosListagemEletrodomesticoDesteUsuario;
import rm349040.techchallenge2.api.dtos.enderecos.output.EnderecoListagem;
import rm349040.techchallenge2.api.dtos.pessoas.output.DadosListagemParentesDesteUsuario;
import rm349040.techchallenge2.api.dtos.usuarios.DadosAtualizarUsuario;
import rm349040.techchallenge2.api.dtos.usuarios.DadosCadastroUsuario;
import rm349040.techchallenge2.api.dtos.usuarios.output.DadosListagemUsuario;
import rm349040.techchallenge2.api.dtos.usuarios.output.DadosListagemUsuarioPorId;
import rm349040.techchallenge2.api.dtos.usuarios.output.DadosUsuarioAtualizado;
import rm349040.techchallenge2.api.dtos.usuarios.output.DadosUsuarioCriado;
import rm349040.techchallenge2.domain.model.Usuario;
import rm349040.techchallenge2.domain.service.UsuarioCadastroService;
import rm349040.techchallenge2.util.Mapper;

@RestController
@RequestMapping("usuarios")
@Scope("prototype")
public class UsuarioController {

	@Autowired
	private UsuarioCadastroService usuarioCadastroService;

	@Autowired
    private Mapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DadosUsuarioCriado criar(@RequestBody @Valid DadosCadastroUsuario dados) {

        Usuario usuario = mapper.toDomain(dados, Usuario.class);

        usuario = usuarioCadastroService.criar(usuario);

        DadosUsuarioCriado output = mapper.toDto(usuario, DadosUsuarioCriado.class);

        return output;

    }

    @PutMapping("/{id}")
    public DadosUsuarioAtualizado atualizar(@PathVariable Long id, @RequestBody @Valid DadosAtualizarUsuario dados) {

        Usuario preUpdateUsuario = mapper.toDomain(dados, Usuario.class);

        preUpdateUsuario.setId(id);

        //Necessário para setar atributos internos do sistema, como data ultimo acesso,
        //e que NÃO são editáveis pelo consumidor da API
        preUpdate(preUpdateUsuario);

        preUpdateUsuario = usuarioCadastroService.atualizarOuFalhar(preUpdateUsuario);

        DadosUsuarioAtualizado output = mapper.toDto(preUpdateUsuario,DadosUsuarioAtualizado.class);

        return output;

    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
    	usuarioCadastroService.excluirById(id);
    }


    @GetMapping
    public Page<DadosListagemUsuario> listar(Pageable pageable) {
        return mapper.toPage(usuarioCadastroService, pageable, DadosListagemUsuario.class);
    }


    @GetMapping("/{id}")
    public DadosListagemUsuarioPorId listarById(@PathVariable Long id) {
        return new DadosListagemUsuarioPorId(usuarioCadastroService.listarById(id), mapper);
    }
    
    @GetMapping("/{id}/meus-enderecos")
    public Collection<EnderecoListagem> listarEnderecosDesteUsuario(@PathVariable Long id) {
        return mapper.toDtoCollection(usuarioCadastroService.getMeusEnderecos(id), EnderecoListagem.class);
    }
    
    @GetMapping("/{id}/meus-parentes")
    public Collection<DadosListagemParentesDesteUsuario> listarParentesDesteUsuario(@PathVariable Long id) {
        return mapper.toDtoCollection(usuarioCadastroService.getMeusParentes(id), DadosListagemParentesDesteUsuario.class);
    }
    
    @GetMapping("/{id}/meus-eletrodomesticos")
    public Collection<DadosListagemEletrodomesticoDesteUsuario> listarEletrodomesticosDesteUsuario(@PathVariable Long id) {
        return mapper.toDtoCollection(usuarioCadastroService.getMeusEletrodomesticos(id), DadosListagemEletrodomesticoDesteUsuario.class);
    }

    @GetMapping("/por-parametros")
    public Collection<DadosListagemUsuario> listarPorParametrosQuery(
    		String nome,
    		String nascimento,
    		String sexo) {

        Collection<Usuario> usuarios = usuarioCadastroService
        									.findByQueryParams(nome,nascimento,sexo);

        return mapper.toDtoCollection(usuarios, DadosListagemUsuario.class);

    }

    //Necessário para setar atributos internos do sistema, como data ultimo acesso,
    //e que NÃO são editáveis pelo consumidor da API
	private void preUpdate(Usuario preUpdateUsuario) {

		if((preUpdateUsuario == null) || (preUpdateUsuario.getId() == null)) return;

		try {

			Optional<Usuario> u = usuarioCadastroService.getRepository().findById(preUpdateUsuario.getId());

			if(u.isPresent()) {
				preUpdateUsuario.setUltimoAcesso(u.get().getUltimoAcesso());
			}

		}catch (Exception e) {
			e.printStackTrace();
		}

	}


}
