package urlshortener2015.goldenbrown.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import urlshortener2015.goldenbrown.domain.MultiplesURIs;

@Repository
public class MultiplesURIsRepositoryImpl implements MultiplesURIsRepository {

	private static final Logger log = LoggerFactory
			.getLogger(MultiplesURIsRepositoryImpl.class);

	private static final RowMapper<MultiplesURIs> rowMapper = new RowMapper<MultiplesURIs>() {

		@Override
		public MultiplesURIs mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new MultiplesURIs(rs.getString("hash"), rs.getString("target"),
					rs.getString("expression"));
		}
	};

	@Autowired
	protected JdbcTemplate jdbc;

	public MultiplesURIsRepositoryImpl() {
	}

	public MultiplesURIsRepositoryImpl(JdbcTemplate jdbc) {
		this.jdbc=jdbc;
	}

	@Override
	public MultiplesURIs findByKey(String id) {
		try {
			return jdbc.queryForObject("SELECT * FROM MultiplesURIs WHERE uri=?",
					rowMapper, id);
		} catch (Exception e) {
			log.debug("When select for key " + id, e);
			return null;
		}
	}
	
	@Override
	public MultiplesURIs findByHash(String hash, String username) {
		try {
			return jdbc.queryForObject("SELECT * FROM MultiplesURIs WHERE hash=? AND username=?",
					rowMapper, hash, username);
		} catch (Exception e) {
			log.debug("When select for hash " + hash, e);
			return null;
		}
	}

	@Override
	public MultiplesURIs save(MultiplesURIs su) {
		try {
			jdbc.update("INSERT INTO multiplesuris VALUES (?,?,?)",
					su.getHash(), su.getTarget(), su.getExpression());
		} catch (DuplicateKeyException e) {
			log.debug("When insert for key " + su.getHash(), e);
			return null;
		} catch (Exception e) {
			log.debug("When insert", e);
			return null;
		}
		return su;
	}
	
	@Override
	public void update(MultiplesURIs su) {
		try {
			jdbc.update(
					"update MultiplesURIs set target=? where hash=?",
					su.getTarget(), su.getHash());
		} catch (Exception e) {
			log.debug("When update for hash " + su.getHash(), e);
		}
	}
	
	@Override
	public void delete(String hash) {
		try {
			jdbc.update("delete from MultiplesURIs where hash=?", hash);
		} catch (Exception e) {
			log.debug("When delete for hash " + hash, e);
		}
	}
	
	public void deleteAll() {
		try {
			jdbc.update("delete from MultiplesURIs");
		} catch (Exception e) {
			log.debug("When delete all", e);
		}
	}
	
	@Override
	public List<MultiplesURIs> list(Long limit, Long offset) {
		try {
			return jdbc.query("SELECT * FROM MultiplesURIs LIMIT ? OFFSET ?",
					new Object[] { limit, offset }, rowMapper);
		} catch (Exception e) {
			log.debug("When select for limit " + limit + " and offset "
					+ offset, e);
			return null;
		}
	}
	
	@Override
	public List<MultiplesURIs> listConditionals(String hash) {
		try {
			return jdbc.query("SELECT * FROM MultiplesURIs WHERE hash = ?",
					new Object[] { hash }, rowMapper);
		} catch (Exception e) {
			log.debug("When select for target " + hash , e);
			return Collections.emptyList();
		}
	}

}