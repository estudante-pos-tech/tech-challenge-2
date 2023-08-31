package rm349040.techchallenge2.domain.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rm349040.techchallenge2.domain.exception.DomainException;
import rm349040.techchallenge2.domain.exception.EnderecoNotFoundException;
import rm349040.techchallenge2.domain.exception.NullException;
import rm349040.techchallenge2.domain.exception.UsuarioInexistenteException;
import rm349040.techchallenge2.domain.main.System;
import rm349040.techchallenge2.domain.model.Endereco;
import rm349040.techchallenge2.domain.model.Parente;
import rm349040.techchallenge2.domain.model.Usuario;
import rm349040.techchallenge2.domain.repository.EletrodomesticoRepository;
import rm349040.techchallenge2.domain.repository.EnderecoRepository;
import rm349040.techchallenge2.domain.repository.ParenteRepository;
import rm349040.techchallenge2.domain.repository.UsuarioRepository;

@Service
public class EnderecoCadastroService extends CadastroService<Endereco> {

	/**
	 * Evita repetir código
	 * Reutiliza alguns métodos deste serviço de cadastro de pessoas.
	 * O serviço tem código de busca de pessoas que moram em dado endereço.
	 */
	@Autowired
    private PessoaCadastroService pessoaCadastroService;

	private UsuarioRepository usuarioRepository;
	private EnderecoRepository enderecoRepository;
	private ParenteRepository parenteRepository;
	private EletrodomesticoRepository eletrodomesticoRepository;
	private System system;

    public EnderecoCadastroService(UsuarioRepository usuarioRepository,
			EnderecoRepository enderecoRepository,
			ParenteRepository parenteRepository,
			EletrodomesticoRepository eletrodomesticoRepository,
			System system){

    	super(Endereco.class);

        this.enderecoRepository = enderecoRepository;
        this.parenteRepository = parenteRepository;
        this.usuarioRepository = usuarioRepository;
		repositorio = enderecoRepository;
		
		this.system = system;

		system.setUsuarioRepository(usuarioRepository);
		system.setEndererecoRepository(enderecoRepository);
		system.setParenteRepository(parenteRepository);
		system.setEletrodomesticoRepository(eletrodomesticoRepository);
    }


	@Override
	public Endereco criar(Endereco endereco) {
		return system.crud().createEndereco(endereco);
	}

	@Override
	public Endereco atualizarOuFalhar(Endereco endereco) {
		return system.crud().updateFacilEndereco(endereco);
	}

	public void excluir(Endereco endereco) {
		system.crud().deleteEndereco(endereco);
	}

	@Override
	public void excluirById(Long enderecoId) {
		system.crud().deleteEndereco(enderecoId);
	}

	@Override
	public Collection<Endereco> findByQueryParams(String... params){
		return enderecoRepository.findByQueryParams(params[0], params[1], params[2],params[3], params[4], params[5]);
		//return enderecoRepository.findByCepContainingAndLogradouroContainingAndNumeroContainingAndBairroContainingAndCidadeContainingAndEstadoContaining(params[0], params[1], params[2],params[3], params[4], params[5]);
	}

    @Override
    protected DomainException entityNotFoundException(Long id) {
        return new EnderecoNotFoundException(String.format(ENTITY_NOT_FOUND_MSG(),id));
    }


    /**
     * Busca as pessoas que moram neste endereco.
     * @param id o id do encereco.
     * @return As pessoas que moram aqui
     */
	public Collection<Parente> getParentesQueMoramAqui(Long id) {
		/*
		 * Evita repetir código
		 * Reutiliza alguns métodos deste serviço de cadastro de pessoas.
		 * O serviço tem código de busca de pessoas que moram em dado endereço.
		 */
		return pessoaCadastroService.getQuemMoraAqui(id);
	}



	/**
	 * Busca os endereços deste usuário
	 * @param id o id do usuario
	 * @return Os endereços deste usuário
	 */
	public Collection<Endereco> getEnderecosDesteUsuario(Long id) {
		
		if (id!=null) {

    		Usuario u = usuarioRepository.findById(id)
    							.orElseThrow(
    									() -> new UsuarioInexistenteException("FALHA AO BUSCAR ENDEREÇOS DESTE USUÁRIO : "
												+ "o usuário não está cadastrado no repositório"));

			return u.getMeusEnderecos();
		
		}else {

			throw new NullException("FALHA AO BUSCAR ENDEREÇOS DESTE USUÁRIO : "
					+ "O id do usuário não pode ser null");

		}
		
	}

}
