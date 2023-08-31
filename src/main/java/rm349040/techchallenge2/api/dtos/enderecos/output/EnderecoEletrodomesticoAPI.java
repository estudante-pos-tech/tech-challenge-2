package rm349040.techchallenge2.api.dtos.enderecos.output;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rm349040.techchallenge2.domain.model.Endereco;

@Getter
@Setter
@NoArgsConstructor
public class EnderecoEletrodomesticoAPI {

	private Long id;

	public EnderecoEletrodomesticoAPI(Endereco endereco) {
		this.id = endereco.getId();
	}

}
