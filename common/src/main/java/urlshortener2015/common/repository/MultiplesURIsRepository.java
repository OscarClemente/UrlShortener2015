package urlshortener2015.common.repository;

import java.util.List;

import urlshortener2015.common.domain.MultiplesURIs;

public interface MultiplesURIsRepository {

	MultiplesURIs findByKey(String id);

	//List<MultiplesURIs> findByTarget(String target);

	MultiplesURIs save(MultiplesURIs su);

	void update(MultiplesURIs su);

	void delete(String id);

	//Long count();

	List<MultiplesURIs> list(Long limit, Long offset);

}
