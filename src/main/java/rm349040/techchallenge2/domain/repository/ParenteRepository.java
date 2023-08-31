package rm349040.techchallenge2.domain.repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import rm349040.techchallenge2.domain.model.Endereco;
import rm349040.techchallenge2.domain.model.Parente;
import rm349040.techchallenge2.domain.model.Sexo;

@Repository
public interface ParenteRepository extends CustomJpaRepository<Parente, Long>{

	public Collection<Parente> findByEndereco(Endereco endereco);
	public Collection<Parente> findByEnderecoId(Long id);
	@Override
	public Optional<Parente> findById(Long id);
	public Optional<Parente> findByNomeAndNascimentoAndSexo(String nome, LocalDate nascimento, Sexo sexo);


	@Query("from Parente p where " +
			" (:nome is null or p.nome  like concat('%', :nome, '%') )"
            + "and (:nascimento is null or p.nascimento like concat('%', :nascimento, '%') )"
			+ "and (:sexo is null or p.sexo like concat('%', cast(:sexo as java.lang.Enum), '%'))"
			+ "and (:parentesco is null or p.parentesco like concat('%', cast(:parentesco as java.lang.Enum), '%'))"
            )
	public Collection<Parente> findByQueryParams(
			String nome,
    		String nascimento,
    		String sexo,
    		String parentesco
    		);

}
