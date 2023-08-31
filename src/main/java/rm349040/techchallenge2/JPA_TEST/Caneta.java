package rm349040.techchallenge2.JPA_TEST;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rm349040.techchallenge2.TechChallenge2Application;

@Getter
@Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
//@Entity
public class Caneta {

	//@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;


	@CreationTimestamp()
	@Column(columnDefinition = "datetime")//nao queremos precisao milisegundos, então datetime dá tempo sem milisegundos.
	//LocalDateTime usa UTC Z (utc zero), ou seja, UTC-0 (ou UTC+0)
	private OffsetDateTime dataFabricacao;

	@UpdateTimestamp
	@Column(columnDefinition = "datetime")
	//offsetDateTime usa utc com offset UTC do servidor onde está rodando a jvm
	private OffsetDateTime dataAtualizacao;


	//CUSTUMIZANDO O NOME DA TABELA INTERMEDIARIO E SUAS COLUNAS NOMES
//	@ManyToAny
//	@JoinTable(name = "c_c",
//	joinColumns = @JoinColumn(name="can_id"),
//	inverseJoinColumns = @JoinColumn(name="cor_id"))
//	private Collection<Cor> cores = new ArrayList();

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnoreProperties("hibernateLazyInitializer")
	private Cor cor;

	@ManyToMany
	private Collection<Cor> cores = new ArrayList();

	public static void main(String[] args) throws InterruptedException {


//		System.out.println(LocalDateTime.now());
//		System.out.println(LocalDate.now());
//		System.out.println(Timestamp.from(Instant.now()));
//		System.out.println(Date.from(Instant.now()));
//
//		return;

		ApplicationContext context = new SpringApplicationBuilder(TechChallenge2Application.class).
				web(WebApplicationType.NONE).

				run(args);

		CanetaRepository canetaRepository = context.getBean(CanetaRepositoryImpl.class);

		Caneta c = Caneta.builder().nome("Carnaval").build();

		c = canetaRepository.criar(c);

		c.setNome("Carnaval jamaica");

		Thread.sleep(2000);

		c = canetaRepository.atualizar(c);

		System.out.println(c.getDataFabricacao());
		System.out.println(c.getDataAtualizacao());
	}

}
