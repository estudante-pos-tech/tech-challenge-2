package rm349040.techchallenge2.api.dtos.pessoas.output;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rm349040.techchallenge2.api.dtos.enderecos.output.EnderecoAPI;
import rm349040.techchallenge2.domain.model.Parentesco;
import rm349040.techchallenge2.domain.model.Sexo;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DadosParenteCriado {
    private Long id;
    private String nome;
    private LocalDate nascimento;
    private Sexo sexo;
    private Parentesco parentesco;
    private EnderecoAPI endereco;
}
