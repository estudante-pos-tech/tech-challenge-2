
package rm349040.techchallenge2.domain.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import rm349040.techchallenge2.domain.exception.EletrodomesticoNaoAssociadoException;
import rm349040.techchallenge2.domain.exception.EnderecoNaoAssociadoAoMesmoUsuarioException;
import rm349040.techchallenge2.domain.exception.EnderecoNaoAssociadoException;
import rm349040.techchallenge2.domain.exception.EntityNotFoundException;
import rm349040.techchallenge2.domain.exception.NullException;
import rm349040.techchallenge2.domain.main.System;
import rm349040.techchallenge2.domain.model.Eletrodomestico;
import rm349040.techchallenge2.domain.model.Endereco;
import rm349040.techchallenge2.domain.repository.EletrodomesticoRepository;
import rm349040.techchallenge2.domain.repository.EnderecoRepository;
import rm349040.techchallenge2.domain.repository.ParenteRepository;
import rm349040.techchallenge2.domain.repository.UsuarioRepository;

@Service
@Scope("prototype")
public class EletrodomesticoCadastroService extends CadastroService<Eletrodomestico> {

	private UsuarioRepository usuarioRepository;
	private EnderecoRepository enderecoRepository;
	private ParenteRepository parenteRepository;
	private EletrodomesticoRepository eletrodomesticoRepository;
	private System system;

    public EletrodomesticoCadastroService(
    		UsuarioRepository usuarioRepository,
			EnderecoRepository enderecoRepository,
			ParenteRepository parenteRepository,
			EletrodomesticoRepository eletrodomesticoRepository,
			System system) {

        super(Eletrodomestico.class);
        repositorio = eletrodomesticoRepository;
        this.eletrodomesticoRepository = eletrodomesticoRepository;
        this.enderecoRepository = enderecoRepository;
        this.usuarioRepository = usuarioRepository;
        this.system = system;

        system.setUsuarioRepository(usuarioRepository);
		system.setEndererecoRepository(enderecoRepository);
		system.setParenteRepository(parenteRepository);
		system.setEletrodomesticoRepository(eletrodomesticoRepository);
		
		java.lang.System.out.println("EletroRepository "+eletrodomesticoRepository.toString());
		java.lang.System.out.println("UsuarioRepository "+usuarioRepository.toString());
		java.lang.System.out.println("ELETROSYSTEM : SYSTEM "+system.toString());

    }


    @Override
	public Eletrodomestico criar(Eletrodomestico eletrodomestico) {
		return system.crud().createEletromestico(eletrodomestico);
	}

    /**
	 * Antes da efetiva atualização, são feitas várias validações de atualização.
	 * <br> A + importante delas é a de conferir, quando o eletrodoméstico estiver sendo transferido para outro endereço, se
	 * o novo endereço é também um dos endereços do mesmo usuário do eletrodoméstico. Desta forma, fica garantida a regra de negócido de que
	 * somente pode se atualizar o endereço de um eletromestico, colocando o eletro em outro endereço, se o novo endereco também for do mesmo usuário.
	 * Atualizar para um endereço que pertence a outro usuário causará EnderecoNaoAssociadoAoMesmoUsuarioException
	 *
	 * @param eletrodomestico o eletrodoméstico a se atualizar.
	 * @return um eletrodoméstico atualizado
	 *
	 * @throws NullException caso eletromestico ou seu id ou seu endereco ou o id do endereco seja nulo.
	 * @throws EntityNotFoundException caso o eletrodomestico a se atualizar nao esteja no repositório
	 * @throws EnderecoNaoAssociadoException caso o endereco do eletrodomestico não esteja no repositório
	 * @throws EnderecoNaoAssociadoAoMesmoUsuarioException caso o novo endereço não pertença ao usuário do eletrodomestico
	 */
	@Override
	public Eletrodomestico atualizarOuFalhar(Eletrodomestico eletrodomestico) {
		return system.crud().updateFacilEletrodomestico(eletrodomestico);
	}

	public void excluir(Eletrodomestico eletrodomestico) {
		system.crud().deleteEletrodomestico(eletrodomestico);
	}

	@Override
	public void excluirById(Long eletrodomesticoId) {
		system.crud().deleteEletrodomestico(eletrodomesticoId);
	}

	@Override
	public Collection<Eletrodomestico> findByQueryParams(String... params){
		return eletrodomesticoRepository.findByQueryParams(params[0], params[1], params[2]);
	}


	/**
	 * Busca os eletrodomesticos associados a este usuário.
	 * @param id o id do usuário
	 * @return Os eletrodomesticos associados a este usuario
	 */
	public Collection<Eletrodomestico> getEletrodomesticosDesteUsuario(Long id) {

		return super
				.getEntitiesByUsuarioId(id,
										(endereco) ->  endereco.getMeusEletrodomesticos(),
										Eletrodomestico.class,
										enderecoRepository,
										usuarioRepository);

	}

	/**
	 * Busca os eletrodomesticos que estão neste endereço.
	 * @param id o id do endereço onde estão os eletrodomesticos
	 * @return Os eletrodomesticos que estão neste endereço.
	 */

	public Collection<Eletrodomestico> getEletrodomesticosDesteEndereco(Long id) {

		if (id!=null) {

			Endereco endereco = enderecoRepository
					.findById(id)
					.orElseThrow(
							() -> new EnderecoNaoAssociadoException(
									String.format("FALHA AO BUSCAR ELETRODOMÉSTICOS QUE ESTÃO "
											+ "NESTE ENDEREÇO (id=%s) : o endereço não está no repositório", id)));

			return eletrodomesticoRepository.findByEnderecoId(endereco.getId());

		}else {
			throw new NullException("FALHA AO BUSCAR ELETRODOMÉSTICOS QUE ESTÃO NESTE ENDEREÇO : O id do endereço não pode ser null");
		}

	}


	public Eletrodomestico getEletroById(Long id) {

		if (id!=null) {

			return eletrodomesticoRepository
					.findById(id)
					.orElseThrow(
							() -> new EletrodomesticoNaoAssociadoException(
									String.format("FALHA AO CALCULAR CONSUMO PARA ELETRODOMÉSTICO (id=%s) : "
												+ "o eletrodoméstico não está no repositório", id)));

		}else {
			throw new NullException("FALHA AO CALCULAR CONSUMO PARA ELETRODOMÉSTICO : O id do eletrodoméstico não pode ser null");
		}
	}
}
