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
//import rm349040.techchallenge2.domain.model.Usuario;
//import rm349040.techchallenge2.domain.repository.EnderecoRepository;
//
//@Component
//public class EnderecoRepositoryImpl implements EnderecoRepository{
//
//	@PersistenceContext
//	private EntityManager manager;
//
//	@Override
//	public Collection<Endereco> listar(){
//
//		 Query query = manager.createQuery("from Endereco", Endereco.class);
//
//		 Collection<Endereco> result = query.getResultList();
//		 return result;
//
////		 try {
////
////			 Collection<Endereco> result = query.getResultList();
////			 return result;
////
////		} catch (Exception e) {
////			e.printStackTrace();
////		}
////
////
////		 return new ArrayList<>();
//
//	}
//
//	@Override
//	public Collection<Endereco> findByUsuario(Usuario usuario) {
//
//		Query query = manager.createNativeQuery("select * from endereco where usuario_id=?", Endereco.class);
//		query.setParameter(1, usuario.getId());
//
//		return query.getResultList();
//	}
//
//	@Override
//	public Endereco buscarPorId(Long id) {
//
//		try {
//			Endereco endereco = (Endereco)manager.find(Endereco.class, id);
//			return endereco;
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//
//		return null;
//	}
//
//	@Transactional
//	@Override
//	public Endereco salvarOuAtualizar(Endereco entity) {
//		return manager.merge(entity);
//	}
//
//	@Transactional
//	@Override
//	public void excluir(Endereco endereco) {
//		manager.remove(buscarPorId(endereco.getId()));
//	}
//
//
//
//}
