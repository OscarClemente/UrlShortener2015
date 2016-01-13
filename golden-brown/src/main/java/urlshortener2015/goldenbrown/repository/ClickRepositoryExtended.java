package urlshortener2015.goldenbrown.repository;

import java.util.List;

import urlshortener2015.goldenbrown.domain.Click;

public interface ClickRepositoryExtended {

	List<Click> findByHash(String hash);

	Click save(Click cl);

	void update(Click cl);

	void delete(Long id);

	void deleteAll();

	Long count();

	List<Click> list(Long limit, Long offset);

	Long clicksByHash(String hash);

}
