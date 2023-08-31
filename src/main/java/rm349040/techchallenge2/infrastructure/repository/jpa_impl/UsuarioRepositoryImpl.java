//package rm349040.techchallenge2.infrastructure.repository.jpa_impl;
//
//import java.util.Collection;
//
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.persistence.Query;
//import rm349040.techchallenge2.domain.model.Endereco;
//import rm349040.techchallenge2.domain.model.Pessoa;
//import rm349040.techchallenge2.domain.model.Usuario;
//import rm349040.techchallenge2.domain.repository.UsuarioRepository;
//
//@Component
//public class UsuarioRepositoryImpl implements UsuarioRepository{
//
//	@PersistenceContext
//	private EntityManager manager;
//
//	@Override
//	public Collection<Usuario> listar(){
//
//		 Query query = manager.createNativeQuery("select * from pessoa where pessoa_tipo=?", Pessoa.class);
//		 query.setParameter(1, "usuario");
//
//		 return query.getResultList();
//
//	}
//
//	@Override
//	public Usuario buscarPorId(Long id) {
//
//		try {
//			Usuario u = (Usuario)manager.find(Pessoa.class, id);
//			return u;
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//
//		return null;
//	}
//
//	@Transactional
//	@Override
//	public Usuario salvarOuAtualizar(Usuario entity) {
//		return manager.merge(entity);
//	}
//
//	@Transactional
//	@Override
//	public Usuario salvarOuAtualizar(Usuario entity, Endereco endereco) {
//		entity = buscarPorId(entity.getId());
//		entity.addEndereco(endereco);
//		return salvarOuAtualizar(entity);
//	}
//
//	@Transactional
//	@Override
//	public void excluir(Usuario usuario) {
//		manager.remove(buscarPorId(usuario.getId()));
//	}
//
//
//}
