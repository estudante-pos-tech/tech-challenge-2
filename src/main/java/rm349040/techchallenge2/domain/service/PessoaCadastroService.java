package rm349040.techchallenge2.domain.service;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rm349040.techchallenge2.domain.exception.EnderecoNaoAssociadoException;
import rm349040.techchallenge2.domain.exception.NullException;
import rm349040.techchallenge2.domain.main.System;
import rm349040.techchallenge2.domain.model.Endereco;
import rm349040.techchallenge2.domain.model.Parente;
import rm349040.techchallenge2.domain.repository.EletrodomesticoRepository;
import rm349040.techchallenge2.domain.repository.EnderecoRepository;
import rm349040.techchallenge2.domain.repository.ParenteRepository;
import rm349040.techchallenge2.domain.repository.UsuarioRepository;

@Service
public class PessoaCadastroService extends CadastroService<Parente>{


	private UsuarioRepository usuarioRepository;
	private EnderecoRepository enderecoRepository;
	private ParenteRepository parenteRepository;
	private EletrodomesticoRepository eletrodomesticoRepository;
	private System system;


    public PessoaCadastroService(UsuarioRepository usuarioRepository,
			EnderecoRepository enderecoRepository,
			ParenteRepository parenteRepository,
			EletrodomesticoRepository eletrodomesticoRepository,
			System system) {

        super(Parente.class);

        this.parenteRepository = parenteRepository;
        this.enderecoRepository = enderecoRepository;
        this.usuarioRepository = usuarioRepository;
        repositorio = parenteRepository;

        this.system = system;
        
        system.setUsuarioRepository(usuarioRepository);
		system.setEndererecoRepository(enderecoRepository);
		system.setParenteRepository(parenteRepository);
		system.setEletrodomesticoRepository(eletrodomesticoRepository);

    }

	@Override
	public Parente criar(Parente parente) {
		return system.crud().createParente(parente);
	}

	@Override
	public Parente atualizarOuFalhar(Parente parente) {
		return system.crud().updateFacilParente(parente);
	}

	public void excluir(Parente parente) {
		system.crud().deleteParente(parente);
	}

	@Override
	public void excluirById(Long parenteId) {
		system.crud().deleteParente(parenteId);
	}

	@Override
	public Collection<Parente> findByQueryParams(String... params) {
		return parenteRepository.findByQueryParams(params[0], params[1],params[2], params[3]);
	}

	/**
	 * Busca as pessoas que moram comigo.
	 * @param id o meu id.
	 * @return As pessoas que moram comigo.
	 */
	public Collection<Parente> getQuemMoraComigo(Long id) {

		acao = CRUD.LISTAR;

		if (id!=null) {
			Parente p = parenteRepository.findById(id).orElseThrow(() -> entityNotFoundException(id));
			return parenteRepository.findByEnderecoId(p.getEndereco().getId())
					.stream()
					.filter(parente -> ! parente.getId().equals(id) )
					.collect(Collectors.toSet());
		}else {
			throw new NullException("FALHA AO BUSCAR QUEM MORA COMIGO : O id do parente não pode ser null");
		}
	}

	/**
	 * Busca as pessoas que moram neste endereço.
	 * @param id o id do endereço onde moram pessoas
	 * @return A pessoas que moram neste endereço.
	 */
	public Collection<Parente> getQuemMoraAqui(Long id) {

		if (id!=null) {

			Endereco endereco = enderecoRepository
					.findById(id)
					.orElseThrow(
							() -> new EnderecoNaoAssociadoException(
									String.format("FALHA AO BUSCAR PESSOAS QUE MORAM NESTE "
											+ "ENDEREÇO (id=%s) : o endereço não está no repositório", id)));

			return parenteRepository.findByEnderecoId(endereco.getId());

		}else {
			throw new NullException("FALHA AO BUSCAR QUEM MORA AQUI : O id do endereço não pode ser null");
		}
	}

	/**
	 * Busca as pessoas associadas a este usuário.
	 * @param id o id do usuário
	 * @return As pessoas associadas a este usuario
	 */
	public Collection<Parente> getParentesDesteUsuario(Long id) {

		return super
				.getEntitiesByUsuarioId(id,
										(endereco) ->  endereco.getMeusParentes(),
										Parente.class,
										enderecoRepository,
										usuarioRepository);

	}

}
