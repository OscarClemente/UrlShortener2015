package urlshortener2015.goldenbrown.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import urlshortener2015.goldenbrown.domain.Usuario;

@Repository
public class UsuarioRepositoryImpl implements UsuarioRepository {

	private static final Logger log = LoggerFactory
			.getLogger(UsuarioRepositoryImpl.class);

	private static final RowMapper<Usuario> rowMapper = new RowMapper<Usuario>() {
		@Override
		public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new Usuario(rs.getString("username"), rs.getString("nick"),
					rs.getString("password"));
		}
	};
	
	@Autowired
	protected JdbcTemplate jdbc;
	
	public UsuarioRepositoryImpl() {
	}

	public UsuarioRepositoryImpl(JdbcTemplate jdbc) {
		this.jdbc=jdbc;
	}

	@Override
	public Usuario findByUsername(String username) {
		try {
			return jdbc.queryForObject("SELECT * FROM users WHERE username=?",
					rowMapper, username);
		} catch (Exception e) {
			log.debug("When select for username " + username, e);
			return null;
		}
	}
	
	@Override
	public Usuario findByUsernameAndPassword(String username, String password) {
		try {
			return jdbc.queryForObject("SELECT * FROM users WHERE username=? AND password=?",
					rowMapper, username, password);
		} catch (Exception e) {
			log.debug("When select for username " + username, e);
			return null;
		}
	}
	
	@Override
	public Usuario save(Usuario user, String rolAdmin) {
		try {
			jdbc.update("INSERT INTO users VALUES (?,?,?,?)",
					user.getUsername(), user.getNick(), user.getPassword(), true);
			jdbc.update("INSERT INTO authorities VALUES (?,?)",
					user.getUsername(), rolAdmin);
		} catch (DuplicateKeyException e) {
			log.debug("When insert for username " + user.getUsername(), e);
			return null;
		} catch (Exception e) {
			log.debug("When insert", e);
			return null;
		}
		return user;
	}

	@Override
	public void update(Usuario user) {
		try {
			jdbc.update(
					"UPDATE users SET nick=?, password=? WHERE username=?",
					user.getNick(), user.getPassword(), user.getUsername());
		} catch (Exception e) {
			log.debug("When update for username " + user.getUsername(), e);
		}
	}

	@Override
	public void delete(String username) {
		try {
			jdbc.update("DELETE FROM users WHERE username=?", username);
			jdbc.update("DETELE FROM authorities WHERE username=?", username);
		} catch (Exception e) {
			log.debug("When delete for username " + username, e);
		}
	}
}
