package rm349040.techchallenge2.domain.main;

import rm349040.techchallenge2.domain.exception.EnderecoNaoAssociadoAoMesmoUsuarioException;
import rm349040.techchallenge2.domain.exception.EnderecoNaoAssociadoException;
import rm349040.techchallenge2.domain.exception.EntityNotFoundException;
import rm349040.techchallenge2.domain.exception.NullException;
import rm349040.techchallenge2.domain.model.Eletrodomestico;
import rm349040.techchallenge2.domain.model.Endereco;
import rm349040.techchallenge2.domain.model.Parente;
import rm349040.techchallenge2.domain.model.Usuario;
import rm349040.techchallenge2.domain.repository.EletrodomesticoRepository;
import rm349040.techchallenge2.domain.repository.EnderecoRepository;
import rm349040.techchallenge2.domain.repository.ParenteRepository;
import rm349040.techchallenge2.domain.repository.UsuarioRepository;

public class System {

	private UsuarioRepository usuarioRepository;
	private ParenteRepository parenteRepository;
	private EnderecoRepository endererecoRepository;
	private EletrodomesticoRepository eletrodomesticoRepository;
	private CRUD crud;
	private CALCULATOR calculator;
	private STATISTICS_CREATOR stats;
	private SystemValidador systemValidador;

	public System() {
		crud = new CRUD();
		systemValidador = new SystemValidador();
	}

	public CRUD crud() {
		return crud;
	}

	public class CRUD {

		public synchronized Usuario create(Usuario usuario) {
			systemValidador.validateCreateUsuario(usuario);
			return usuarioRepository.save(usuario);
		}

		public synchronized Usuario update(Usuario usuario) {
			systemValidador.validateUpdateUsuario(usuario);
			return usuarioRepository.save(usuario);
		}
		
		public synchronized Usuario updateFacilUsuario(Usuario usuario) {

			systemValidador.validateUpdateFacilUsuario(usuario);
			
			usuario = usuarioRepository.save(usuario);

			return usuario;

		}

		public synchronized void delete(Usuario usuario) {
			systemValidador.validateDeleteUsuario(usuario);
			usuarioRepository.delete(usuario);
		}

		public synchronized void delete(Long usuarioId) {
			// É necessário checar a existencia, pois
			// em deleteById se a entidade nao for encontrada no repositorio ela é
			// silenciosamente ignorada.
			Usuario usuario = systemValidador.validateDeleteUsuario(usuarioId);
			usuarioRepository.delete(usuario);
		}

		public synchronized Endereco createEndereco(Endereco endereco) {

			Usuario usuario = endereco.getUsuario();

			systemValidador.validateCreateEndereco(usuario, endereco);

			endereco = endererecoRepository.save(endereco);

			usuario.addEndereco(endereco);

			return endereco;

		}

		public synchronized Endereco updateEndereco(Endereco endereco) {

			systemValidador.validateUpdateEndereco(endereco);

			endereco = endererecoRepository.save(endereco);

			return endereco;

		}

		public synchronized Endereco updateFacilEndereco(Endereco endereco) {

			Endereco end = systemValidador.validateUpdateFacilEndereco(endereco);
			endereco.setUsuario(end.getUsuario());

			endereco = endererecoRepository.save(endereco);

			return endereco;

		}

		public synchronized void deleteEndereco(Endereco endereco) {

			systemValidador.validateDeleteEndereco(endereco);

			endererecoRepository.delete(endereco);

			Usuario u = endereco.getUsuario();

			u.removeEndereco(endereco);

		}

		public synchronized void deleteEndereco(Long enderecoId) {

			// É necessário checar a existencia, pois
			// em deleteById se a entidade nao for encontrada no repositorio ela é
			// silenciosamente ignorada.
			Endereco endereco = systemValidador.validateDeleteEndereco(enderecoId);

			Usuario u = endereco.getUsuario();

			endererecoRepository.delete(endereco);

			u.removeEndereco(endereco);

		}

		public synchronized Parente createParente(Parente parente) {

			systemValidador.validateCreateParente(parente);

			parente = parenteRepository.save(parente);

			parente.getEndereco().addParente(parente);

			return parente;

		}

		public synchronized Parente updateParente(Parente parente) {

			systemValidador.validateUpdateParente(parente);

			parente = parenteRepository.save(parente);

			return parente;

		}

		public synchronized Parente updateFacilParente(Parente parente) {

			Parente p = systemValidador.validateUpdateFacilParente(parente);
			parente.setEndereco(p.getEndereco());

			parente = parenteRepository.save(parente);

			return parente;

		}

		public synchronized void deleteParente(Parente parente) {

			systemValidador.validateDeleteParente(parente);

			parenteRepository.delete(parente);

			Endereco endereco = parente.getEndereco();

			endereco.removeParente(parente);

		}

		public synchronized void deleteParente(Long parenteId) {

			// É necessário checar a existencia, pois
			// em deleteById se a entidade nao for encontrada no repositorio ela é
			// silenciosamente ignorada.
			Parente parente = systemValidador.validateDeleteParente(parenteId);

			Endereco endereco = parente.getEndereco();

			parenteRepository.delete(parente);

			endereco.removeParente(parente);

		}

		public synchronized Eletrodomestico createEletromestico(Eletrodomestico eletrodomestico) {

			systemValidador.validateCreateEletrodomestico(eletrodomestico);

			eletrodomestico = eletrodomesticoRepository.save(eletrodomestico);

			eletrodomestico.getEndereco().addEletrodomestico(eletrodomestico);

			return eletrodomestico;

		}

		public synchronized Eletrodomestico updateEletrodomestico(Eletrodomestico eletrodomestico) {

			systemValidador.validateUpdateEletrodomestico(eletrodomestico);

			eletrodomestico = eletrodomesticoRepository.save(eletrodomestico);

			return eletrodomestico;

		}

		/**
		 * Antes da efetiva atualização, faz várias validações de atualização. <br>
		 * A + importante delas é a de conferir, quando o eletrodoméstico estiver sendo
		 * transferido para outro endereço, se o novo endereço é também um dos endereços
		 * do mesmo usuário do eletrodoméstico. Desta forma, fica garantida a regra de
		 * negócido de que somente pode se atualizar o endereço de um eletromestico,
		 * colocando o eletro em outro endereço, se o novo endereco também for do mesmo
		 * usuário. Atualizar para um endereço que pertence a outro usuário causará
		 * EnderecoNaoAssociadoAoMesmoUsuarioException
		 *
		 * @param eletrodomestico o eletrodoméstico a se atualizar.
		 * @return um eletrodoméstico atualizado
		 *
		 * @throws NullException                               caso eletromestico ou seu
		 *                                                     id ou seu endereco ou o
		 *                                                     id do endereco seja nulo.
		 * @throws EntityNotFoundException                     caso o eletrodomestico a
		 *                                                     se atualizar nao esteja
		 *                                                     no repositório
		 * @throws EnderecoNaoAssociadoException               caso o endereco do
		 *                                                     eletrodomestico não
		 *                                                     esteja no repositório
		 * @throws EnderecoNaoAssociadoAoMesmoUsuarioException caso o novo endereço não
		 *                                                     pertença ao usuário do
		 *                                                     eletrodomestico
		 */
		public synchronized Eletrodomestico updateFacilEletrodomestico(Eletrodomestico eletrodomestico) {

			Eletrodomestico eletro = systemValidador.validateUpdateFacilEletrodomestico(eletrodomestico);

			eletrodomestico.getEndereco().setUsuario(eletro.getEndereco().getUsuario());

			eletrodomestico = eletrodomesticoRepository.save(eletrodomestico);

			return eletrodomestico;

		}

		public synchronized void deleteEletrodomestico(Eletrodomestico eletrodomestico) {

			systemValidador.validateDeleteEletrodomestico(eletrodomestico);

			eletrodomesticoRepository.delete(eletrodomestico);

			Endereco endereco = eletrodomestico.getEndereco();

			endereco.removeEletrodomestico(eletrodomestico);

		}

		public synchronized void deleteEletrodomestico(Long eletrodomesticoId) {

			// É necessário checar a existencia, pois
			// em deleteById se a entidade nao for encontrada no repositorio ela é
			// silenciosamente ignorada.
			Eletrodomestico eletrodomestico = systemValidador
					.validateDeleteEletrodomestico(eletrodomesticoId);

			Endereco endereco = eletrodomestico.getEndereco();

			eletrodomesticoRepository.delete(eletrodomestico);

			endereco.removeEletrodomestico(eletrodomestico);

		}

	}

	public class CALCULATOR {

	}

	public class STATISTICS_CREATOR {

	}

	public void setUsuarioRepository(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
		systemValidador.setUsuarioRepository(usuarioRepository);
	}

	public void setParenteRepository(ParenteRepository parenteRepository) {
		this.parenteRepository = parenteRepository;
		systemValidador.setParenteRepository(parenteRepository);
	}

	public void setEndererecoRepository(EnderecoRepository endererecoRepository) {
		this.endererecoRepository = endererecoRepository;
		systemValidador.setEndererecoRepository(endererecoRepository);
	}

	public void setEletrodomesticoRepository(EletrodomesticoRepository eletrodomesticoRepository) {
		this.eletrodomesticoRepository = eletrodomesticoRepository;
		systemValidador.setEletrodomesticoRepository(eletrodomesticoRepository);
	}

}
