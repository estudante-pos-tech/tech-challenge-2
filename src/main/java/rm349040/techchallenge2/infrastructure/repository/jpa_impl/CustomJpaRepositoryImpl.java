package rm349040.techchallenge2.infrastructure.repository.jpa_impl;

import java.util.Collection;
import java.util.Optional;

import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import rm349040.techchallenge2.domain.repository.CustomJpaRepository;

@Repository
@Scope("prototype")
public class CustomJpaRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> implements CustomJpaRepository<T, ID> {

	private EntityManager entityManager;

	public CustomJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.entityManager = entityManager;
	}

	@Override
	public Collection<T> find10First() {

		String jpql = "from " + getDomainClass().getName();

		Collection<T> tenFirst = entityManager.createQuery(jpql, getDomainClass()).setMaxResults(10).getResultList();

		return tenFirst;
	}

	@Override
	public Optional<T> queryById(Long id) {

		String jpql = "from " + getDomainClass().getName() + " e where e.id=:id";
				
		try {
			return Optional.of(entityManager
						.createQuery(jpql,getDomainClass())
						.setParameter("id", id)
						.getSingleResult());
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return Optional.empty();
	}

}
