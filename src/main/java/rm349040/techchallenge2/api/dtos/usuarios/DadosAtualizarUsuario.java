package rm349040.techchallenge2.api.dtos.usuarios;

import java.time.LocalDate;
import java.util.Collection;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import rm349040.techchallenge2.api.dtos.enderecos.EnderecoAtualizarUsuarioAPI;
import rm349040.techchallenge2.domain.model.Sexo;

public record DadosAtualizarUsuario(

		@NotBlank(message = "O nome da pessoa NÃO pode ser nulo ou em BRANCO")
        @Size(max = 60, message = "O nome da pessoa NÃO pode conter mais do que 60 chars")
		String nome,
		@Past(message = "A data de nascimento NÃO pode ser hoje ou qualquer outro dia depois de hoje")
		LocalDate nascimento,
		@NotNull(message = "O sexo NÃO pode ser nulo")
		Sexo sexo//,
		//Collection<EnderecoAtualizarUsuarioAPI> meusEnderecos

) {

}
