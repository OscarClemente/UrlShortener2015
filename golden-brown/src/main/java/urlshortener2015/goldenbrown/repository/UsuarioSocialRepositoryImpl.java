package urlshortener2015.goldenbrown.repository;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import urlshortener2015.goldenbrown.domain.UserConnection;
import urlshortener2015.goldenbrown.domain.UserProfile;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UsuarioSocialRepositoryImpl implements UsuarioSocialRepository {

    private static final Logger log = LoggerFactory.getLogger(UsuarioSocialRepositoryImpl.class);

	@Autowired
	protected JdbcTemplate jdbc;
	
	public UsuarioSocialRepositoryImpl() {
	}

	public UsuarioSocialRepositoryImpl(JdbcTemplate jdbc) {
		this.jdbc=jdbc;
	}

    @Override
    public UserProfile getUserProfile(final String userId) {
        log.debug("SQL SELECT ON userprofile: {}", userId);

        return jdbc.queryForObject("SELECT * FROM userprofile WHERE userid = ?",
            new RowMapper<UserProfile>() {
                public UserProfile mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new UserProfile(userId, rs.getString("name"), rs.getString("firstname"),
                    rs.getString("lastname"), rs.getString("email"), rs.getString("username"));
                }
            }, userId);
    }

    @Override
    public UserConnection getUserConnection(final String userId) {
        log.debug("SQL SELECT ON userconnection: {}", userId);

        return jdbc.queryForObject("SELECT * FROM userconnection WHERE userid = ?",
            new RowMapper<UserConnection>() {
                public UserConnection mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new UserConnection(userId, rs.getString("providerid"), rs.getString("provideruserid"),
                		rs.getInt("rank"), rs.getString("displayname"), rs.getString("profileurl"),
                		rs.getString("imageurl"), rs.getString("accesstoken"), rs.getString("secret"),
                		rs.getString("refreshtoken"), rs.getLong("expiretime"));
                }
            }, userId);
    }

    @Override
    public void createUser(String userId, UserProfile profile) {
		try {
	    	if (log.isDebugEnabled()) {
	        	log.debug("SQL INSERT ON users, authorities and userProfile: " + userId + " with profile: " +
	        			profile.getEmail() + ", " + profile.getFirstName() + ", " + profile.getLastName() + ", " +
	            		profile.getName() + ", " + profile.getUsername());
	        }
	        jdbc.update("INSERT INTO users(username, nick, password, enabled) values (?,?,?,?)", userId, profile.getUsername(), RandomStringUtils.randomAlphanumeric(8), true);
	        jdbc.update("INSERT INTO authorities(username, authority) values (?,?)", userId, "ROLE_USER");
	        jdbc.update("INSERT INTO userprofile(userid, email, firstname, lastname, name, username) values (?,?,?,?,?,?)",
	            userId, profile.getEmail(), profile.getFirstName(), profile.getLastName(), profile.getName(),
	            profile.getUsername());
		} catch (DuplicateKeyException e) {
			log.debug("When insert for username " + userId, e);
		} catch (Exception e) {
			log.debug("When insert", e);
		}
    }
}