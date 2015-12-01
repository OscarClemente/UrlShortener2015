package urlshortener2015.goldenbrown.web;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.sql.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import urlshortener2015.common.domain.Click;
import urlshortener2015.common.domain.ShortURL;
import urlshortener2015.common.repository.ClickRepository;
import urlshortener2015.common.repository.ShortURLRepository;
import urlshortener2015.goldenbrown.domain.Usuario;
import urlshortener2015.goldenbrown.repository.UsuarioRepository;

@RestController
public class UrlShortenerControllerWithLogs {

	private static final Logger logger = LoggerFactory.getLogger(UrlShortenerControllerWithLogs.class);

	@Autowired
	protected ShortURLRepository shortURLRepository;

	@Autowired
	protected ClickRepository clickRepository;
	
	@Autowired
	protected UsuarioRepository usuarioRepository;

	String name;
	
	@RequestMapping(value = "/{name:(?!link).*}", method = RequestMethod.GET)
	public ResponseEntity<?> redirectTo(@PathVariable String name,
			HttpServletRequest request) {
		logger.info("Requested redirection with hash " + name);
		ShortURL l = shortURLRepository.findByKey(this.name);
		if (l != null) {
			createAndSaveClick(extractIP(request),this.name);
			return createSuccessfulRedirectToResponse(l);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	protected void createAndSaveClick(String ip, String name) {
		Click cl = new Click(null, name, new Date(System.currentTimeMillis()),
				null, null, null, ip, null);
		cl=clickRepository.save(cl);
		logger.info(cl!=null?"["+name+"] saved with id ["+cl.getId()+"]":"["+name+"] was not saved");
	}

	protected String extractIP(HttpServletRequest request) {
		return request.getRemoteAddr();
	}

	protected ResponseEntity<?> createSuccessfulRedirectToResponse(ShortURL l) {
		HttpHeaders h = new HttpHeaders();
		h.setLocation(URI.create(l.getTarget()));
		return new ResponseEntity<>(h, HttpStatus.valueOf(l.getMode()));
	}

	@RequestMapping(value = "/link", method = RequestMethod.POST)
	public ResponseEntity<ShortURL> shortener(@RequestParam("url") String url,
			@RequestParam(value = "sponsor", required = false) String sponsor,
			@RequestParam(value = "brand", required = false) String brand,
			@RequestParam("urlName") String name, HttpServletRequest request) {
		logger.info("Requested new short for uri " + url);
		this.name=name;
		ShortURL su = createAndSaveIfValid(url, sponsor, brand, UUID
				.randomUUID().toString(), extractIP(request), name);
		if (su != null) {
			HttpHeaders h = new HttpHeaders();
			h.setLocation(su.getUri());
			return new ResponseEntity<>(su, h, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<Usuario> register(@RequestParam(value = "nick") String nick,
			@RequestParam(value = "email") String email,
			@RequestParam(value = "password") String password,
			@RequestParam(value = "rolAdmin") String rolAdmin,
			HttpServletRequest request) {
		logger.info("Requested new user for email " + email);
		Usuario user = null;
		if (email != null && !email.equals("") && password != null
				&& !password.equals("") && nick != null && !nick.equals("")) {
			if (usuarioRepository.findByEmail(email) == null) {
				user = usuarioRepository.save(new Usuario(email, password, nick, rolAdmin));
			}
		}
		if (user != null) {
			return new ResponseEntity<>(user, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	protected ShortURL createAndSaveIfValid(String url, String sponsor,
			String brand, String owner, String ip, String name) {
		UrlValidator urlValidator = new UrlValidator(new String[] { "http",
		"https" });
		if (urlValidator.isValid(url)) {
			ShortURL su = new ShortURL(name, url,
					linkTo(
							methodOn(UrlShortenerControllerWithLogs.class).redirectTo(
									name, null)).toUri(), sponsor, new Date(
											System.currentTimeMillis()), owner,
					HttpStatus.TEMPORARY_REDIRECT.value(), true, ip, null);
			return shortURLRepository.save(su);
		} else {
			return null;
		}
	}
}

