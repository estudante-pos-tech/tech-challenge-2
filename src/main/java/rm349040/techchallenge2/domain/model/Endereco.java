package rm349040.techchallenge2.domain.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import rm349040.techchallenge2.domain.exception.NullException;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@EqualsAndHashCode(of = { "cep", "logradouro", "numero", "bairro", "cidade", "estado" })
@ToString(exclude = {"usuario", "meusEletrodomesticos", "meusParentes"})
@Entity
public class Endereco {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String cep;
	private String logradouro;
	private String numero;
	private String bairro;
	private String cidade;
	private String estado;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Usuario usuario;

	@OneToMany(mappedBy = "endereco", cascade = CascadeType.REMOVE)
	private Collection<Parente> meusParentes = new ArrayList<>();

	@OneToMany(mappedBy = "endereco", cascade = CascadeType.REMOVE)
	private Collection<Eletrodomestico> meusEletrodomesticos = new ArrayList<>();

	public Collection<Eletrodomestico> getMeusEletrodomesticos() {
		return Collections.unmodifiableCollection(meusEletrodomesticos);
	}

	public Collection<Parente> getMeusParentes() {
		return Collections.unmodifiableCollection(meusParentes);
	}

	public void addEletrodomestico(Eletrodomestico eletrodomestico) {

		if (eletrodomestico == null)
			throw new NullException(
					"O eletrodoméstico a ser adicionado ao endereço não pode ser nulo. Corrija e tente novamente.");

		if (eletrodomestico.getId() == null) {
			throw new NullException(
					"O id do eletrodoméstico a ser adicionado ao endereço não pode ser nulo. Corrija e tente novamente.");
		}

		if (!meusEletrodomesticos.contains(eletrodomestico)) {
			meusEletrodomesticos.add(eletrodomestico);
		}
	}

	public void removeEletrodomestico(Eletrodomestico eletrodomestico) {

		if (eletrodomestico == null)
			throw new NullException(
					"O eletrodoméstico a ser removido do endereço não pode ser nulo. Corrija e tente novamente.");

		if (eletrodomestico.getId() == null) {
			throw new NullException(
					"O id do eletrodoméstico a ser removido do endereço não pode ser nulo. Corrija e tente novamente.");
		}

		meusEletrodomesticos.remove(eletrodomestico);

	}



	public void addParente(Parente parente) {
		if (parente == null)
			throw new NullException(
					"O parente a ser adicionado ao endereço não pode ser nulo. Corrija e tente novamente.");

		if (parente.getId() == null) {
			throw new NullException(
					"O id do parente a ser adicionado ao endereço não pode ser nulo. Corrija e tente novamente.");
		}

		if (!meusParentes.contains(parente)) {
			meusParentes.add(parente);
		}
	}

	public void removeParente(Parente parente) {

		if (parente == null)
			throw new NullException(
					"O parente a ser removido do endereço não pode ser nulo. Corrija e tente novamente.");

		if (parente.getId() == null) {
			throw new NullException(
					"O id do parente a ser removido do endereço não pode ser nulo. Corrija e tente novamente.");
		}

		meusParentes.remove(parente);

	}

	@Override
	public int hashCode() {
		return Objects.hash(
				bairro==null?bairro:bairro.toLowerCase(),
				cep==null?cep:cep.toLowerCase(),
				cidade==null?cidade:cidade.toLowerCase(),
				estado==null?estado:estado.toLowerCase(),
				logradouro==null?logradouro:logradouro.toLowerCase(),
				numero==null?numero:numero.toLowerCase());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (getClass() != obj.getClass()))
			return false;
		Endereco other = (Endereco) obj;
		return Objects.equals(bairro==null?bairro:bairro.toLowerCase().trim(), other.bairro==null?other.bairro:other.bairro.toLowerCase().trim())
				&& Objects.equals(cep==null?cep:cep.toLowerCase().trim(), other.cep==null?other.cep:other.cep.toLowerCase().trim())
				&& Objects.equals(cidade==null?cidade:cidade.toLowerCase().trim(), other.cidade==null?other.cidade:other.cidade.toLowerCase().trim())
				&& Objects.equals(estado==null?estado:estado.toLowerCase().trim(), other.estado==null?other.estado:other.estado.toLowerCase().trim())
				&& Objects.equals(logradouro==null?logradouro:logradouro.toLowerCase().trim(), other.logradouro==null?other.logradouro:other.logradouro.toLowerCase().trim())
				&& Objects.equals(numero==null?numero:numero.toLowerCase().trim(), other.numero==null?other.numero:other.numero.toLowerCase().trim());
	}

}
