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
//import rm349040.techchallenge2.domain.model.Parente;
//import rm349040.techchallenge2.domain.model.Pessoa;
//import rm349040.techchallenge2.domain.repository.ParenteRepository;
//
//@Component
//public class ParenteRepositoryImpl implements ParenteRepository{
//
//	@PersistenceContext
//	private EntityManager manager;
//
//	@Override
//	public Collection<Parente> listar(){
//
//		 Query query = manager.createNativeQuery("select * from pessoa where pessoa_tipo=?", Pessoa.class);
//		 query.setParameter(1, "parente");
//
//		 return query.getResultList();
//
//	}
//
//	@Override
//	public Collection<Parente> findByEndereco(Endereco endereco) {
//
//		Query query = manager.createNativeQuery("select * from pessoa where endereco_id=?", Pessoa.class);
//		query.setParameter(1, endereco.getId());
//
//		return query.getResultList();
//
//	}
//
//	@Override
//	public Parente buscarPorId(Long id) {
//
//		try {
//			Parente u = (Parente)manager.find(Pessoa.class, id);
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
//	public Parente salvarOuAtualizar(Parente entity) {
//		return manager.merge(entity);
//	}
//
//	@Transactional
//	@Override
//	public void excluir(Parente parente) {
//		manager.remove(buscarPorId(parente.getId()));
//	}
//
//
//
//}
////