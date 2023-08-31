package rm349040.techchallenge2.domain.model;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@ToString()
@Entity
public class Eletrodomestico {

    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String modelo;
    private Double potencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)//sinaliza ser possivel fazer um inner join
    private Endereco endereco;
    
    @Transient
    public Long getUsuarioId() {
    	return getEndereco().getUsuario().getId();
    }

}
