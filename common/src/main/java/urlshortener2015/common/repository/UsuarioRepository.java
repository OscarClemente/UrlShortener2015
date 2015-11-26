package urlshortener2015.common.repository;

import java.util.List;

import urlshortener2015.common.domain.Usuario;

public interface UsuarioRepository {

	Usuario findByKey(String id);

	//List<Usuario> findByTarget(String target);

	Usuario save(Usuario su);

	void update(Usuario su);

	void delete(String id);

	//Long count();

	List<Usuario> list(Long limit, Long offset);

}
