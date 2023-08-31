package rm349040.techchallenge2.domain.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import rm349040.techchallenge2.domain.main.System;
import rm349040.techchallenge2.domain.model.Usuario;
import rm349040.techchallenge2.domain.repository.EletrodomesticoRepository;
import rm349040.techchallenge2.domain.repository.EnderecoRepository;
import rm349040.techchallenge2.domain.repository.ParenteRepository;
import rm349040.techchallenge2.domain.repository.UsuarioRepository;

@Component
public class UsuarioCadastroService extends CadastroService<Usuario> {
	
	/*
	 * Evita repetir código
	 * Reutiliza alguns métodos deste serviço de cadastro de parentes.
	 * O serviço tem código de busca de parentes de um usuario.
	 */
	@Autowired private PessoaCadastroService pessoaCadastroService;
	
	/*
	 * Evita repetir código
	 * Reutiliza alguns métodos deste serviço de cadastro de eletro.
	 * O serviço tem código de busca de eletros de um usuario.
	 */
	@Autowired private EletrodomesticoCadastroService eletrodomesticoCadastroService;
	
	/*
	 * Evita repetir código
	 * Reutiliza alguns métodos deste serviço de cadastro de enderecos.
	 * O serviço tem código de busca de enderecos de um usuario.
	 */
	@Autowired
	private EnderecoCadastroService enderecoCadastroService;

	private UsuarioRepository usuarioRepository;
	private EnderecoRepository enderecoRepository;
	private ParenteRepository parenteRepository;
	private EletrodomesticoRepository eletrodomesticoRepository;
	private System system;

	public UsuarioCadastroService(UsuarioRepository usuarioRepository,
			EnderecoRepository enderecoRepository,
			ParenteRepository parenteRepository,
			EletrodomesticoRepository eletrodomesticoRepository,
			System system) {
		super(Usuario.class);

		this.usuarioRepository = usuarioRepository;
		this.enderecoRepository = enderecoRepository;
		this.parenteRepository = parenteRepository;
		this.eletrodomesticoRepository = eletrodomesticoRepository;
		repositorio = usuarioRepository;
		
		this.system = system;

		system.setUsuarioRepository(usuarioRepository);
		system.setEndererecoRepository(enderecoRepository);
		system.setParenteRepository(parenteRepository);
		system.setEletrodomesticoRepository(eletrodomesticoRepository);

		java.lang.System.out.println("USURAIOSERVICE : EletroRepository "+eletrodomesticoRepository.toString());
		java.lang.System.out.println("USURAIOSYSTEM : SYSTEM "+system.toString());
		
	}

	@Override
	public Usuario criar(Usuario usuario) {
		return system.crud().create(usuario);
	}

	@Override
	public Usuario atualizarOuFalhar(Usuario usuario) {
		return system.crud().updateFacilUsuario(usuario);
	}

	@Override
	public void excluirById(Long id) {
		system.crud().delete(id);
	}

	@Override
	public Collection<Usuario> findByQueryParams(String... params) {
		return usuarioRepository.findByQueryParams(params[0], params[1],params[2]);
	}

	/**
	 * Busca os endereços deste usuário
	 * @param id o id do usuario
	 * @return Os endereços deste usuário
	 */
	public Collection getMeusEnderecos(Long id) {
		/*
		 * Evita repetir código
		 * Reutiliza alguns métodos deste serviço de cadastro de enderecos.
		 * O serviço tem código de busca de enderecos de um usuario.
		 */
		return enderecoCadastroService.getEnderecosDesteUsuario(id);
	}

	/**
	 * Busca os eletrodomesticos associados a este usuário.
	 * @param id o id do usuário
	 * @return Os eletrodomesticos associados a este usuario
	 */
	public Collection getMeusEletrodomesticos(Long id) {
		/*
		 * Evita repetir código
		 * Reutiliza alguns métodos deste serviço de cadastro de eletro.
		 * O serviço tem código de busca de eletros de um usuario.
		 */
		return eletrodomesticoCadastroService.getEletrodomesticosDesteUsuario(id);
	}

	/**
	 * Busca as pessoas associadas a este usuário.
	 * @param id o id do usuário
	 * @return As pessoas associadas a este usuario
	 */
	public Collection getMeusParentes(Long id) {
		/*
		 * Evita repetir código
		 * Reutiliza alguns métodos deste serviço de cadastro de parentes.
		 * O serviço tem código de busca de parentes de um usuario.
		 */
		return pessoaCadastroService.getParentesDesteUsuario(id);
	}

}
