package rm349040.techchallenge2.api.dtos.enderecos.output;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rm349040.techchallenge2.api.dtos.usuarios.UsuarioAPI;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DadosEnderecoAtualizado {

    private Long id;
    private String cep;
    private String logradouro;
    private String numero;
    private String bairro;
    private String cidade;
    private String estado;
    private UsuarioAPI usuario;

}
