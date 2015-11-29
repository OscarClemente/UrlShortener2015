package urlshortener2015.common.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import urlshortener2015.common.domain.Usuario;


@Repository
public class UsuarioRepositoryImpl implements UsuarioRepository{

	private static final Logger log = LoggerFactory
			.getLogger(UsuarioRepositoryImpl.class);

	private static final RowMapper<Usuario> rowMapper = new RowMapper<Usuario>() {

		@Override
		public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new Usuario(rs.getString("correo"), rs.getString("password"), rs.getString("nick"));
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
	public Usuario findByKey(String id) {
		try {
			return jdbc.queryForObject("SELECT * FROM Usuario WHERE correo=?",
					rowMapper, id);
		} catch (Exception e) {
			log.debug("When select for key " + id, e);
			return null;
		}
	}

	@Override
	public Usuario save(Usuario su) {
		try {
			jdbc.update("INSERT INTO Usuario VALUES (?,?,?)",
					su.getCorreo(), su.getPassword(), su.getNick());
		} catch (DuplicateKeyException e) {
			log.debug("When insert for key " + su.getCorreo(), e);
			return su;
		} catch (Exception e) {
			log.debug("When insert", e);
			return null;
		}
		return su;
	}

	@Override
	public void update(Usuario su) {
		try {
			jdbc.update(
					"update Usuario set password=?, set nick=? where correo=?",
					su.getPassword(), su.getNick(), su.getCorreo());
		} catch (Exception e) {
			log.debug("When update for hash " + su.getCorreo(), e);
		}
	}

	@Override
	public void delete(String hash) {
		try {
			jdbc.update("delete from Usuario where correo=?", hash);
		} catch (Exception e) {
			log.debug("When delete for hash " + hash, e);
		}
	}

	public void deleteAll() {
		try {
			jdbc.update("delete from usuario");
		} catch (Exception e) {
			log.debug("When delete all", e);
		}
	}

	@Override
	public List<Usuario> list(Long limit, Long offset) {
		try {
			return jdbc.query("SELECT * FROM Usuario LIMIT ? OFFSET ?",
					new Object[] { limit, offset }, rowMapper);
		} catch (Exception e) {
			log.debug("When select for limit " + limit + " and offset "
					+ offset, e);
			return null;
		}
	}

}
