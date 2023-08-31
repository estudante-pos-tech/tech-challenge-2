package rm349040.techchallenge2.JPA_TEST;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Component
public class CanetaRepositoryImpl implements CanetaRepository{

	@PersistenceContext
	private EntityManager manager;

	@Transactional
	@Override
	public Caneta criar(Caneta caneta) {
		return manager.merge(caneta);
	}

	@Transactional
	@Override
	public Caneta atualizar(Caneta caneta) {
		return manager.merge(caneta);
	}

}
