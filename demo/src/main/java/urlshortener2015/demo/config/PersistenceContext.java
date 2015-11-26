package urlshortener2015.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import urlshortener2015.common.repository.ClickRepository;
import urlshortener2015.common.repository.ClickRepositoryImpl;
import urlshortener2015.common.repository.MultiplesURIsRepository;
import urlshortener2015.common.repository.MultiplesURIsRepositoryImpl;
import urlshortener2015.common.repository.ShortURLRepository;
import urlshortener2015.common.repository.ShortURLRepositoryImpl;
import urlshortener2015.common.repository.UsuarioRepository;
import urlshortener2015.common.repository.UsuarioRepositoryImpl;

@Configuration
public class PersistenceContext {

	@Autowired
    protected JdbcTemplate jdbc;

	@Bean
	ShortURLRepository shortURLRepository() {
		return new ShortURLRepositoryImpl(jdbc);
	}
 	
	@Bean
	ClickRepository clickRepository() {
		return new ClickRepositoryImpl(jdbc);
	}
	
	@Bean
	UsuarioRepository usuarioRepository() {
		return new UsuarioRepositoryImpl(jdbc);
	}
	
	@Bean
	MultiplesURIsRepository multiplesURIsRepository() {
		return new MultiplesURIsRepositoryImpl(jdbc);
	}
	
}
