package rm349040.techchallenge2.api.dtos.usuarios.output;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rm349040.techchallenge2.api.dtos.enderecos.EnderecoAtualizarUsuarioAPI;
import rm349040.techchallenge2.domain.model.Sexo;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DadosUsuarioAtualizado {

	private Long id;
    private String nome;
    private LocalDate nascimento;
    private Sexo sexo;
	private OffsetDateTime ultimoAcesso;
	//private Collection<EnderecoAtualizarUsuarioAPI> meusEnderecos;

}
