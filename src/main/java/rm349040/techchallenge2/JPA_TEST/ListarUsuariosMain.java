//package rm349040.techchallenge2.JPA_TEST;
//
//import org.springframework.boot.WebApplicationType;
//import org.springframework.boot.builder.SpringApplicationBuilder;
//import org.springframework.context.ApplicationContext;
//
//import rm349040.techchallenge2.TechChallenge2Application;
//import rm349040.techchallenge2.domain.repository.UsuarioRepository;
//import rm349040.techchallenge2.infrastructure.repository.jpa_impl.UsuarioRepositoryImpl;
//
//public class ListarUsuariosMain {
//
//	public static void main(String[] args) {
//		ApplicationContext context = new SpringApplicationBuilder(TechChallenge2Application.class).
//				web(WebApplicationType.NONE).
//				run(args);
//
//
//		UsuarioRepository usuarioRepositorio = context.getBean(UsuarioRepositoryImpl.class);
//
//		usuarioRepositorio.findAll().forEach(System.out::println);
//
//	}
//
//
//
//}
