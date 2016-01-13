package urlshortener2015.goldenbrown.repository;

import java.util.List;

import urlshortener2015.goldenbrown.domain.MultiplesURIs;

public interface MultiplesURIsRepository {

	MultiplesURIs findByKey(String id);
	
	MultiplesURIs findByHash(String hash, String username);

	//List<MultiplesURIs> findByTarget(String target);

	MultiplesURIs save(MultiplesURIs su);

	void update(MultiplesURIs su);

	void delete(String id);

	//Long count();

	List<MultiplesURIs> list(Long limit, Long offset);

	List<MultiplesURIs> listConditionals(String hash);

}
