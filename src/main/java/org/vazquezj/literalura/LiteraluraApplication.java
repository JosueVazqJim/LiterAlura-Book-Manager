package org.vazquezj.literalura;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.vazquezj.literalura.main.Main;
import org.vazquezj.literalura.repository.AuthorRepository;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	//inyection de dependencias de tipo: type-based injection
	@Autowired
	private AuthorRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Main main = new Main(repository);
		main.menu();
	}
}