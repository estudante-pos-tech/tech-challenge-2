//package rm349040.techchallenge2.JPA_TEST;
//
//import java.time.LocalDate;
//
//import org.springframework.boot.WebApplicationType;
//import org.springframework.boot.builder.SpringApplicationBuilder;
//import org.springframework.context.ApplicationContext;
//
//import rm349040.techchallenge2.TechChallenge2Application;
//import rm349040.techchallenge2.domain.model.Parente;
//import rm349040.techchallenge2.domain.model.Parentesco;
//import rm349040.techchallenge2.domain.model.Sexo;
//import rm349040.techchallenge2.domain.model.Usuario;
//import rm349040.techchallenge2.domain.repository.ParenteRepository;
//import rm349040.techchallenge2.domain.repository.UsuarioRepository;
//import rm349040.techchallenge2.infrastructure.repository.jpa_impl.ParenteRepositoryImpl;
//import rm349040.techchallenge2.infrastructure.repository.jpa_impl.UsuarioRepositoryImpl;
//
//public class BuscarParenteMain {
//
//	public static void main(String[] args) {
//		ApplicationContext context = new SpringApplicationBuilder(TechChallenge2Application.class)
//				.web(WebApplicationType.NONE).run(args);
//
//		ParenteRepository parenteRepository = context.getBean(ParenteRepositoryImpl.class);
//
//		UsuarioRepository usuarioRepository = context.getBean(UsuarioRepositoryImpl.class);
//
//		Usuario usuario = usuarioRepository.findById(1L);
//
//		Parente parente = new Parente().builder().
//				nome("Usuario-Amélia").
//				sexo(Sexo.FEMININO).
//				nascimento(LocalDate.of(2000, 1, 10)).
//				parentesco(Parentesco.AVÔ).
//				build();
////		parente.setUsuario(usuario);
//
//		parente = parenteRepository.save(parente);
//
//		parente = parenteRepository.findById(parente.getId());
//
//	System.out.printf("%d - %s - %s - %s\n", parente.getId(), parente.getNome(), parente.getParentesco(),"");
//
//	}
//
//}
