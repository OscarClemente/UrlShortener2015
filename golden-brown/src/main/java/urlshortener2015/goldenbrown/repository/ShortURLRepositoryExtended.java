package urlshortener2015.goldenbrown.repository;

import java.util.List;

import urlshortener2015.common.domain.ShortURL;
import urlshortener2015.common.repository.ShortURLRepository;

public interface ShortURLRepositoryExtended extends ShortURLRepository {
	
	List<ShortURL> findByUsername(String id);

}
