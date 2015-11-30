package urlshortener2015.common.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.DirectFieldAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import urlshortener2015.common.domain.Usuario;

@Repository
public class UsuarioRepositoryImpl implements UsuarioRepository {

	private static final Logger log = LoggerFactory
			.getLogger(UsuarioRepositoryImpl.class);

	private static final RowMapper<Usuario> rowMapper = new RowMapper<Usuario>() {
		@Override
		public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new Usuario(rs.getString("correo"), rs.getString("password"),
					rs.getString("nick"), rs.getString("rol_admin"));
		}
	};

	@Autowired
	protected JdbcTemplate jdbc;

	public UsuarioRepositoryImpl() {
	}

	public UsuarioRepositoryImpl(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	@Override
	public Usuario findByEmail(String correo) {
		try {
			return jdbc.queryForObject("SELECT * FROM usuario WHERE correo=?",
					rowMapper, correo);
		} catch (Exception e) {
			log.debug("When select for email " + correo, e);
			return null;
		}
	}

	@Override
	public Usuario save(Usuario user) {
		try {
			jdbc.update("INSERT INTO usuario VALUES (?,?,?,?)",
					user.getCorreo(), user.getPassword(), user.getNick(),
					user.getRolAdmin());
		} catch (DuplicateKeyException e) {
			log.debug("When insert for email " + user.getCorreo(), e);
			return null;
		} catch (Exception e) {
			log.debug("When insert", e);
			return null;
		}
		return user;
	}

	@Override
	public Usuario setRol(Usuario user, boolean rol_admin) {
		try {
			jdbc.update("UPDATE usuario SET rol_admin=? WHERE correo=?", rol_admin,
					user.getCorreo());
			Usuario res = new Usuario();
			BeanUtils.copyProperties(user, res);
			new DirectFieldAccessor(res).setPropertyValue("rol_admin", rol_admin);
			return res;
		} catch (Exception e) {
			log.debug("When update", e);
			return null;
		}
	}

	@Override
	public void update(Usuario user) {
		try {
			jdbc.update(
					"update usuario set correo=?, password=?, nick=?, rol_admin=? where correo=?",
					user.getCorreo(), user.getNick(), user.getNick(), user.getRolAdmin());
		} catch (Exception e) {
			log.debug("When update for email " + user.getCorreo(), e);
		}
	}

	@Override
	public void delete(String correo) {
		try {
			jdbc.update("delete from usuario where correo=?", correo);
		} catch (Exception e) {
			log.debug("When delete for email " + correo, e);
		}
	}
}
