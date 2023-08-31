package rm349040.techchallenge2.configuration;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.TimeZone;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import rm349040.techchallenge2.api.dtos.eltrodomesticos.output.DadosListagemEletrodomesticoNesteEndereco;
import rm349040.techchallenge2.api.dtos.enderecos.output.ParentesQueMoramAquiListagem;
import rm349040.techchallenge2.api.dtos.pessoas.output.DadosListagemPessoaNesteEndereco;
import rm349040.techchallenge2.domain.model.Eletrodomestico;
import rm349040.techchallenge2.domain.model.Parente;

@Configuration
public class TechChallengeAppConfiguration {

    @Bean
    public Validator beanValidator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Bean
    @Scope("prototype")
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().
                setFieldMatchingEnabled(true).
                setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        modelMapper
        .createTypeMap(Eletrodomestico.class, DadosListagemEletrodomesticoNesteEndereco.class)
        .addMapping(Eletrodomestico::getUsuarioId,DadosListagemEletrodomesticoNesteEndereco::setUsuarioId);
        
        modelMapper
        .createTypeMap(Parente.class, DadosListagemPessoaNesteEndereco.class)
        .addMapping(Parente::getUsuarioId, DadosListagemPessoaNesteEndereco::setUsuarioId);
        
        modelMapper
        .createTypeMap(Parente.class, ParentesQueMoramAquiListagem.class)
        .addMapping(Parente::getUsuarioId, ParentesQueMoramAquiListagem::setUsuarioId);
        
        
        return modelMapper;
    }
    
    @Bean
    @Scope("prototype")
    public rm349040.techchallenge2.domain.main.System system() {
    	return new rm349040.techchallenge2.domain.main.System();
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        System.out.println("Date in UTC: " + LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)+"Z");
    }

}
