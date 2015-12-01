package urlshortener2015.goldenbrown.repository;

import urlshortener2015.goldenbrown.domain.Usuario;

public interface UsuarioRepository {

	Usuario findByEmail(String correo);

	Usuario save(Usuario user);

	Usuario setRol(Usuario user, boolean rol_admin);

	void update(Usuario user);

	void delete(String correo);

}
