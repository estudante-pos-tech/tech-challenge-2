package rm349040.techchallenge2.domain.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import rm349040.techchallenge2.domain.model.Endereco;
import rm349040.techchallenge2.domain.model.Usuario;

@Repository
public interface EnderecoRepository extends CustomJpaRepository<Endereco, Long>{

	Collection<Endereco> findByUsuario(Usuario usuario);

	Collection<Endereco> findByUsuarioId(Long id);
	
	@Query("from Endereco e join e.usuario left join fetch e.meusParentes left join e.meusEletrodomesticos")
	List<Endereco> findAll(); 
	
	@Query("from Endereco e join fetch e.usuario left join e.meusParentes p "
			+ "left join fetch e.meusEletrodomesticos m where e.id=:id")
	Optional<Endereco> findById(Long id); 


//	@Query("select e from Endereco e where " +
//            "coalesce(e.cep, '') like concat('%', :cep, '%') " +
//            "and coalesce(e.logradouro, '') like concat('%', :logradouro, '%') " +
//            "and coalesce(e.numero, '') like concat('%', :numero, '%') " +
//            "and coalesce(e.bairro, '') like concat('%', :bairro, '%') " +
//            "and coalesce(e.cidade, '') like concat('%', :cidade, '%') " +
//            "and coalesce(e.estado, '') like concat('%', :estado, '%') " )
	@Query("from Endereco e where " +
			" (:cep is null or e.cep  like concat('%', :cep, '%') )"
            + "and (:logradouro is null or e.logradouro like concat('%', :logradouro, '%') )"
			+ "and (:numero is null or e.numero like concat('%', :numero, '%') )"
            + "and (:bairro is null or e.bairro like concat('%', :bairro, '%') )"
            + "and (:cidade is null or e.cidade like concat('%', :cidade, '%') )"
            + "and (:estado is null or e.estado like concat('%', :estado, '%') )"
			)
	public Collection<Endereco> findByQueryParams(
			String cep,
    		String logradouro,
    		String numero,
    		String bairro,
    		String cidade,
    		String estado);






}
