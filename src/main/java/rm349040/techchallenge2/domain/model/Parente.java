package rm349040.techchallenge2.domain.model;

import java.time.LocalDate;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@Entity @DiscriminatorValue("parente")
public class Parente extends Pessoa {

	@Enumerated(EnumType.STRING)
    private Parentesco parentesco;

    @ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(nullable = false)//sinaliza ser possivel fazer um inner join
    private Endereco endereco;

    public Parente() {
		super();
	}
    @Builder
	public Parente(Long id, String nome, LocalDate nascimento, Sexo sexo, Endereco endereco, Parentesco parentesco, String comunhao) {
		super(id, nome, nascimento, sexo);
		this.parentesco = parentesco;
	}
    
    @Transient
    public Long getUsuarioId() {
    	return getEndereco().getUsuario().getId();
    }

}
