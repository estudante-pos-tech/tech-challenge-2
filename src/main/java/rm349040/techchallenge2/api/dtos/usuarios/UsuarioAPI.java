package rm349040.techchallenge2.api.dtos.usuarios;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rm349040.techchallenge2.domain.model.Usuario;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioAPI {

	private Long id;

	public UsuarioAPI(Usuario usuario) {
		this.id = usuario.getId();
	}

}
