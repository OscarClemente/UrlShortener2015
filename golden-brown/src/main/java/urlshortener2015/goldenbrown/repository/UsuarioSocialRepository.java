package urlshortener2015.goldenbrown.repository;

import urlshortener2015.goldenbrown.domain.UserConnection;
import urlshortener2015.goldenbrown.domain.UserProfile;

public interface UsuarioSocialRepository {

	UserProfile getUserProfile(String userId);

	UserConnection getUserConnection(String userId);

	void createUser(String userId, UserProfile profile);

}