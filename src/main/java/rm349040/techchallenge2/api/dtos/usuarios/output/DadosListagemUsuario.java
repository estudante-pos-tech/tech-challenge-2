package rm349040.techchallenge2.api.dtos.usuarios.output;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rm349040.techchallenge2.domain.model.Sexo;
import rm349040.techchallenge2.domain.model.Usuario;
import rm349040.techchallenge2.util.Mapper;

@Getter
@Setter
@NoArgsConstructor
public class DadosListagemUsuario  {

	private Long id;
    private String nome;
    private LocalDate nascimento;
    private Sexo sexo;
	private OffsetDateTime ultimoAcesso;

	public DadosListagemUsuario(Usuario usuario, Mapper mapper){
		mapper.identify(usuario, this);
	}

}
