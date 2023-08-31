//TODO refatorar validateCreateEndereco, validateCreateParente, validateCreateEletrodomestico para um médodo comum para evitar TANTO código PARECIDO
//TODO refatorar validateUpdateEndereco, validateUpdateParente, validateUpdateEletrodomestico para um médodo comum para evitar TANTO código PARECIDO
//TODO refatorar validateDeleteEndereco, validateDeleteParente, validateDeleteEletrodomestico para um médodo comum para evitar TANTO código PARECIDO
package rm349040.techchallenge2.domain.main;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

import rm349040.techchallenge2.domain.exception.DomainException;
import rm349040.techchallenge2.domain.exception.EletrodomesticoJaAssociadoAEsteEnderecoException;
import rm349040.techchallenge2.domain.exception.EletrodomesticoJaAssociadoAOutroEnderecoException;
import rm349040.techchallenge2.domain.exception.EletrodomesticoNaoAssociadoAoEnderecoException;
import rm349040.techchallenge2.domain.exception.EletrodomesticoNaoAssociadoException;
import rm349040.techchallenge2.domain.exception.EnderecoCaseInsensitivePropertyException;
import rm349040.techchallenge2.domain.exception.EnderecoJaAssociadoAOutroUsuarioExcepition;
import rm349040.techchallenge2.domain.exception.EnderecoJaAssociadoAoUsuarioException;
import rm349040.techchallenge2.domain.exception.EnderecoJaNoRepositorioException;
import rm349040.techchallenge2.domain.exception.EnderecoNaoAssociadoAoMesmoUsuarioException;
import rm349040.techchallenge2.domain.exception.EnderecoNaoAssociadoException;
import rm349040.techchallenge2.domain.exception.EntityNotFoundException;
import rm349040.techchallenge2.domain.exception.IdNullException;
import rm349040.techchallenge2.domain.exception.NullException;
import rm349040.techchallenge2.domain.exception.ParenteJaAssociadoAEsteEnderecoException;
import rm349040.techchallenge2.domain.exception.ParenteJaAssociadoAOutroEnderecoException;
import rm349040.techchallenge2.domain.exception.ParenteJaNoRepositorioException;
import rm349040.techchallenge2.domain.exception.ParenteNaoAssociadoAoEnderecoException;
import rm349040.techchallenge2.domain.exception.ParenteNaoAssociadoException;
import rm349040.techchallenge2.domain.exception.UsuarioCaseInsensitivePropertyException;
import rm349040.techchallenge2.domain.exception.UsuarioInexistenteException;
import rm349040.techchallenge2.domain.exception.UsuarioJaNoRepositorioException;
import rm349040.techchallenge2.domain.exception.UsuarioNaoAssociadoAoEnderecoException;
import rm349040.techchallenge2.domain.model.Eletrodomestico;
import rm349040.techchallenge2.domain.model.Endereco;
import rm349040.techchallenge2.domain.model.Parente;
import rm349040.techchallenge2.domain.model.Usuario;
import rm349040.techchallenge2.domain.repository.EletrodomesticoRepository;
import rm349040.techchallenge2.domain.repository.EnderecoRepository;
import rm349040.techchallenge2.domain.repository.ParenteRepository;
import rm349040.techchallenge2.domain.repository.UsuarioRepository;
import rm349040.techchallenge2.domain.service.CRUD;

public class SystemValidador {

	private UsuarioRepository usuarioRepository;
	private ParenteRepository parenteRepository;
	private EnderecoRepository endererecoRepository;
	private EletrodomesticoRepository eletrodomesticoRepository ;


	private Usuario usuarioJaExistente = null;
	private Endereco enderecoJaExistente = null;
	private Parente parenteJaExistente = null;
	private Eletrodomestico eletrodomesticoJaExistente = null;

	private CRUD acao = CRUD.LISTAR;
	private Class type;

	public void validateCreateUsuario(Usuario usuario) {

		checkUsuarioNulidades(usuario);

		if(jaTemAlgumUsuarioIgualAEste(usuario)) {
			throw new UsuarioJaNoRepositorioException(
					String.format("%s USUÁRIO : O usuário %s já está no repositório"
							,CRUD.CREATE_FAIL.getAction()
							, usuarioJaExistente));
		}
	}


	public void validateCreateUsuarioEmEndereco(Usuario usuario, String exceptionMessage) {
		if (usuario == null)
			throw new NullException(exceptionMessage);

		if (usuario.getId() == null)
			throw new NullException("O id do usuário não pode ser nulo. Corrija e tente novamente.");

		if (!usuarioEstaNoRepositorio(usuario)) {
			throw new UsuarioInexistenteException(String.format("%s ENDEREÇO: "
					+ "O usuário (id=%s) a se associar ao endereço não está no repositório. "
					+ "Crie o usuário e tente novamente."
					,CRUD.CREATE_FAIL.getAction()
					, usuario.getId()));
		}

	}

	public void validateUpdateUsuario(Usuario usuario) {
		if (usuario == null)
			throw new NullException("O usuário não pode ser nulo. Corrija e tente novamente.");

		if (usuario.getId() == null)
			throw new NullException("O id do usuário sendo editado não pode ser nulo. Corrija e tente novamente.");

		checkUsuarioNulidades(usuario);

		acao = CRUD.UPDATE;

		Optional.of(usuarioRepository.
				 queryById(usuario.getId())).get().orElseThrow(() ->  {return entityNotFoundException(Usuario.class,usuario.getId()); });

		Collection<Endereco> enderecosNoRepositorio = endererecoRepository.findByUsuario(usuario);

		if (!enderecosSaoConsistentes(usuario.getMeusEnderecos(), enderecosNoRepositorio)) {
			throw new DomainException(String.format(
					"%s USUÁRIO : " + "INCONSISTÊNCIA NOS ENDEREÇOS ASSOCIADOS AO USUÁRIO (id=%s) : "
							+ "TORNE OS ENDERECOS DO USUARIO IDÊNTICOS AOS DO REPOSITORIO DE ENDEREÇOS"
							,CRUD.UPDATE_FAIL.getAction()
							,usuario.getId()));
		}

	}
	
	public Usuario validateUpdateFacilUsuario(Usuario usuario) {
		
		// CHECK-IN DE USUARIO
		checkUsuarioNulidades(usuario);
		
		if(jaTemAlgumUsuarioIgualAEste(usuario) && ! usuario.getId().equals(usuarioJaExistente.getId())) {
			throw new UsuarioJaNoRepositorioException(
					String.format("%s USUÁRIO : O usuário %s já está no repositório"
							, CRUD.UPDATE_FAIL.getAction()
							, usuarioJaExistente));
		}
		
		if (usuario.getId() == null)
			throw new NullException("O id do usuário sendo editado não pode ser nulo. Corrija e tente novamente.");

		acao = CRUD.UPDATE;
				
		Usuario e = usuarioRepository.queryById(
							usuario.getId())
							.orElseThrow(
									() -> entityNotFoundException(Usuario.class, usuario.getId()));
		
		return e;
	}

	public Usuario validateDeleteUsuario(Long usuarioId) {

		try {

            acao = CRUD.DELETE;

    		return usuarioRepository.queryById(usuarioId).orElseThrow(() ->   entityNotFoundException(Usuario.class,usuarioId));

        }catch (NullPointerException e){

            throw new IdNullException(e.getMessage());
        }

	}

	public void validateDeleteUsuario(Usuario usuario) {

		if (usuario == null)
			throw new NullException("O usuário não pode ser nulo. Corrija e tente novamente.");

		validateDeleteUsuario(usuario.getId());

	}

	//TODO refatorar validateCreateEndereco, validateCreateParente, validateCreateEletrodomestico para um médodo comum para evitar TANTO código PARECIDO
	//TODO refatorar validateUpdateEndereco, validateUpdateParente, validateUpdateEletrodomestico para um médodo comum para evitar TANTO código PARECIDO
	//TODO refatorar validateDeleteEndereco, validateDeleteParente, validateDeleteEletrodomestico para um médodo comum para evitar TANTO código PARECIDO
	public void validateCreateEndereco(Usuario usuario, Endereco endereco) {

		if (usuario == null)
			throw new NullException("O usuário a ser relacionado ao endereco não pode ser nulo. Corrija e tente novamente.");

		if (usuario.getId() == null)
			throw new NullException("O id do usuário não pode ser nulo. Corrija e tente novamente.");

		if (!usuarioEstaNoRepositorio(usuario)) {
			throw new UsuarioInexistenteException(String.format("%s ENDEREÇO: "
					+ "O usuário (id=%s) a se associar ao endereço não está no repositório. "
					+ "Crie o usuário e tente novamente."
					,CRUD.CREATE_FAIL.getAction()
					, usuario.getId()));
		}

		// CHECK-IN DE ENDEREÇO
		checkEnderecoNulidades(endereco);

		// Checa se já tem algum endereco igual a este no repositório.
		// Se sim, OU usuário está tentando RE-associar este endereçõ a ele próprio
		// (caso em que o usuário do endereço é o usuário solicitador da associacao)
		// OU, o usuário está tentando associar a si mesmo um endereço JÁ ASSOCIDADO a
		// outro usuário... PENALIDADE MÁXIMA. TOMA EXCEPTION AÍ, SAFADO...*
		if (jaTemAlgumEnderecoIgualAEste(endereco)) {

			// Tentativa INVÁLIDA de associar a este usuario um endereco que já associado a
			// ele próprio
			if (enderecoExistenteJaAssociadoAEsteUsuario(usuario)) {

				// Tentativa INVÁLIDA de RE-associar a este usuario (NOVAMENTE) o mesmo endereco
				// que já estava associado a ele mesmo
				throw new EnderecoJaAssociadoAoUsuarioException(String.format(
						"%s ENDEREÇO : RE-ASSOCIACAO NAO PERMITIDA : O endereço (%s) já está associado ao usuário id=%s - %s"
						,CRUD.CREATE_FAIL.getAction()
						,enderecoJaExistente
						,enderecoJaExistente.getUsuario().getId()
						,enderecoJaExistente.getUsuario().getNome()));

			} else {

				// Tentativa INVÁLIDA de associar a este usuario um endereco que já associado a
				// outro usuário
				throw new EnderecoJaAssociadoAOutroUsuarioExcepition(
						String.format("%s ENDEREÇO : ASSOCIACAO NAO PERMITIDA : O endereço já está associado ao usuário id=%s - %s"
								,CRUD.CREATE_FAIL.getAction()
								,enderecoJaExistente.getUsuario().getId()
								, enderecoJaExistente.getUsuario().getNome()));
			}

		}
		// FIM CHECK-IN DE ENDEREÇO
	}



	//TODO refatorar validateCreateEndereco, validateCreateParente, validateCreateEletrodomestico para um médodo comum para evitar TANTO código PARECIDO
	//TODO refatorar validateUpdateEndereco, validateUpdateParente, validateUpdateEletrodomestico para um médodo comum para evitar TANTO código PARECIDO
	//TODO refatorar validateDeleteEndereco, validateDeleteParente, validateDeleteEletrodomestico para um médodo comum para evitar TANTO código PARECIDO
	public void validateUpdateEndereco(Endereco endereco) {
		// CHECK-IN DE ENDEREÇO
		if (endereco == null)
			throw new NullException("O endereço a ser atualizado não pode ser nulo. Corrija e tente novamente.");

		if (endereco.getId() == null)
			throw new NullException("O id do endereço a se atualizar DEVE SER NÃO-NULO. Corrija e tente novamente.");

		checkEnderecoNulidades(endereco);

		if (endereco.getUsuario() == null)
			throw new NullException(
					"O usuário do endereço a ser atualizado não pode ser nulo. Corrija e tente novamente.");

		if (endereco.getUsuario().getId() == null)
			throw new NullException(String.format(
					"O id do usuário do endereço (%s) a se atualizar DEVE SER NÃO-NULO. Corrija e tente novamente."
					,endereco));


		if (!enderecoEstaNoRepositorio(endereco)) {
			throw new EnderecoNaoAssociadoException(String.format("%s ENDEREÇO : "
					+ "O endereço (%s) a se atualizar não está associado a nenhum usuário no repositório. "
					+ "Relacione o endereço (crie o endereço para um usuário) e tente novamente."
					,CRUD.UPDATE_FAIL.getAction()
					,endereco));
		}

		Optional<Usuario> usuario = usuarioRepository.queryById(endereco.getUsuario().getId());

		if (usuario.isEmpty()) {

			throw new UsuarioNaoAssociadoAoEnderecoException(String.format("%s ENDEREÇO : "
					+ "O usuário (id=%s) do endereço (%s) a se atualizar não está associado ao endereço, no repositório. "
					+ "Relacione os dois a e tente novamente."
					,CRUD.UPDATE_FAIL.getAction()
					,endereco.getUsuario().getId()
					,endereco));
		} else {

			Collection<Endereco> enderecosDesteUsuario = endererecoRepository.findByUsuario(usuario.get());
			Collection idsEnderecosDesteUsuario = enderecosDesteUsuario.stream().map(e->e.getId()).collect(Collectors.toList());
			if (!idsEnderecosDesteUsuario.contains(endereco.getId())) {
					throw new EnderecoNaoAssociadoException(String.format(
						"%s ENDEREÇO : O endereço (%s) a se atualizar não está associado a este usuário (id=%s), no repositório."
						+ "Relacione o endereço (crie o endereço para um usuário) e tente novamente."
						,CRUD.UPDATE_FAIL.getAction()
						,endereco
						,endereco.getUsuario().getId()));
			}

		}// FIM CHECK-IN DE ENDEREÇO
	}

	public Endereco validateUpdateFacilEndereco(Endereco endereco) {
		// CHECK-IN DE ENDEREÇO
		acao = CRUD.UPDATE;

		if (endereco == null)
			throw new NullException("O endereco a ser atualizado não pode ser nulo. Corrija e tente novamente.");

		if (endereco.getId() == null)
			throw new NullException("O id do endereco a ser atualizado DEVE SER NÃO-NULO. Corrija e tente novamente.");

		checkEnderecoNulidades(endereco);
		
		if(jaTemAlgumEnderecoIgualAEste(endereco) && !endereco.getId().equals(enderecoJaExistente.getId())) {
			throw new EnderecoJaNoRepositorioException(
					String.format("%s ENDEREÇO : O endereço %s já está no repositório"
							,CRUD.UPDATE_FAIL.getAction()
							,enderecoJaExistente));
		}

		Endereco e = endererecoRepository.findById(endereco.getId()).orElseThrow(() -> entityNotFoundException(Endereco.class, endereco.getId()));

		return e;
	}


	//TODO refatorar validateCreateEndereco, validateCreateParente, validateCreateEletrodomestico para um médodo comum para evitar TANTO código PARECIDO
	//TODO refatorar validateUpdateEndereco, validateUpdateParente, validateUpdateEletrodomestico para um médodo comum para evitar TANTO código PARECIDO
	//TODO refatorar validateDeleteEndereco, validateDeleteParente, validateDeleteEletrodomestico para um médodo comum para evitar TANTO código PARECIDO
	public void validateDeleteEndereco(Endereco endereco) {

		// CHECK-OUT DE ENDEREÇO
		if (endereco == null)
			throw new NullException("O endereço a ser removido não pode ser nulo. Corrija e tente novamente.");

		if (endereco.getId() == null)
			throw new NullException("O id do endereço a se removido DEVE SER NÃO-NULO. Corrija e tente novamente.");

		if (endereco.getUsuario() == null)
			throw new NullException(
					"O usuário do endereço a ser removido não pode ser nulo. Corrija e tente novamente.");

		if (endereco.getUsuario().getId() == null)
			throw new NullException(
					"O id do usuário do endereço a se remover DEVE SER NÃO-NULO. Corrija e tente novamente.");

		if (!enderecoEstaNoRepositorio(endereco)) {
			throw new EnderecoNaoAssociadoException(String.format("%s ENDEREÇO: "
					+ "O endereço (%s) a se remover não está associado a nenhum usuário no repositório. "
					+ "Relacione o endereço (crie o endereço para um usuário) e tente novamente."
					,CRUD.DELETE_FAIL.getAction()
					,endereco));
		}

		Optional<Usuario> usuario = usuarioRepository.queryById(endereco.getUsuario().getId());

		if (usuario.isEmpty()) {
			throw new UsuarioNaoAssociadoAoEnderecoException(String.format("%s ENDEREÇO: "
					+ "O usuário (id=%s) do endereço (%s) a se remover não está associado ao endereço, no repositório. "
					+ "Relacione os dois a e tente novamente."
					,CRUD.DELETE_FAIL.getAction()
					,endereco.getUsuario().getId()
					,endereco));
		} else {

			Collection<Endereco> enderecosDesteUsuario = endererecoRepository.findByUsuario(usuario.get());
			if (!enderecosDesteUsuario.contains(endereco)) {
				throw new EnderecoNaoAssociadoException(String.format("%s ENDEREÇO: "
						+ "O endereço (%s) a se remover não está associado a este usuário (id=%s), no repositório."
						+ "Relacione o endereço (crie o endereço para um usuário) e tente novamente."
						,CRUD.DELETE_FAIL.getAction()
						,endereco
						,endereco.getUsuario().getId()));
		}

		} // FIM CHECK-OUT DE ENDEREÇO

	}

	public Endereco validateDeleteEndereco(Long enderecoId) {

		// CHECK-OUT DE ENDEREÇO

		try {

            acao = CRUD.DELETE;

    		return endererecoRepository.findById(enderecoId).orElseThrow(() ->   entityNotFoundException(Endereco.class,enderecoId));

        }catch (NullPointerException e){

            throw new IdNullException(e.getMessage());
        }

		// FIM CHECK-OUT DE ENDEREÇO

	}

	//TODO refatorar validateCreateEndereco, validateCreateParente, validateCreateEletrodomestico para um médodo comum para evitar TANTO código PARECIDO
	//TODO refatorar validateUpdateEndereco, validateUpdateParente, validateUpdateEletrodomestico para um médodo comum para evitar TANTO código PARECIDO
	//TODO refatorar validateDeleteEndereco, validateDeleteParente, validateDeleteEletrodomestico para um médodo comum para evitar TANTO código PARECIDO
	public void validateCreateParente(Parente parente) {

		//CHECK IN PARENTE

		if (parente == null)
			throw new NullException(
					"O parente a ser relacionado ao endereço do usuário não pode ser nulo. Corrija e tente novamente.");

		Endereco endereco = parente.getEndereco();

		if (endereco == null)
			throw new NullException(
					"O endereço a ser relacionado a parente não pode ser nulo. Corrija e tente novamente.");

		if (endereco.getId() == null)
			throw new NullException(
					"O id do endereço a ser relacionado a parente DEVE SER NÃO-NULO. Corrija e tente novamente.");

		if (endereco.getUsuario() == null)
			throw new NullException(
					"O usuário do endereço a ser relacionado a parente não pode ser nulo. Corrija e tente novamente.");

		if (endereco.getUsuario().getId() == null)
			throw new NullException(
					"O id do usuário do endereço a ser relacionado a parente DEVE SER NÃO-NULO. Corrija e tente novamente.");

		if (!enderecoEstaNoRepositorio(endereco)) {
			throw new EnderecoNaoAssociadoException(String.format("%s PARENTE : "
					+ "O endereço (%s) a ser relacionado a parente não está associado a nenhum usuário no repositório. "
					+ "Relacione o endereço (crie o endereço para um usuário) e tente novamente."
					,CRUD.CREATE_FAIL.getAction()
					, endereco));
		}

		if (!usuarioEstaNoRepositorio(endereco.getUsuario())) {
			throw new UsuarioNaoAssociadoAoEnderecoException(String.format("%s PARENTE : "
					+ "O usuário (id=%s) do endereço a ser relacionado a parente não está associado ao endereço (%s), no repositório. "
					+ "Relacione os dois a e tente novamente."
					,CRUD.CREATE_FAIL.getAction()
					, endereco.getUsuario().getId()
					,endereco));
		} else {

			Collection<Endereco> enderecosDesteUsuario = endererecoRepository.findByUsuario(endereco.getUsuario());
			Collection idsEnderecosDesteUsuario = enderecosDesteUsuario.stream().map(e->e.getId()).collect(Collectors.toList());
			if (!idsEnderecosDesteUsuario.contains(endereco.getId())) {
				throw new EnderecoNaoAssociadoException(String.format("%s PARENTE : "
						+ "O endereço (%s) a ser relacionado a parente não está associado a este usuário (id=%s), no repositório. "
						+ "Relacione o endereço (crie o endereço para um usuário) e tente novamente."
						,CRUD.CREATE_FAIL.getAction()
						,endereco
						,endereco.getUsuario().getId()));
			}
		}

		checkParenteNulidades(parente);


		// Checa se já tem algum parente igual a este no repositório.
		// Se sim, OU usuário está tentando RE-associar este parente ao endereço
		// OU, o usuário está tentando associar a si mesmo, neste endereço, um parente
		// JÁ ASSOCIDADO a outro usuário... PENALIDADE MÁXIMA. TOMA EXCEPTION AÍ,
		// SAFADO...
		if (jaTemAlgumParenteIgualAEste(parente)) {

			// Tentativa INVÁLIDA de associar a este usuario um endereco que já associado a

			// ele próprio
			if (parenteExistenteJaAssociadoAEsteEndereco(endereco)) {

				// Tentativa INVÁLIDA de RE-associar a este endereço (NOVAMENTE) o mesmo parente
				throw new ParenteJaAssociadoAEsteEnderecoException(String.format(
						"RE-ASSOCIACAO NAO PERMITIDA : %s PARENTE : "
								+ "O parente (id=%s) já está associado a este endereço (id=%s) do usuário id=%s - %s",
						CRUD.CREATE_FAIL.getAction(),
						parenteJaExistente.getId(),
						parenteJaExistente.getEndereco().getId(),
						parenteJaExistente.getEndereco().getUsuario().getId(),
						parenteJaExistente.getEndereco().getUsuario().getNome()));

			} else {

				// Tentativa INVÁLIDA de associar a este endereco um parente que já está
				// associado a outro endereço

				Endereco enderecoCorreto = parenteRepository.findById(parenteJaExistente.getId()).get().getEndereco();

				throw new ParenteJaAssociadoAOutroEnderecoException(String.format(
						"%s PARENTE : ASSOCIACAO NAO PERMITIDA : associação de parente (id=%s) ao endereco (id=%s) : FALHA AO CRIAR PARENTE : "
								+ "O parente (id=%s) já está associado ao endereço (id=%s) do usuário id=%s - %s",
								CRUD.CREATE_FAIL.getAction(),
								parenteJaExistente.getId(),
								endereco.getId(),
								parenteJaExistente.getId()
								,enderecoCorreto.getId()
								,enderecoCorreto.getUsuario().getId(),
								enderecoCorreto.getUsuario().getNome()));
			}

		}//FIM CHECK IN PARENTE

	}


	//TODO refatorar validateCreateEndereco, validateCreateParente, validateCreateEletrodomestico para um médodo comum para evitar TANTO código PARECIDO
	//TODO refatorar validateUpdateEndereco, validateUpdateParente, validateUpdateEletrodomestico para um médodo comum para evitar TANTO código PARECIDO
	//TODO refatorar validateDeleteEndereco, validateDeleteParente, validateDeleteEletrodomestico para um médodo comum para evitar TANTO código PARECIDO
	public void validateUpdateParente(Parente parente) {

		if (parente == null)
			throw new NullException("O parente a ser atualizado não pode ser nulo. Corrija e tente novamente.");

		if (parente.getId() == null)
			throw new NullException("O id do parente a ser atualizado DEVE SER NÃO-NULO. Corrija e tente novamente.");

		Endereco endereco = parente.getEndereco();

		if (endereco == null)
			throw new NullException(
					"O parente sendo atualizado não pode ter endereço nulo. Corrija e tente novamente.");

		if (endereco.getId() == null)
			throw new NullException(
					"O parente sendo atualizado nao pode ter o id do seu endereço NULO. Corrija e tente novamente.");

		if (endereco.getUsuario() == null)
			throw new NullException(
					"O parente sendo atualizado nao pode ter o usuário associado a ele NULO. Corrija e tente novamente.");

		if (endereco.getUsuario().getId() == null)
			throw new NullException(
					"O parente sendo atualizado nao pode ter o id do usuário associado a ele NULO. Corrija e tente novamente.");

		if (!enderecoEstaNoRepositorio(endereco)) {
			throw new EnderecoNaoAssociadoException(String.format(
					"%s PARENTE : "
							+ "O endereço (%s) a ser relacionado ao parente (id=%s) não está no repositório. "
							+ "Relacione o endereço (crie o endereço para este parente) e tente novamente."
							,CRUD.UPDATE_FAIL.getAction()
							,endereco
							, parente.getId()));
		}

		checkParenteNulidades(parente);

		if (!usuarioEstaNoRepositorio(endereco.getUsuario())) {
			throw new UsuarioNaoAssociadoAoEnderecoException(String.format("%s PARENTE : "
					+ "O usuário (id=%s) do endereço (id=%s) a ser relacionado a parente (id=%s) não está associado ao endereço, no repositório. "
					+ "Relacione os dois a e tente novamente."
					,CRUD.UPDATE_FAIL.getAction()
					,endereco.getUsuario().getId()
					,endereco.getId()
					,parente.getId()));
		} else {

			Collection<Endereco> enderecosDesteUsuario = endererecoRepository.findByUsuario(endereco.getUsuario());
			Collection idsEnderecosDesteUsuario = enderecosDesteUsuario.stream().map(e->e.getId()).collect(Collectors.toList());
			if (!idsEnderecosDesteUsuario.contains(endereco.getId())) {
				throw new EnderecoNaoAssociadoException(String.format("%s PARENTE : "
						+ "O endereço (%s) a ser relacionado a parente (id=%s) não está associado a este usuário (id=%s), no repositório. "
						+ "Relacione o endereço (crie o endereço para um usuário) e tente novamente."
						,CRUD.UPDATE_FAIL.getAction()
						,endereco
						,parente.getId()
						,endereco.getUsuario().getId()));
			}

			Collection<Parente> parentesDesteUsuario = parenteRepository.findByEndereco(endereco);
			if (!parentesDesteUsuario.contains(parente)) {
				throw new ParenteNaoAssociadoException(String.format("%s PARENTE : "
						+ "o parente (id=%s) NÃO está relacionado ao endereco (%s), no repositório. "
						+ "Relacione-os e tente novamente"
						,CRUD.UPDATE_FAIL.getAction()
						, parente.getId()
						, endereco));
			}
		}

	}

	public Parente validateUpdateFacilParente(Parente parente) {

			acao = CRUD.UPDATE;

			if (parente == null)
				throw new NullException("O parente a ser atualizado não pode ser nulo. Corrija e tente novamente.");

			if (parente.getId() == null)
				throw new NullException("O id do parente a ser atualizado DEVE SER NÃO-NULO. Corrija e tente novamente.");

			checkParenteNulidades(parente);
			
			if(jaTemAlgumParenteIgualAEste(parente) && !parente.getId().equals(parenteJaExistente.getId())) {
				
				throw new ParenteJaNoRepositorioException(
						String.format("%s PARENTE : O parente %s já está no repositório"
								,CRUD.UPDATE_FAIL.getAction()
								,parenteJaExistente));
				
			}

			Parente p = parenteRepository.findById(parente.getId()).orElseThrow(() -> entityNotFoundException(Parente.class, parente.getId()));

			return p;

	}

	//TODO refatorar validateCreateEndereco, validateCreateParente, validateCreateEletrodomestico para um médodo comum para evitar TANTO código PARECIDO
	//TODO refatorar validateUpdateEndereco, validateUpdateParente, validateUpdateEletrodomestico para um médodo comum para evitar TANTO código PARECIDO
	//TODO refatorar validateDeleteEndereco, validateDeleteParente, validateDeleteEletrodomestico para um médodo comum para evitar TANTO código PARECIDO
	public void validateDeleteParente(Parente parente) {

		// CHECK-OUT DE PARENTE
		if (parente == null)
			throw new NullException("O parente a ser removido não pode ser nulo. Corrija e tente novamente.");

		if (parente.getId() == null)
			throw new NullException("O id do parente a ser removido DEVE SER NÃO-NULO. Corrija e tente novamente.");

		Endereco endereco = parente.getEndereco();

		if (endereco == null)
			throw new NullException(String.format("O endereço do parente (id=%s) a ser removido não pode ser nulo. Corrija e tente novamente."
					,parente.getId()));

		if (endereco.getId() == null)
			throw new NullException(String.format(
					"O id do endereço do parente (id=%s) a se remover DEVE SER NÃO-NULO. Corrija e tente novamente.",parente.getId()));


		if (endereco.getUsuario() == null)
			throw new NullException(String.format(
					"O parente (id=%s) sendo removido nao pode ter o usuário associado a ele NULO. Corrija e tente novamente.",parente.getId()));

		if (endereco.getUsuario().getId() == null)
			throw new NullException(String.format(
					"O parente (id=%s) sendo removido nao pode ter o id do usuário associado a ele NULO. Corrija e tente novamente.",parente.getId()));

		if (!enderecoEstaNoRepositorio(endereco)) {
			throw new EnderecoNaoAssociadoException(String.format(
					"%s PARENTE : "
							+ "O endereço (%s) a ser relacionado ao parente (id=%s) não está no repositório. "
							+ "Relacione o endereço (crie o endereço para este parente) e tente novamente."
							,CRUD.DELETE_FAIL.getAction()
							,endereco
							, parente.getId()));
		}

		if (!usuarioEstaNoRepositorio(endereco.getUsuario())) {
			throw new UsuarioNaoAssociadoAoEnderecoException(String.format("%s PARENTE : "
					+ "O usuário (id=%s) do endereço (%s) a ser relacionado a parente (id=%s) não está associado ao endereço, no repositório. "
					+ "Relacione os dois a e tente novamente."
					,CRUD.DELETE_FAIL.getAction()
					,endereco.getUsuario().getId()
					,endereco
					,parente.getId()));
		} else {

			Collection<Endereco> enderecosDesteUsuario = endererecoRepository.findByUsuario(endereco.getUsuario());
			if (!enderecosDesteUsuario.contains(endereco)) {
				throw new EnderecoNaoAssociadoException(String.format("%s PARENTE : "
						+ "O endereço (%s) a ser relacionado a parente (id=%s) não está associado a este usuário (id=%s), no repositório. "
						+ "Relacione o endereço (crie o endereço para um usuário) e tente novamente."
						,CRUD.DELETE_FAIL.getAction()
						,endereco
						,parente.getId()
						,endereco.getUsuario().getId()));
			}

			Collection<Parente> parentesDesteUsuario = parenteRepository.findByEndereco(endereco);
			if (!parentesDesteUsuario.contains(parente)) {
				throw new ParenteNaoAssociadoException(String.format("%s PARENTE : "
						+ "o parente (id=%s) NÃO está relacionado ao endereco (%s), no repositório. "
						+ "Relacione-os e tente novamente"
						,CRUD.DELETE_FAIL.getAction()
						,parente.getId()
						,endereco));
			}
		}

		if (!parenteEstaNoRepositorio(parente)) {
			throw new ParenteNaoAssociadoException(CRUD.DELETE_FAIL.getAction()+" PARENTE	: "
					+ "O parente a se remover não está associado a nenhum endereço no repositório. "
					+ "Relacione o parente (crie o parente para um endereço) e tente novamente.");
		}

		Optional<Endereco> endereCo = endererecoRepository.findById(parente.getEndereco().getId());

		if (endereCo.isEmpty()) {
			throw new ParenteNaoAssociadoAoEnderecoException(String.format("%s PARENTE: "
					+ "O endereço do parente a se remover não está associado ao parente (id=%s), no repositório. "
					+ "Relacione os dois a e tente novamente."
					,CRUD.DELETE_FAIL.getAction()
					,parente.getId()));
		} else {

			Collection<Parente> parentesNesteEndereco = parenteRepository.findByEndereco(endereCo.get());
			if (!parentesNesteEndereco.contains(parente)) {
				throw new ParenteNaoAssociadoAoEnderecoException(String.format("%s PARENTE: "
						+ "O parente (id=%s) a se remover não está associado a este endereço (%s), no repositório. "
						+ "Relacione o parente (crie o parente para um endereço) e tente novamente."
						,CRUD.DELETE_FAIL.getAction()
						,parente.getId()
						,endereCo));
			}

		} // FIM CHECK-OUT DE PARENTE

	}

	public Parente validateDeleteParente(Long parenteId) {

		// CHECK-OUT DE PARENTE

		try {

            acao = CRUD.DELETE;

    		return parenteRepository.findById(parenteId).orElseThrow(() ->   entityNotFoundException(Parente.class,parenteId));

        }catch (NullPointerException e){

            throw new IdNullException(e.getMessage());
        }

		// FIM CHECK-OUT DE PARENTE

	}


	public Eletrodomestico validateDeleteEletrodomestico(Long eletrodomesticoId) {

		// CHECK-OUT DE ELETRODOMESTICO

		try {

            acao = CRUD.DELETE;

    		return eletrodomesticoRepository.findById(eletrodomesticoId).orElseThrow(() ->   entityNotFoundException(Eletrodomestico.class,eletrodomesticoId));

        }catch (NullPointerException e){

            throw new IdNullException(e.getMessage());
        }

		// FIM CHECK-OUT DE ELETRODOMESTICO

	}

	//TODO refatorar validateCreateEndereco, validateCreateParente, validateCreateEletrodomestico para um médodo comum para evitar TANTO código PARECIDO
	//TODO refatorar validateUpdateEndereco, validateUpdateParente, validateUpdateEletrodomestico para um médodo comum para evitar TANTO código PARECIDO
	//TODO refatorar validateDeleteEndereco, validateDeleteParente, validateDeleteEletrodomestico para um médodo comum para evitar TANTO código PARECIDO
	public void validateCreateEletrodomestico(Eletrodomestico eletrodomestico) {

			    //CHECK-IN ELETRODOMESTICO

				checkEletrodomesticoNulidades(eletrodomestico);

				Endereco endereco = eletrodomestico.getEndereco();

				if (endereco == null)
					throw new NullException(
							"O endereço a ser relacionado a eletrodomestico não pode ser nulo. Corrija e tente novamente.");

				if (endereco.getId() == null)
					throw new NullException(
							"O id do endereço a ser relacionado a eletrodomestico DEVE SER NÃO-NULO. Corrija e tente novamente.");

				if (endereco.getUsuario() == null)
					throw new NullException(
							"O usuário do endereço a ser relacionado a eletrodomestico não pode ser nulo. Corrija e tente novamente.");

				if (endereco.getUsuario().getId() == null)
					throw new NullException(
							"O id do usuário do endereço a ser relacionado a eletrodomestico DEVE SER NÃO-NULO. Corrija e tente novamente.");

				if (!enderecoEstaNoRepositorio(endereco)) {
					throw new EnderecoNaoAssociadoException(String.format("%s ELETRODOMESTICO : "
							+ "O endereço (id=%s) a ser relacionado a eletrodomestico não está associado a nenhum usuário no repositório. "
							+ "Relacione o endereço (crie o endereço para um usuário) e tente novamente."
							,CRUD.CREATE_FAIL.getAction()
							,endereco.getId()));
				}

				if (!usuarioEstaNoRepositorio(endereco.getUsuario())) {
					throw new UsuarioNaoAssociadoAoEnderecoException(String.format("%s ELETRODOMESTICO : "
							+ "O usuário (id=%s) do endereço a ser relacionado a eletrodomestico não está associado ao endereço (id=%s), no repositório. "
							+ "Relacione os dois a e tente novamente."
							,CRUD.CREATE_FAIL.getAction()
							,endereco.getUsuario().getId()
							,endereco.getId()));
				} else {

					Collection<Endereco> enderecosDesteUsuario = endererecoRepository.findByUsuario(endereco.getUsuario());
					Collection idsEnderecosDesteUsuario = enderecosDesteUsuario.stream().map(e->e.getId()).collect(Collectors.toList());
					if (!idsEnderecosDesteUsuario.contains(endereco.getId())) {
						throw new EnderecoNaoAssociadoException(String.format("%s ELETRODOMESTICO : "
								+ "O endereço (%s) a ser relacionado a eletrodomestico não está associado a este usuário (id=%s), no repositório. "
								+ "Relacione o endereço (crie o endereço para um usuário) e tente novamente."
								,CRUD.CREATE_FAIL.getAction()
								,endereco
								,endereco.getUsuario().getId()));
					}
				}

				// Checa se já tem algum eletrodomestico igual a este no repositório.
				// Se sim, OU usuário está tentando RE-associar este eletrodomestico ao endereço
				// OU, o usuário está tentando associar a si mesmo, neste endereço, um eletrodomestico
				// JÁ ASSOCIDADO a outro usuário... PENALIDADE MÁXIMA. TOMA EXCEPTION AÍ,
				// SAFADO...
				if (jaTemAlgumEletrodomesticoIgualAEste(eletrodomestico)) {

					// Tentativa INVÁLIDA de associar a este endereco um eletrodomestico que já associado a
					// ele próprio
					if (eletrodomesticoExistenteJaAssociadoAEsteEndereco(endereco)) {

						// Tentativa INVÁLIDA de RE-associar a este endereço (NOVAMENTE) o mesmo eletrodomestico
						throw new EletrodomesticoJaAssociadoAEsteEnderecoException(String.format(
								"RE-ASSOCIACAO NAO PERMITIDA : %s ELETRODOMESTICO : "
										+ "O eletrodomestico (id=%s) já está associado a este endereço (id=%s) do usuário id=%s - %s"
										,CRUD.CREATE_FAIL.getAction()
										,eletrodomestico.getId()==null?"null":eletrodomestico.getId()
										,endereco.getId()
										,endereco.getUsuario().getId()
										,endereco.getUsuario().getNome()));

					} else {

						// Tentativa INVÁLIDA de associar a este endereco um eletrodomestico que já está
						// associado a outro endereço

						Endereco enderecoCorreto = eletrodomesticoRepository.findById(eletrodomestico.getId()).get().getEndereco();

						throw new EletrodomesticoJaAssociadoAOutroEnderecoException(String.format(
								"ASSOCIACAO NAO PERMITIDA : associação de eletrodomestico  (id=%s) ao endereco  (id=%s) : %s ELETRODOMESTICO : "
										+ "O eletrodomestico (id=%s) já está associado ao endereço (id=%s) do usuário id=%s - %s",
												eletrodomestico.getId()==null?"null":eletrodomestico.getId(),
												endereco.getId(),
												CRUD.CREATE_FAIL.getAction(),
												eletrodomestico.getId()==null?"null":eletrodomestico.getId(),
												enderecoCorreto.getId(),
												enderecoCorreto.getUsuario().getId(),
												enderecoCorreto.getUsuario().getNome()));
					}

				}//FIM CHECK-IN ELETRODOMESTICO
	}



	//TODO refatorar validateCreateEndereco, validateCreateParente, validateCreateEletrodomestico para um médodo comum para evitar TANTO código PARECIDO
	//TODO refatorar validateUpdateEndereco, validateUpdateParente, validateUpdateEletrodomestico para um médodo comum para evitar TANTO código PARECIDO
	//TODO refatorar validateDeleteEndereco, validateDeleteParente, validateDeleteEletrodomestico para um médodo comum para evitar TANTO código PARECIDO
	public void validateUpdateEletrodomestico(Eletrodomestico eletrodomestico) {

		if (eletrodomestico == null)
			throw new NullException("O eletrodomestico a ser atualizado não pode ser nulo. Corrija e tente novamente.");

		if (eletrodomestico.getId() == null)
			throw new NullException("O id do eletrodomestico a ser atualizado DEVE SER NÃO-NULO. Corrija e tente novamente.");

		checkEletrodomesticoNulidades(eletrodomestico);

		Endereco endereco = eletrodomestico.getEndereco();

		if (endereco == null)
			throw new NullException(
					"O eletrodomestico sendo atualizado não pode ter endereço nulo. Corrija e tente novamente.");

		if (endereco.getId() == null)
			throw new NullException(
					"O eletrodomestico sendo atualizado nao pode ter o id do seu endereço NULO. Corrija e tente novamente.");

		if (endereco.getUsuario() == null)
			throw new NullException(
					"O eletrodomestico sendo atualizado nao pode ter o usuário associado a ele NULO. Corrija e tente novamente.");

		if (endereco.getUsuario().getId() == null)
			throw new NullException(
					"O eletrodomestico sendo atualizado nao pode ter o id do usuário associado a ele NULO. Corrija e tente novamente.");

		if (!enderecoEstaNoRepositorio(endereco)) {
			throw new EnderecoNaoAssociadoException(String.format(
					"%s ELETRODOMESTICO : "
							+ "O endereço (%s) a ser relacionado ao eletrodomestico (id=%s) não está no repositório. "
							+ "Relacione o endereço (crie o endereço para este eletrodomestico) e tente novamente."
							,CRUD.UPDATE_FAIL.getAction()
							,endereco
							, eletrodomestico.getId()));
		}

		if (!usuarioEstaNoRepositorio(endereco.getUsuario())) {
			throw new UsuarioNaoAssociadoAoEnderecoException(String.format("%s ELETRODOMESTICO : "
					+ "O usuário (id=%s) do endereço (%s) a ser relacionado a eletrodomestico (id=%s) não está associado ao endereço, no repositório. "
					+ "Relacione os dois a e tente novamente."
					,CRUD.UPDATE_FAIL.getAction()
					, endereco.getUsuario().getId()
					, endereco
					,eletrodomestico.getId()));
		} else {

			Collection<Endereco> enderecosDesteUsuario = endererecoRepository.findByUsuario(endereco.getUsuario());
			Collection idsEnderecosDesteUsuario = enderecosDesteUsuario.stream().map(e->e.getId()).collect(Collectors.toList());
			if (!idsEnderecosDesteUsuario.contains(endereco.getId())) {
				throw new EnderecoNaoAssociadoException(String.format("%s ELETRODOMESTICO : "
						+ "O endereço (%s) a ser relacionado a eletrodomestico (id=%s) não está associado a este usuário (id=%s), no repositório. "
						+ "Relacione o endereço (crie o endereço para um usuário) e tente novamente."
						,CRUD.UPDATE_FAIL.getAction()
						,endereco
						,eletrodomestico.getId()
						,endereco.getUsuario().getId()));
			}

			Collection<Eletrodomestico> eletrodomesticosDesteUsuario = eletrodomesticoRepository.findByEndereco(endereco);
			if (!eletrodomesticosDesteUsuario.contains(eletrodomestico)) {
				throw new EletrodomesticoNaoAssociadoException(String.format("%s ELETRODOMESTICO : "
						+ "o eletrodomestico (id=%s) NÃO está relacionado ao endereco (%s), no repositório. "
						+ "Relacione-os e tente novamente"
						,CRUD.UPDATE_FAIL.getAction()
						, eletrodomestico.getId()
						, endereco));
			}
		}


	}

	/**
	 * Faz várias validações de atualização.
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
	public Eletrodomestico validateUpdateFacilEletrodomestico(Eletrodomestico eletrodomestico) {

		acao = CRUD.UPDATE;

		if (eletrodomestico == null)
			throw new NullException("O eletrodomestico a ser atualizado não pode ser nulo. Corrija e tente novamente.");

		if (eletrodomestico.getId() == null)
			throw new NullException("O id do eletrodomestico a ser atualizado DEVE SER NÃO-NULO. Corrija e tente novamente.");

		checkEletrodomesticoNulidades(eletrodomestico);

		Eletrodomestico eletrodomesticoNoRepositorio = eletrodomesticoRepository
				.findById(eletrodomestico.getId())
				.orElseThrow(() -> entityNotFoundException(Eletrodomestico.class, eletrodomestico.getId()));

		Endereco endereco = eletrodomestico.getEndereco();

		if (endereco == null)
			throw new NullException(
					"O eletrodomestico sendo atualizado não pode ter endereço nulo. Corrija e tente novamente.");

		if (endereco.getId() == null)
			throw new NullException(
					"O eletrodomestico sendo atualizado nao pode ter o id do seu endereço NULO. Corrija e tente novamente.");

		if (!enderecoEstaNoRepositorio(endereco)) {
			throw new EnderecoNaoAssociadoException(String.format(
					"%s ELETRODOMESTICO : "
							+ "O endereço (%s) a ser relacionado ao eletrodomestico (id=%s) não está no repositório. "
							+ "Relacione o endereço (crie o endereço para este eletrodomestico) e tente novamente."
							,CRUD.UPDATE_FAIL.getAction()
							,endereco
							,eletrodomestico.getId()));
		}

		Endereco end = endererecoRepository.findById(endereco.getId()).get();
		Usuario usuario = end.getUsuario();

		if (! enderecoAssociadoAoMesmoUsuario(eletrodomesticoNoRepositorio, usuario) ) {
			throw new EnderecoNaoAssociadoAoMesmoUsuarioException(String.format("%s ELETRODOMESTICO : "
					+ "O usuário (id=%s) do endereço (%s) NÃO CADASTROU este eletrodoméstico (id=%s), no repositório. "
					,CRUD.UPDATE_FAIL.getAction()
					,usuario.getId()
					,endereco
					,eletrodomestico.getId()));
		}
		
		
		return eletrodomesticoNoRepositorio;

	}

	//TODO refatorar validateCreateEndereco, validateCreateParente, validateCreateEletrodomestico para um médodo comum para evitar TANTO código PARECIDO
	//TODO refatorar validateUpdateEndereco, validateUpdateParente, validateUpdateEletrodomestico para um médodo comum para evitar TANTO código PARECIDO
	//TODO refatorar validateDeleteEndereco, validateDeleteParente, validateDeleteEletrodomestico para um médodo comum para evitar TANTO código PARECIDO
	public void validateDeleteEletrodomestico(Eletrodomestico eletrodomestico) {

		// CHECK-OUT DE ELETRODOMESTICO
		if (eletrodomestico == null)
			throw new NullException("O eletrodomestico a ser removido não pode ser nulo. Corrija e tente novamente.");

		if (eletrodomestico.getId() == null)
			throw new NullException("O id do eletrodomestico a ser removido DEVE SER NÃO-NULO. Corrija e tente novamente.");

		Endereco endereco = eletrodomestico.getEndereco();

		if (endereco == null)
			throw new NullException(String.format("O endereço do eletrodomestico (id=%s) a ser removido não pode ser nulo. Corrija e tente novamente."
					,eletrodomestico.getId()));

		if (endereco.getId() == null)
			throw new NullException(String.format(
					"O id do endereço do eletrodomestico (id=%s) a se remover DEVE SER NÃO-NULO. Corrija e tente novamente.",eletrodomestico.getId()));


		if (endereco.getUsuario() == null)
			throw new NullException(String.format(
					"O eletrodomestico (id=%s) sendo removido nao pode ter o usuário associado a ele NULO. Corrija e tente novamente.",eletrodomestico.getId()));

		if (endereco.getUsuario().getId() == null)
			throw new NullException(String.format(
					"O eletrodomestico (id=%s) sendo removido nao pode ter o id do usuário associado a ele NULO. Corrija e tente novamente.",eletrodomestico.getId()));

		if (!enderecoEstaNoRepositorio(endereco)) {
			throw new EnderecoNaoAssociadoException(String.format(
					"%s ELETRODOMESTICO : "
							+ "O endereço (%s) a ser relacionado ao eletrodomestico (id=%s) não está no repositório. "
							+ "Relacione o endereço (crie o endereço para este eletrodomestico) e tente novamente."
							,CRUD.DELETE_FAIL.getAction()
							,endereco
							,eletrodomestico.getId()));
		}

		if (!usuarioEstaNoRepositorio(endereco.getUsuario())) {
			throw new UsuarioNaoAssociadoAoEnderecoException(String.format("%s ELETRODOMESTICO : "
					+ "O usuário (id=%s) do endereço (%s) a ser relacionado a eletrodomestico (id=%s) não está associado ao endereço, no repositório. "
					+ "Relacione os dois a e tente novamente."
					,CRUD.DELETE_FAIL.getAction()
					,endereco.getUsuario().getId()
					,endereco
					,eletrodomestico.getId()));
		} else {

			Collection<Endereco> enderecosDesteUsuario = endererecoRepository.findByUsuario(endereco.getUsuario());
			if (!enderecosDesteUsuario.contains(endereco)) {
				throw new EnderecoNaoAssociadoException(String.format("%s ELETRODOMESTICO : "
						+ "O endereço (%s) a ser relacionado a eletrodomestico (id=%s) não está associado a este usuário (id=%s), no repositório. "
						+ "Relacione o endereço (crie o endereço para um usuário) e tente novamente."
						,CRUD.DELETE_FAIL.getAction()
						,endereco
						,eletrodomestico.getId()
						,endereco.getUsuario().getId()));
			}

			Collection<Eletrodomestico> eletrodoemesticosDesteUsuario = eletrodomesticoRepository.findByEndereco(endereco);
			if (!eletrodoemesticosDesteUsuario.contains(eletrodomestico)) {
				throw new EletrodomesticoNaoAssociadoException(String.format("%s ELETRODOMESTICO : "
						+ "o eletrodomestico (id=%s) NÃO está relacionado ao endereco (%s), no repositório. "
						+ "Relacione-os e tente novamente"
						, CRUD.DELETE_FAIL.getAction()
						, eletrodomestico.getId()
						, endereco));
			}
		}

		if (!eletrodomesticoEstaNoRepositorio(eletrodomestico)) {
			throw new EletrodomesticoNaoAssociadoException(CRUD.DELETE_FAIL.getAction()+" ELETRODOMESTICO  : "
					+ "O eletrodomestico a se remover não está associado a nenhum endereço no repositório. "
					+ "Relacione o eletrodomestico (crie o eletrodomestico para um endereço) e tente novamente.");
		}

		Optional<Endereco> endereCo = endererecoRepository.findById(eletrodomestico.getEndereco().getId());

		if (endereCo.isEmpty()) {
			throw new EletrodomesticoNaoAssociadoAoEnderecoException(String.format("%s ELETRODOMESTICO: "
					+ "O endereço do eletrodomestico a se remover não está associado ao eletrodomestico (id=%s), no repositório. "
					+ "Relacione os dois a e tente novamente."
					, CRUD.DELETE_FAIL.getAction()
					, eletrodomestico.getId()));
		} else {

			Collection<Eletrodomestico> eletrodomesticosNesteEndereco = eletrodomesticoRepository.findByEndereco(endereCo.get());
			if (!eletrodomesticosNesteEndereco.contains(eletrodomestico)) {
				throw new EletrodomesticoNaoAssociadoAoEnderecoException(String.format("%s ELETRODOMESTICO: "
						+ "O eletrodomestico (id=%s) a se remover não está associado a este endereço (%s), no repositório. "
						+ "Relacione o eletrodomestico (crie o eletrodomestico para um endereço) e tente novamente."
						,CRUD.DELETE_FAIL.getAction()
						,eletrodomestico.getId()
						,endereCo));
			}

		} // FIM CHECK-OUT DE ELETRODOMESTICO
	}

	private boolean usuarioEstaNoRepositorio(Usuario usuario) {
		return usuarioRepository.queryById(usuario.getId()).isPresent();
	}

	private boolean enderecoEstaNoRepositorio(Endereco endereco) {
		Optional<Endereco> e = endererecoRepository.findById(endereco.getId());
		if(e.isPresent()) {
			enderecoJaExistente = e.get();
			return true;
		}
		return false;
	}

	private boolean parenteEstaNoRepositorio(Parente parente) {
		return parenteRepository.findById(parente.getId()).isPresent();
	}

	private boolean eletrodomesticoEstaNoRepositorio(Eletrodomestico eletrodomestico) {
		return eletrodomesticoRepository.findById(eletrodomestico.getId()).isPresent();
	}

	public boolean enderecosSaoConsistentes(Collection<Endereco> enderecoChecaveis,
			Collection<Endereco> enderecosNoRepositorio) {

		if(enderecoChecaveis == null && enderecosNoRepositorio == null) return true;


		if(enderecoChecaveis != null && enderecosNoRepositorio != null) {

			if(enderecoChecaveis.isEmpty() && enderecosNoRepositorio.isEmpty()) return true;

			Collection<Long> idsChecaveis = enderecoChecaveis.stream().
			 			    map(eChecavel -> eChecavel.getId()).collect(Collectors.toList());

			Collection<Long> idsNoRepositorio = enderecosNoRepositorio.stream().
					 			    map( e -> e.getId()).collect(Collectors.toList());

			return new HashSet<>(idsChecaveis).equals(new HashSet<>(idsNoRepositorio));

		}

		return false;

	}

	private boolean jaTemAlgumUsuarioIgualAEste(Usuario usuario) {

		Collection<Usuario> usuarios = usuarioRepository.findAll();

		Optional<Usuario> u = usuarios
				.stream()
				.filter(uu ->
						uu.getNome().trim().toLowerCase().equals(usuario.getNome().trim().toLowerCase()) &&
						//apenas para ser mostrado para pós-graduacao, comentou-se essa linha porque o mysql salva LocalDate com erro de 1 dia.
						//remover comentário da linha abaixo quando resolvido.
						//uu.getNascimento().equals(usuario.getNascimento()) &&
						uu.getSexo().equals(usuario.getSexo())
						)
				.findFirst();

		if (u.isEmpty()) {
			return false;
		}

		usuarioJaExistente = u.get();

		return true;

	}


	private boolean jaTemAlgumEnderecoIgualAEste(Endereco endereco) {

		Collection<Endereco> enderecos = endererecoRepository.findAll();

		Optional<Endereco> end = enderecos.stream().filter(e -> e.equals(endereco)).findFirst();

		if (end.isEmpty()) {
			return false;
		}

		enderecoJaExistente = end.get();

		return true;

	}

	private boolean jaTemAlgumParenteIgualAEste(Parente parente) {
		/*
		Optional<Parente> p = parenteRepository.findByNomeAndNascimentoAndSexo(
				parente.getNome(),
				parente.getNascimento(),
				parente.getSexo());*/

		Collection<Parente> parentes = parenteRepository.findAll();


		Optional<Parente> p = parentes.stream().filter(pp ->

			pp.getNome().trim().toLowerCase().equals(parente.getNome().trim().toLowerCase()) &&
			//apenas para ser mostrado para pós-graduacao, comentou-se essa linha porque o mysql salva LocalDate com erro de 1 dia.
			//remover comentário da linha abaixo quando resolvido.
			//pp.getNascimento().equals(parente.getNascimento()) &&
			pp.getSexo().equals(parente.getSexo()) &&
			pp.getParentesco().equals(parente.getParentesco())

		).findFirst();

		if(p.isPresent()) {
			parenteJaExistente = p.get();
			return true;
		}

		return false;
	}

	private boolean jaTemAlgumEletrodomesticoIgualAEste(Eletrodomestico eletrodomestico) {

		Collection<Eletrodomestico> eletrodomesticos = eletrodomesticoRepository.findAll();

		Optional<Eletrodomestico> p = eletrodomesticos.stream().filter(e -> e.equals(eletrodomestico)).findFirst();

		if (p.isEmpty()) {
			return false;
		}

		eletrodomesticoJaExistente = p.get();

		return true;

	}

	private void checkUsuarioNulidades(Usuario usuario) {
		if (usuario == null)
			throw new NullException("O usuário não pode ser nulo. Corrija e tente novamente.");

		if(usuario.getNome() == null || usuario.getNome().trim().equals(""))
			throw new NullException("O nome do usuário não pode ser nulo");

		if(usuario.getNascimento() == null )
			throw new NullException("O nascimento do usuário não pode ser nulo");

		if(usuario.getSexo() == null )
			throw new NullException("O sexo do usuário não pode ser nulo");
	}

	private void checkEnderecoNulidades(Endereco endereco) {
		if (endereco == null)
			throw new NullException(
					"O endereço a ser associado ao usuário não pode ser nulo. Corrija e tente novamente.");

		if(endereco.getCep() == null || endereco.getCep().trim().equals(""))
			throw new NullException("O cep do endereço não pode ser nulo");

		if(endereco.getLogradouro() == null || endereco.getLogradouro().trim().equals(""))
			throw new NullException("O logradouro do endereço não pode ser nulo");

		if(endereco.getNumero() == null || endereco.getNumero().trim().equals(""))
			throw new NullException("O número do endereço não pode ser nulo");

		if(endereco.getBairro() == null || endereco.getBairro().trim().equals(""))
			throw new NullException("O bairro do endereço não pode ser nulo");

		if(endereco.getCidade() == null || endereco.getCidade().trim().equals(""))
			throw new NullException("A cidade do endereço não pode ser nulo");

		if(endereco.getEstado() == null || endereco.getEstado().trim().equals(""))
			throw new NullException("O estado do endereço não pode ser nulo");

	}

	private void checkUsuarioInsensitiveCases(Usuario usuario, Usuario usuarioNoRepositorio) {

		if( usuario.getNome().toLowerCase().equals(usuarioNoRepositorio.getNome().toLowerCase()) )
					throw new UsuarioCaseInsensitivePropertyException(
							String.format("FALHA AO CRIAR USUÁRIO : "
											+ "o usuario %s é idêntico ao usuário  %s, no repositório"
												, usuario, usuarioNoRepositorio ));

	}

	private void checkEnderecoInsensitiveCases(Endereco endereco, Endereco enderecoNoRepositorio) {

		if( endereco.getCep().toLowerCase().equals(enderecoNoRepositorio.getCep().toLowerCase())
			&& endereco.getLogradouro().toLowerCase().equals(enderecoNoRepositorio.getLogradouro().toLowerCase())
			&& endereco.getNumero().toLowerCase().equals(enderecoNoRepositorio.getNumero().toLowerCase())
			&& endereco.getBairro().toLowerCase().equals(enderecoNoRepositorio.getBairro().toLowerCase())
			&& endereco.getCidade().toLowerCase().equals(enderecoNoRepositorio.getCidade().toLowerCase())
			&& endereco.getEstado().toLowerCase().equals(enderecoNoRepositorio.getEstado().toLowerCase())
				)
					throw new EnderecoCaseInsensitivePropertyException(
							String.format("FALHA AO CRIAR ENDEREÇO : "
											+ "o endereco %s é idêntico ao endereço  %s, no repositório"
												, endereco, enderecoNoRepositorio ));

	}


	private void checkParenteNulidades(Parente parente) {
		if(parente.getNome() == null || parente.getNome().trim().equals(""))
			throw new NullException("O nome do parente não pode ser nulo");

		if(parente.getNascimento() == null)
			throw new NullException("O nascimento do parente não pode ser nulo");

		if(parente.getSexo() == null)
			throw new NullException("O sexo do parente não pode ser nulo");

		if(parente.getParentesco() == null )
			throw new NullException("O parentesco do parente não pode ser nulo");
	}


	private void checkEletrodomesticoNulidades(Eletrodomestico eletrodomestico) {
		if (eletrodomestico == null)
			throw new NullException(
					"O eletrodomestico a ser relacionado ao endereço do usuário não pode ser nulo. Corrija e tente novamente.");

		if (eletrodomestico.getNome() == null || eletrodomestico.getNome().trim().equals(""))
			throw new NullException(
					"O nome do eletrodomestico não pode ser nulo. Corrija e tente novamente.");

		if (eletrodomestico.getModelo() == null || eletrodomestico.getModelo().trim().equals(""))
			throw new NullException(
					"O modelo do eletrodomestico não pode ser nulo. Corrija e tente novamente.");

		if (eletrodomestico.getPotencia() == null )
			throw new NullException(
					"A potência do eletrodomestico não pode ser nula. Corrija e tente novamente.");
	}

	private boolean enderecoExistenteJaAssociadoAEsteUsuario(Usuario usuario) {
		return enderecoJaExistente.getUsuario().equals(usuario);
	}

	private boolean parenteExistenteJaAssociadoAEsteEndereco(Endereco endereco) {
		return parenteJaExistente.getEndereco().getId().equals(endereco.getId());
	}

	private boolean eletrodomesticoExistenteJaAssociadoAEsteEndereco(Endereco endereco) {
		return eletrodomesticoJaExistente.getEndereco().getId().equals(endereco.getId());
	}

	private boolean enderecoAssociadoAoMesmoUsuario(Eletrodomestico eletro, Usuario usuario) {
		return eletro.getEndereco().getUsuario().equals(usuario);
	}

	public void setUsuarioRepository(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	public void setParenteRepository(ParenteRepository parenteRepository) {
		this.parenteRepository = parenteRepository;
	}

	public void setEndererecoRepository(EnderecoRepository endererecoRepository) {
		this.endererecoRepository = endererecoRepository;
	}

	public void setEletrodomesticoRepository(EletrodomesticoRepository eletrodomesticoRepository) {
		this.eletrodomesticoRepository = eletrodomesticoRepository;
	}

	private DomainException entityNotFoundException(Class clazz, Long id) {
		type = clazz;
        return new EntityNotFoundException(String.format(ENTITY_NOT_FOUND_MSG(),id));
    }

	public DomainException entityNotFoundException(CRUD acao, Class clazz, Long id) {
		this.acao = acao;
		type = clazz;
        return new EntityNotFoundException(String.format(ENTITY_NOT_FOUND_MSG(),id));
    }

    private String getType() {
        return type==null? "Entidade":type.getSimpleName();
    }

    private String ENTITY_NOT_FOUND_MSG(){
      return  getType() + " não "+ acao.getAction().toLowerCase()+", pois o id %d não existia na base de dados";
    }

}
