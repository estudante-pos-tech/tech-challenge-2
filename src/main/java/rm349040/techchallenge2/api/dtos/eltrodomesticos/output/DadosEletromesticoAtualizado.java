package rm349040.techchallenge2.api.dtos.eltrodomesticos.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rm349040.techchallenge2.api.dtos.enderecos.output.EnderecoAPI;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DadosEletromesticoAtualizado {

    private Long id;
    private String nome;
    private String modelo;
    private Double potencia;
    private EnderecoAPI endereco;

}
