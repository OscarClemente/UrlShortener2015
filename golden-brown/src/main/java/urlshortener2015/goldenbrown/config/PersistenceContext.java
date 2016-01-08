package urlshortener2015.goldenbrown.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import urlshortener2015.goldenbrown.repository.ClickRepositoryExtended;
import urlshortener2015.goldenbrown.repository.ClickRepositoryImplExtended;
import urlshortener2015.goldenbrown.repository.ShortURLRepositoryExtended;
import urlshortener2015.goldenbrown.repository.ShortURLRepositoryImplExtended;
import urlshortener2015.goldenbrown.repository.UsuarioRepository;
import urlshortener2015.goldenbrown.repository.UsuarioRepositoryImpl;
import urlshortener2015.goldenbrown.repository.UsuarioSocialRepository;
import urlshortener2015.goldenbrown.repository.UsuarioSocialRepositoryImpl;

@Configuration
public class PersistenceContext {
	
	@Autowired
    protected JdbcTemplate jdbc;
	
	@Bean
	ShortURLRepositoryExtended shortURLRepositoryExtended() {
		return new ShortURLRepositoryImplExtended(jdbc);
	}
 	
	@Bean
	ClickRepositoryExtended clickRepositoryExtended() {
		return new ClickRepositoryImplExtended(jdbc);
	}
	
	@Bean
	UsuarioRepository usuarioRepository() {
		return new UsuarioRepositoryImpl(jdbc);
	}
	
	@Bean
	UsuarioSocialRepository usuarioSocialRepository() {
		return new UsuarioSocialRepositoryImpl(jdbc);
	}
}
