package rm349040.techchallenge2.domain.repository;


import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import rm349040.techchallenge2.domain.model.Usuario;

@Repository
public interface UsuarioRepository extends CustomJpaRepository<Usuario,Long> {
	
	@Query("from Usuario u left join fetch u.meusEnderecos where u.id=:id")
	Optional<Usuario> findById(Long id); 
	
	@Query("from Usuario u left join fetch u.meusEnderecos e "
			+ "join fetch e.meusParentes p "
			+ "join fetch e.meusEletrodomesticos eletro "
			+ " where u.id=:id "
			+ "and p.endereco.usuario.id=:id "
			+ "and eletro.endereco.usuario.id=:id")
	Set<Usuario> findByTestId(Long id); 

	@Query("from Usuario u where " +
			" (:nome is null or u.nome  like concat('%', :nome, '%') )"
            + "and (:nascimento is null or u.nascimento like concat('%', :nascimento, '%') )"
			+ "and (:sexo is null or u.sexo like concat('%', cast(:sexo as java.lang.Enum), '%'))"
            )
	public Collection<Usuario> findByQueryParams(
			String nome,
    		String nascimento,
    		String sexo
    		);
}
