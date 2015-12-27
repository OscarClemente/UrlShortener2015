package urlshortener2015.goldenbrown.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import urlshortener2015.common.repository.ClickRepository;
import urlshortener2015.common.repository.ClickRepositoryImpl;
import urlshortener2015.goldenbrown.repository.ShortURLRepositoryExtended;
import urlshortener2015.goldenbrown.repository.ShortURLRepositoryImplExtended;
import urlshortener2015.goldenbrown.repository.UsuarioRepository;
import urlshortener2015.goldenbrown.repository.UsuarioRepositoryImpl;

@Configuration
public class PersistenceContext {
	
	@Autowired
    protected JdbcTemplate jdbc;
	
	@Bean
	ShortURLRepositoryExtended shortURLRepositoryExtended() {
		return new ShortURLRepositoryImplExtended(jdbc);
	}
 	
	@Bean
	ClickRepository clickRepository() {
		return new ClickRepositoryImpl(jdbc);
	}
	
	@Bean
	UsuarioRepository usuarioRepository() {
		return new UsuarioRepositoryImpl(jdbc);
	}
}
