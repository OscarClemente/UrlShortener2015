package urlshortener2015.goldenbrown.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import urlshortener2015.common.domain.ShortURL;
import urlshortener2015.common.repository.ShortURLRepositoryImpl;

@Repository
public class ShortURLRepositoryImplExtended extends ShortURLRepositoryImpl implements ShortURLRepositoryExtended {

	private static final Logger log = LoggerFactory
			.getLogger(ShortURLRepositoryImplExtended.class);

	private static final RowMapper<ShortURL> rowMapper = new RowMapper<ShortURL>() {
		@Override
		public ShortURL mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new ShortURL(rs.getString("hash"), rs.getString("target"),
					null, rs.getString("sponsor"), rs.getDate("created"),
					rs.getString("owner"), rs.getInt("mode"),
					rs.getBoolean("safe"), rs.getString("ip"),
					rs.getString("country"));
		}
	};

	@Autowired
	protected JdbcTemplate jdbc;

	public ShortURLRepositoryImplExtended() {
	}

	public ShortURLRepositoryImplExtended(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}
	
	@Override
	public List<ShortURL> findByUsername(String username) {
		try {
			return jdbc.query("SELECT s.hash, s.target, s.sponsor, s.owner, s.mode, s.safe,"
					+ " s.ip, s.country FROM shorturl s, usershorturl u WHERE s.hash=u.hash"
					+ " AND u.username=?",
					rowMapper, username);
		} catch (Exception e) {
			log.debug("When select for username " + username, e);
			return null;
		}
	}
}
