package rm349040.techchallenge2.api.dtos.enderecos.output;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rm349040.techchallenge2.domain.model.Endereco;

@Getter
@Setter
@NoArgsConstructor
public class EnderecoParenteAPI {

	private Long id;

	public EnderecoParenteAPI(Endereco endereco) {
		this.id = endereco.getId();
	}

}
