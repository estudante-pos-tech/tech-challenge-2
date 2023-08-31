package rm349040.techchallenge2.api.dtos.eltrodomesticos.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rm349040.techchallenge2.domain.model.Eletrodomestico;
import rm349040.techchallenge2.util.Mapper;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EletrodomesticosListagemEnderecoPorId{

    private Long id;
    private String nome;
    private String modelo;
    private Double potencia;

    public EletrodomesticosListagemEnderecoPorId(Eletrodomestico eletrodomestico, Mapper mapper){
    	mapper.identify(eletrodomestico, this);
    }
}
