package urlshortener2015.goldenbrown.repository;

import urlshortener2015.goldenbrown.domain.Usuario;

public interface UsuarioRepository {

	Usuario findByUsername(String username);

	Usuario findByUsernameAndPassword(String username, String password);
	
	Usuario save(Usuario user, String rolAdmin);

	void update(Usuario user);

	void delete(String username);

}
