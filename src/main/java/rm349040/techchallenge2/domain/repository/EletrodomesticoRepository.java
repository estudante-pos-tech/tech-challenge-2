package rm349040.techchallenge2.domain.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import rm349040.techchallenge2.domain.model.Eletrodomestico;
import rm349040.techchallenge2.domain.model.Endereco;

@Repository
public interface EletrodomesticoRepository extends CustomJpaRepository<Eletrodomestico, Long> {

	public Collection<Eletrodomestico> findByEndereco(Endereco endereco);
	public Collection<Eletrodomestico> findByEnderecoId(Long id);
	public Collection<Eletrodomestico> findTop2ByEnderecoId(Long id);
	public boolean existsByNome(String nome);
	public boolean existsByNomeContaining(String nome);
	public int countByEnderecoId(Long id);

	@Query("from Eletrodomestico e where " +
			" (:nome is null or e.nome  like concat('%', :nome, '%') )"
            + "and (:modelo is null or e.modelo like concat('%', :modelo, '%') )"
			+ "and (:potencia is null or e.potencia like concat('%', :potencia, '%') )"
			)
	public Collection<Eletrodomestico> findByQueryParams(
			String nome,
    		String modelo,
    		String potencia
    		);


}
