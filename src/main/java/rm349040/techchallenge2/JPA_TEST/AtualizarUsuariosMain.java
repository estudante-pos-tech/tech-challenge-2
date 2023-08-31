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
//public class AtualizarUsuariosMain {
//
//	public static void main(String[] args) {
//		ApplicationContext context = new SpringApplicationBuilder(TechChallenge2Application.class).
//				web(WebApplicationType.NONE).
//				run(args);
//
//		UsuarioRepository usuarioRepository = context.getBean(UsuarioRepositoryImpl.class);
//
//		Usuario usuario = usuarioRepository.findById(1L);
//
//		System.out.printf("%d - %s\n", usuario.getId(), usuario.getNome());
//
//		usuario = new Usuario().builder().
//				id(1L).
//				nome("NATASHA").
//				sexo(Sexo.FEMININO).
//				nascimento(LocalDate.of(2000, 1, 10)).
//				ultimoAcesso(OffsetDateTime.now()).
//				complemento("ESTA COM SAIA DE BURRACHA").build();
//
//		usuarioRepository.save(usuario);
//
//		usuario = usuarioRepository.findById(1L);
//
//		System.out.printf("%d - %s\n", usuario.getId(), usuario.getNome());
//
//	}
//
//
//
//}
