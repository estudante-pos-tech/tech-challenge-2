package rm349040.techchallenge2.api.dtos.enderecos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import rm349040.techchallenge2.api.dtos.usuarios.UsuarioAPI;


public record DadosCadastroEndereco(

		@NotBlank
		@Pattern(regexp = "(^[0-9]{5})-?([0-9]{3}$)",message = "O cep deve ter 8 dígitos. Opcionalmente, os três últimos dígitos da direita podem ser separados dos demais com hifen -")
		String cep,
        @NotBlank
        @Size(max = 60)
        String logradouro,
        @NotBlank()
        @Size(max = 10)
        String numero,
        @NotBlank
        @Size(max = 40)
        String bairro,
        @NotBlank
        @Size(max = 50)
        String cidade,
        @NotBlank
        @Size(max = 30)
        String estado,
        UsuarioAPI usuario

) {}
