package rm349040.techchallenge2.api.dtos.enderecos.output;

import java.util.Collection;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rm349040.techchallenge2.api.dtos.eltrodomesticos.output.EletrodomesticosListagemEnderecoPorId;
import rm349040.techchallenge2.api.dtos.pessoas.output.ParentesListagemEnderecoPorId;
import rm349040.techchallenge2.api.dtos.usuarios.UsuarioAPI;
import rm349040.techchallenge2.domain.model.Endereco;
import rm349040.techchallenge2.util.Mapper;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DadosEnderecoPorId {

	private Long id;
	private String cep;
	private String logradouro;
	private String numero;
	private String bairro;
	private String cidade;
	private String estado;
	private UsuarioAPI usuario;
    private Collection<ParentesListagemEnderecoPorId> meusParentes;
	private Collection<EletrodomesticosListagemEnderecoPorId> meusEletrodomesticos;

	public DadosEnderecoPorId(Endereco endereco, Mapper mapper) {

		mapper.identify(endereco,this);

		this.meusParentes = mapper.toDtoCollection(endereco.getUsuario().getMeusParentes(), ParentesListagemEnderecoPorId.class);
		this.meusEletrodomesticos = mapper.toDtoCollection(endereco.getUsuario().getMeusEletrodomesticos(), EletrodomesticosListagemEnderecoPorId.class);
	}

}
