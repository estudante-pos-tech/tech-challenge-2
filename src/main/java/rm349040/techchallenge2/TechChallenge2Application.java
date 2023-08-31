package rm349040.techchallenge2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import rm349040.techchallenge2.infrastructure.repository.jpa_impl.CustomJpaRepositoryImpl;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = CustomJpaRepositoryImpl.class)
public class TechChallenge2Application {

	public static void main(String[] args) {
		SpringApplication.run(TechChallenge2Application.class, args);
	}

}
