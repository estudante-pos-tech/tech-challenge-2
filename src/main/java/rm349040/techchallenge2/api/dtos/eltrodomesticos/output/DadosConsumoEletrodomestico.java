package rm349040.techchallenge2.api.dtos.eltrodomesticos.output;

import java.math.BigDecimal;
import java.math.RoundingMode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rm349040.techchallenge2.domain.exception.DomainException;
import rm349040.techchallenge2.domain.model.Eletrodomestico;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DadosConsumoEletrodomestico{

    private Long id;
    private String nome;
    private String potencia;
    private String tempoDeUso;
    private String consumo;

    public DadosConsumoEletrodomestico(Eletrodomestico eletrodomestico, Long  tempoDeUsoEmSegundos){

    	if(tempoDeUsoEmSegundos<0) {
    		throw new DomainException(
    				String.format("FALHA AO CALCULAR CONSUMO PARA ELETRODOMÉSTICO (id=%s) - %s : "
    						+ "tempo de uso deve ser >= ZERO, mas veio : %s"
    						, eletrodomestico.getId()
    						,eletrodomestico.getNome()
    						,tempoDeUsoEmSegundos));
    	}
    	
    	/*se quiser precisao decimal maior ou menor, basta atualizar esta variavel.*/
    	int DECIMAL_PRECISION = 10;
    	
    	BigDecimal potenciaEmKW = new BigDecimal(eletrodomestico.getPotencia()/1000.0).setScale(DECIMAL_PRECISION, RoundingMode.HALF_UP);
    	BigDecimal tempoDeUsoEmHora = new BigDecimal(tempoDeUsoEmSegundos/3600.0).setScale(DECIMAL_PRECISION, RoundingMode.HALF_UP);

    	this.id = eletrodomestico.getId();
    	this.nome = eletrodomestico.getNome();

    	//adiciona equivalente em KW
    	potencia = appendPotenciaEquivalenteEmKW(eletrodomestico, potenciaEmKW);

    	//adiciona equivalente em horas
    	tempoDeUso = appendTempoEquivalenteEmHoras(tempoDeUsoEmSegundos, tempoDeUsoEmHora);

    	BigDecimal consumoEmKWh = tempoDeUsoEmHora
    				.multiply(potenciaEmKW)
    				.setScale(DECIMAL_PRECISION, RoundingMode.HALF_UP);

    	consumo = String.format("%s KWh", consumoEmKWh.toString());
    }

	private String appendTempoEquivalenteEmHoras(Long tempoDeUsuEmSegundos, BigDecimal tempoDeUsoEmHora) {
		return new StringBuilder()
    			.append(Long.toString(tempoDeUsuEmSegundos))
    			.append("s ( ")
    			.append(tempoDeUsoEmHora.toString())
    			.append(" h )").toString();
	}

	private String appendPotenciaEquivalenteEmKW(Eletrodomestico eletrodomestico, BigDecimal potenciaEmKW) {
		return new StringBuilder()
    			.append(Double.toString(eletrodomestico.getPotencia()))
    			.append("W ( ")
    			.append(potenciaEmKW.toString())
    			.append(" KW )").toString();
	}

}
