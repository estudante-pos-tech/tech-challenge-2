//package rm349040.techchallenge2.JPA_TEST;
//
//import java.time.LocalDate;
//import java.time.OffsetDateTime;
//
//import org.springframework.boot.WebApplicationType;
//import org.springframework.boot.builder.SpringApplicationBuilder;
//import org.springframework.context.ApplicationContext;
//
//import rm349040.techchallenge2.TechChallenge2Application;
//import rm349040.techchallenge2.domain.model.Sexo;
//import rm349040.techchallenge2.domain.model.Usuario;
//import rm349040.techchallenge2.domain.repository.UsuarioRepository;
//import rm349040.techchallenge2.infrastructure.repository.jpa_impl.UsuarioRepositoryImpl;
//
//public class InserirUsuariosMain {
//
//	public static void main(String[] args) {
//		ApplicationContext context = new SpringApplicationBuilder(TechChallenge2Application.class).
//				web(WebApplicationType.NONE).
//				run(args);
//
//
//		UsuarioRepository usuarioRepositorio = context.getBean(UsuarioRepositoryImpl.class);
//
//		Usuario usuario1 = new Usuario().builder().
//				nome("Usuario-2").
//				sexo(Sexo.FEMININO).
//				nascimento(LocalDate.of(2000, 1, 10)).
//				ultimoAcesso(OffsetDateTime.now()).
//				complemento("Está sem ar fresco").build();
//
//		Usuario usuario2 = new Usuario().builder().
//				nome("Usuario-3").
//				sexo(Sexo.FEMININO).
//				nascimento(LocalDate.of(1990, 2, 12)).
//				ultimoAcesso(OffsetDateTime.now()).
//				complemento("Está em srp4").build();
//
//
//		System.out.printf("%d - %s\n", usuario1.getId(), usuario1.getNome());
//		System.out.printf("%d - %s\n", usuario2.getId(), usuario2.getNome());
//
//		usuario1 = usuarioRepositorio.save(usuario1);
//		usuario2 = usuarioRepositorio.save(usuario2);
//
//
//		System.out.printf("%d - %s\n", usuario1.getId(), usuario1.getNome());
//		System.out.printf("%d - %s\n", usuario2.getId(), usuario2.getNome());
//
//	}
//
//
//
//}
