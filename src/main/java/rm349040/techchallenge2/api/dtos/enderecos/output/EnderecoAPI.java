package rm349040.techchallenge2.api.dtos.enderecos.output;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rm349040.techchallenge2.api.dtos.usuarios.UsuarioAPI;
import rm349040.techchallenge2.domain.model.Endereco;

@Getter
@Setter
@NoArgsConstructor
public class EnderecoAPI {

	private Long id;
	private UsuarioAPI usuario;

	public EnderecoAPI(Endereco endereco) {
		this.id = endereco.getId();
	}

}
