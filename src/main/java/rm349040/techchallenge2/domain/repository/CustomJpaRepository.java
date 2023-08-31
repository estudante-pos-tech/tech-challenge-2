package rm349040.techchallenge2.domain.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CustomJpaRepository<T,ID> extends JpaRepository<T, ID> { 
	Collection<T> find10First();
	Optional<T> queryById(Long id);
}
