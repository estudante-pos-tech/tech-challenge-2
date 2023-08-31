package rm349040.techchallenge2.domain.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import rm349040.techchallenge2.domain.exception.DomainException;
import rm349040.techchallenge2.domain.exception.EntityNotFoundException;
import rm349040.techchallenge2.domain.exception.IdNullException;
import rm349040.techchallenge2.domain.exception.NullException;
import rm349040.techchallenge2.domain.exception.UsuarioInexistenteException;
import rm349040.techchallenge2.domain.model.Endereco;
import rm349040.techchallenge2.domain.repository.EnderecoRepository;
import rm349040.techchallenge2.domain.repository.UsuarioRepository;

@Service
public abstract class CadastroService<T>{

    private Class<T> type;
    @Autowired protected JpaRepository<T,Long> repositorio;
    protected CRUD acao = CRUD.LISTAR;

    public CadastroService(Class<T> entityClass) {
        this.type = entityClass;
    }

    public T criar(T t) {
    	throw new UnsupportedOperationException();
    }

    public T atualizarOuFalhar(T atual){
    	throw new UnsupportedOperationException();
    }

    public void excluirById(Long id) {
    	throw new UnsupportedOperationException();
    }

    public Collection<T> listar() {
    	return repositorio.findAll();
    }
    
    public Page<T> listar(Pageable pageable) {
    	return repositorio.findAll(pageable);
    }

    public T listarById(Long id) {

        try {

            acao = CRUD.LISTAR;

            return repositorio.findById(id).orElseThrow(() -> entityNotFoundException(id));

        }catch (NullPointerException e){

            throw new IdNullException(e.getMessage());

        }

    }

    public Collection<T> findByQueryParams(String... params){
		throw new UnsupportedOperationException();
	}

    /**
     * We encapsulate the algorithm's varying parts. The algorithm repeats itself in many methods.
     * This super method encapsulates many other methods calls which need entities by usuario retrievals.
     * Those methods are almost completely similar. They differ only by the type of entities they need.
     * @param <S> the type of entities we want to get from repository
     * @param id the usuario id
     * @param function this function encapsulates the type of entities collections that vary among method's clients needs.
     * @param type the S type entity
     * @param enderecoRepository the repository we search in
     * @param usuarioRepository the repository we search in
     * @return a Collection of <S> type
     * @throws NullException if id is null
     * @throws UsuarioInexistenteException if the usuario is not in the repository
     */
    public <S> Collection<S> getEntitiesByUsuarioId(Long id,
    		Function<Endereco,
    		Collection<S>> function,
    		Class<S> type,
    		EnderecoRepository enderecoRepository,
    		UsuarioRepository usuarioRepository){

    	if (id!=null) {

    		usuarioRepository.findById(id)
    							.orElseThrow(
    									() -> new UsuarioInexistenteException(
    											String.format("FALHA AO BUSCAR %s DESTE USUÁRIO : "
    													+ "o usuário nao está cadastrado no repositório"
    													, (type.getTypeName().split("\\.")[type.getTypeName().split("\\.").length-1]).toUpperCase()+"S")));

			Collection<S> entitiesByUsuarioId = new HashSet<>();

			enderecoRepository
				.findByUsuarioId(id)
					.forEach(endereco -> entitiesByUsuarioId.addAll(function.apply(endereco)));

			return entitiesByUsuarioId;

		}else {

			throw new NullException(
								String.format("FALHA AO BUSCAR %s DESTE USUÁRIO : "
										+ "O id do usuário não pode ser null"
										, type.getClass().getSimpleName().toUpperCase()+"S"));

		}

    }

    protected DomainException entityNotFoundException(Long id) {
        return new EntityNotFoundException(String.format(ENTITY_NOT_FOUND_MSG(),id));
    }

    private String getType() {
        return type==null? "Entidade":type.getSimpleName();
    }

    protected String ENTITY_NOT_FOUND_MSG(){
        return  getType() + " não "+ acao.getAction().toLowerCase()+", pois o id %d não existia na base de dados";
    }

    public JpaRepository<T, Long> getRepository() {
		return repositorio;
	}

}