package urlshortener2015.goldenbrown.service;

import java.util.UUID;

import urlshortener2015.goldenbrown.domain.UserProfile;
import urlshortener2015.goldenbrown.repository.UsuarioSocialRepository;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;

public class AccountConnectionSignUpService implements ConnectionSignUp {

	private final UsuarioSocialRepository usuarioSocialRepository;
	
    public AccountConnectionSignUpService(UsuarioSocialRepository usuarioSocialRepository) {
        this.usuarioSocialRepository = usuarioSocialRepository;
    }
    
    public String execute(Connection<?> connection) {
        org.springframework.social.connect.UserProfile profile = connection.fetchUserProfile();
        String userId = UUID.randomUUID().toString();
        usuarioSocialRepository.createUser(userId, new UserProfile(userId, profile));
        return userId;
    }
}