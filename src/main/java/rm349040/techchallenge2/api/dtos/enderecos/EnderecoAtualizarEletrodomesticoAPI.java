package rm349040.techchallenge2.api.dtos.enderecos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rm349040.techchallenge2.domain.model.Endereco;

@Getter
@Setter
@NoArgsConstructor
public class EnderecoAtualizarEletrodomesticoAPI {

	private Long id;

	public EnderecoAtualizarEletrodomesticoAPI(Endereco endereco) {
		this.id = endereco.getId();
	}

}
