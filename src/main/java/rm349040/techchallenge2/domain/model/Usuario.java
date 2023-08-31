//TODO Excluir essa classe se não houver relacao entre usuario e pessoa.
package rm349040.techchallenge2.domain.model;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import rm349040.techchallenge2.domain.exception.NullException;

@Getter
@Setter
@ToString(callSuper = true)
@Entity
@DiscriminatorValue("usuario")
public class Usuario extends Pessoa {


	@CreationTimestamp()
	@Column(columnDefinition = "datetime")//nao queremos precisao milisegundos, então datetime dá tempo sem milisegundos.
	private OffsetDateTime ultimoAcesso;

	@OneToMany(mappedBy = "usuario", cascade = CascadeType.REMOVE)
	private Collection<Endereco> meusEnderecos = new ArrayList();


	public Usuario() {
		super();
	}

	@Builder
	public Usuario(Long id, String nome, LocalDate nascimento, Sexo sexo, OffsetDateTime ultimoAcesso,
			String complemento) {
		super(id, nome, nascimento, sexo);
		this.ultimoAcesso = ultimoAcesso;
	}

	public Collection<Endereco> getMeusEnderecos() {
		return Collections.unmodifiableCollection(meusEnderecos);
	}

	public void addEndereco(Endereco endereco) {

		if (endereco == null)
			throw new NullException(
					"O endereço a ser adicionado ao usuário não pode ser nulo. Corrija e tente novamente.");

		if (endereco.getId() == null) {
			throw new NullException(
					"O id do endereço a ser adicionado ao usuário não pode ser nulo. Corrija e tente novamente.");
		}

		if (!meusEnderecos.contains(endereco)) {
			meusEnderecos.add(endereco);
		}
	}

	public void removeEndereco(Endereco endereco) {

		if (endereco == null)
			throw new NullException(
					"O endereço a ser removido do usuário não pode ser nulo. Corrija e tente novamente.");

		if (endereco.getId() == null) {
			throw new NullException(
					"O id do endereço a ser removido do usuário não pode ser nulo. Corrija e tente novamente.");
		}

		meusEnderecos.remove(endereco);

	}

	@Transient
	public Collection<Parente> getMeusParentes(){
		Collection<Parente> meusParentes = new HashSet<>();
		getMeusEnderecos().stream().forEach(e -> meusParentes.addAll(e.getMeusParentes()));
		return Collections.unmodifiableCollection(meusParentes);
	}

	@Transient
	public Collection<Eletrodomestico> getMeusEletrodomesticos() {

		Collection<Eletrodomestico> meusEletrodomesticos = new HashSet<>();
		getMeusEnderecos()
			.stream()
			.forEach(endereco -> meusEletrodomesticos.addAll(endereco.getMeusEletrodomesticos()));

		return Collections.unmodifiableCollection(meusEletrodomesticos);
	}

}
