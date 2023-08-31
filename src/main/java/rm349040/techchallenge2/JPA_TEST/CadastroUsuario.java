/**
 *
 */
package rm349040.techchallenge2.JPA_TEST;

import java.util.Collection;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import rm349040.techchallenge2.domain.model.Pessoa;
import rm349040.techchallenge2.domain.model.Usuario;

/**
 *
 */
@Component
public class CadastroUsuario {

	@PersistenceContext
	private EntityManager manager;

	public Collection<Usuario> listar(){

		 Query query = manager.createNativeQuery("select * from pessoa where pessoa_tipo=?", Pessoa.class);
		 query.setParameter(1, "usuario");

		 return query.getResultList();

	}

	public Usuario buscarPorId(Long id) {

		try {
			Usuario u = (Usuario)manager.find(Pessoa.class, id);
			return u;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}

	@Transactional
	public Usuario salvarOuAtualizar(Usuario entity) {
		return manager.merge(entity);
	}

	@Transactional
	public void excluir(Usuario usuario) {
		manager.remove(buscarPorId(usuario.getId()));
	}




}
