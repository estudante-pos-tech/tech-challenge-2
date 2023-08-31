package rm349040.techchallenge2.api.dtos.usuarios.output;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rm349040.techchallenge2.api.dtos.eltrodomesticos.output.EletrodomesticosListagem;
import rm349040.techchallenge2.api.dtos.enderecos.output.EnderecoListagem;
import rm349040.techchallenge2.api.dtos.pessoas.output.ParentesListagem;
import rm349040.techchallenge2.domain.model.Sexo;
import rm349040.techchallenge2.domain.model.Usuario;
import rm349040.techchallenge2.util.Mapper;

@Getter
@Setter
@NoArgsConstructor
public class DadosListagemUsuarioPorId  {
	@Autowired
	@JsonIgnore
	private Mapper mapper;

	private Long id;
    private String nome;
    private LocalDate nascimento;
    private Sexo sexo;
	private OffsetDateTime ultimoAcesso;
	private Collection<EnderecoListagem> meusEnderecos;
	private Collection<ParentesListagem> meusParentes;
	private Collection<EletrodomesticosListagem> meusEletrodomesticos;

	public DadosListagemUsuarioPorId(Usuario usuario, Mapper mapper){

		mapper.identify(usuario,this);
		
//		Collection<EnderecoListagem> meusEnderecosListagem = usuario.getMeusEnderecos()
//											.stream().map(e -> new EnderecoListagem(e,mapper))
//												.collect(Collectors.toList());

		this.meusEnderecos = mapper.toDtoCollection(usuario.getMeusEnderecos(), EnderecoListagem.class);
		this.meusParentes = mapper.toDtoCollection(usuario.getMeusParentes(), ParentesListagem.class);
		this.meusEletrodomesticos = mapper.toDtoCollection(usuario.getMeusEletrodomesticos(), EletrodomesticosListagem.class);
	}

}
