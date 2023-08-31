package rm349040.techchallenge2.api.dtos.pessoas.output;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rm349040.techchallenge2.api.dtos.enderecos.output.EnderecoParentePorId;
import rm349040.techchallenge2.domain.model.Parente;
import rm349040.techchallenge2.domain.model.Parentesco;
import rm349040.techchallenge2.domain.model.Sexo;
import rm349040.techchallenge2.util.Mapper;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DadosListagemPessoaPorId {

    private Long id;
    private String nome;
    private LocalDate nascimento;
    private Sexo sexo;
    private Parentesco parentesco;
    private EnderecoParentePorId endereco;

    public DadosListagemPessoaPorId(Parente parente, Mapper mapper){
        mapper.identify(parente, this);
      }

}
