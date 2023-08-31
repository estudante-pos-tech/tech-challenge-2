//package rm349040.techchallenge2.infrastructure.repository.jpa_impl;
//
//import java.util.Collection;
//
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.persistence.Query;
//import rm349040.techchallenge2.domain.model.Eletrodomestico;
//import rm349040.techchallenge2.domain.model.Endereco;
//import rm349040.techchallenge2.domain.repository.EletrodomesticoRepository;
//
//@Repository
//public class EletrodomesticoRepositoryImpl implements EletrodomesticoRepository{
//
//	@PersistenceContext
//	private EntityManager manager;
//
//	@Override
//	public Collection<Eletrodomestico> listar(){
//
//		 Query query = manager.createQuery("from Eletrodomestico", Eletrodomestico.class);
//
//		 return query.getResultList();
//
//	}
//
//	@Override
//	public Collection<Eletrodomestico> findByEndereco(Endereco endereco) {
//
//		Query query = manager.createNativeQuery("select * from eletrodomestico where endereco_id=?", Eletrodomestico.class);
//		query.setParameter(1, endereco.getId());
//
//		return query.getResultList();
//
//	}
//
//	@Override
//	public Eletrodomestico buscarPorId(Long id) {
//
//		try {
//			Eletrodomestico u = (Eletrodomestico)manager.find(Eletrodomestico.class, id);
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
//	public Eletrodomestico salvarOuAtualizar(Eletrodomestico entity) {
//		return manager.merge(entity);
//	}
//
//	@Transactional
//	@Override
//	public void excluir(Eletrodomestico eletrodomestico) {
//		manager.remove(buscarPorId(eletrodomestico.getId()));
//	}
//
//
//}
