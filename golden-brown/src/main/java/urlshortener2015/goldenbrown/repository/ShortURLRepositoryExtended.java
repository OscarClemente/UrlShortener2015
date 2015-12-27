package urlshortener2015.goldenbrown.repository;

import java.util.List;

import urlshortener2015.goldenbrown.domain.ShortURL;

public interface ShortURLRepositoryExtended {

	ShortURL findByKey(String id);

	ShortURL findByHash(String hash, String username);

	List<ShortURL> findByTarget(String target);

	ShortURL save(ShortURL su);

	ShortURL mark(ShortURL urlSafe, boolean safeness);

	void update(ShortURL su);

	void delete(String id);

	Long count();

	List<ShortURL> list(Long limit, Long offset);
	
	List<ShortURL> findByUsername(String id);

	void deleteByHash(String username, String hash, String target);

	void deleteByUsername(String username);

}
