package urlshortener2015.goldenbrown.web;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.hash.Hashing;

import rita.RiWordNet;

import org.json.JSONObject;
import org.json.JSONArray;

import urlshortener2015.common.domain.Click;
import urlshortener2015.common.repository.ClickRepository;
import urlshortener2015.common.web.UrlShortenerController;
import urlshortener2015.goldenbrown.domain.ShortURL;
import urlshortener2015.goldenbrown.domain.Usuario;
import urlshortener2015.goldenbrown.repository.ShortURLRepositoryExtended;
import urlshortener2015.goldenbrown.repository.UsuarioRepository;

@Controller
public class UrlShortenerControllerWithLogs {

	private static final Logger logger = LoggerFactory
			.getLogger(UrlShortenerControllerWithLogs.class);

	@Autowired
	protected ShortURLRepositoryExtended shortURLRepositoryExtended;

	@Autowired
	protected ClickRepository clickRepository;

	@Autowired
	protected UsuarioRepository usuarioRepository;

	@RequestMapping(value = "/{name:(?!link|home|login|users).*}", method = RequestMethod.GET)
	public ResponseEntity<?> redirectTo(@PathVariable String name,
			HttpServletRequest request) {
		logger.info("Requested redirection with hash " + name);
		ShortURL l = shortURLRepositoryExtended.findByKey(name);
		if (l != null) {
			createAndSaveClick(extractIP(request), name);
			return createSuccessfulRedirectToResponse(l);
		}
		else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	protected void createAndSaveClick(String ip, String name) {
		Click cl = new Click(null, name, new Date(System.currentTimeMillis()),
				null, null, null, ip, null);
		cl = clickRepository.save(cl);
		logger.info(cl != null ? "[" + name + "] saved with id [" + cl.getId()
				+ "]" : "[" + name + "] was not saved");
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
	public ResponseEntity<?> shortener(@RequestParam("url") String url,
			@RequestParam(value = "sponsor", required = false) String sponsor,
			@RequestParam(value = "brand", required = false) String brand,
			@RequestParam(value = "urlName", required = false) String name,
			@RequestParam("username") String username,
			HttpServletRequest request) {
		logger.info("Requested new short for uri " + url);
		sponsor = "http://github.com/UNIZAR-30246-WebEngineering";
		if (name.equals("")) {
			name = Hashing.murmur3_32().hashString(url, StandardCharsets.UTF_8)
					.toString();
			String nueva = "a";
			while (shortURLRepositoryExtended.findByHash(name, username) != null) {
				// seguir creando
				name = Hashing.murmur3_32()
						.hashString(url + "" + nueva, StandardCharsets.UTF_8)
						.toString();
				nueva += "a";
			}
		}
		if (shortURLRepositoryExtended.findByHash(name, username) != null &&
				url != null && !url.equals("")) {
			ArrayList<String> sufijos = new ArrayList<String>(); // contiene todos los sufijos
			sufijos.add("ada");
			sufijos.add("aco");
			sufijos.add("ado");
			JSONArray arr = new JSONArray();
			JSONObject tmp;
			try {
				for (int i = 0; i < sufijos.size(); i++) {
					ShortURL l = shortURLRepositoryExtended.findByHash(name + ""
							+ sufijos.get(i), username);
					if (l == null) {
						// No existe en la BD, luego no esta cogido
						tmp = new JSONObject();
						tmp.put("name", name + "" + sufijos.get(i));
						arr.put(tmp);
					}
				}
			}
			catch (Exception e) { }
			String devueltos = "'{\"names\":" + arr.toString() + "}'";

			// Would pass in a PApplet normally, but we don't need to here
			RiWordNet wordnet = new RiWordNet("C:\\Program Files (x86)\\WordNet\\2.1\\dict");

			// Crea 10 sinonimos
			String[] poss = wordnet.getPos(name);
			/*
			 * for (int j = 0; j < poss.length; j++) {
			 * System.out.println("\n\nSynonyms for " + name + " (pos: " +
			 * poss[j] + ")");
			 */
			String[] synonyms = wordnet.getAllSynonyms(name, poss[0], 10);
			Arrays.sort(synonyms);
			JSONArray sinonimos = new JSONArray();
			JSONObject tmpo;
			try {
				for (int i = 0; i < synonyms.length; i++) {
					ShortURL l = shortURLRepositoryExtended.findByHash(synonyms[i], username);
					if (l == null) {
						// No existe en la BD, luego no esta cogido
						tmpo = new JSONObject();
						tmpo.put("sinonimo", synonyms[i]);
						sinonimos.put(tmpo);
					}
				}
			}
			catch (Exception e) { }
			String synDevueltos = "'{\"synonyms\":" + sinonimos.toString() + "}'";
			String respuesta = devueltos + "separa" + synDevueltos;
			return new ResponseEntity<>(respuesta, HttpStatus.NOT_ACCEPTABLE);
		}
		else {
			ShortURL su = createAndSaveIfValid(url, sponsor, brand, UUID
					.randomUUID().toString(), extractIP(request), name,
					username);
			if (su != null) {
				HttpHeaders h = new HttpHeaders();
				h.setLocation(su.getUri());
				return new ResponseEntity<>(su, h, HttpStatus.CREATED);
			}
			else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<Usuario> register(
			@RequestParam(value = "nick") String nick,
			@RequestParam(value = "username") String username,
			@RequestParam(value = "password") String password,
			@RequestParam(value = "rolAdmin") String rolAdmin,
			HttpServletRequest request) {
		logger.info("Requested new user for username " + username);
		Usuario user = null;
		if (username != null && !username.equals("") && password != null
				&& !password.equals("") && nick != null && !nick.equals("")) {
			if (usuarioRepository.findByUsernameAndPassword(username, password) == null) {
				user = usuarioRepository.save(new Usuario(username, nick,
						password), rolAdmin);
			}
		}
		if (user != null) {
			return new ResponseEntity<>(user, HttpStatus.CREATED);
		}
		else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ResponseEntity<Usuario> update(
			@RequestParam(value = "nick") String nick,
			@RequestParam(value = "username") String username,
			@RequestParam(value = "password") String password,
			HttpServletRequest request) {
		logger.info("Requested update for user with username " + username);
		Usuario user = null;
		if (username != null && !username.equals("") && password != null
				&& !password.equals("") && nick != null && !nick.equals("")) {
			user = usuarioRepository.findByUsername(username);
			if (user != null) {
				usuarioRepository.update(new Usuario(username, nick, password));
			}
		}
		if (user != null) {
			return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
		}
		else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/dataUser", method = RequestMethod.GET)
	public ResponseEntity<Usuario> dataUser(
			@RequestParam(value = "username") String username) {
		logger.info("Requested data from user with username " + username);
		Usuario user = null;
		if (username != null && !username.equals("")) {
			user = usuarioRepository.findByUsername(username);
		}
		if (user != null) {
			return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
		}
		else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/listLinks", method = RequestMethod.POST)
	public ResponseEntity<List<ShortURL>> listLinks(
			@RequestParam(value = "username") String username,
			HttpServletRequest request) {
		logger.info("Requested list of links from user with username "
				+ username);
		List<ShortURL> list = null;
		if (username != null && !username.equals("")) {
			list = shortURLRepositoryExtended.findByUsername(username);
		}
		if (list != null) {
			return new ResponseEntity<>(list, HttpStatus.ACCEPTED);
		}
		else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/deleteRow", method = RequestMethod.POST)
	public ResponseEntity<Boolean> deleteRow(
			@RequestParam(value = "username") String username,
			@RequestParam(value = "hash") String hash,
			@RequestParam(value = "target") String target,
			HttpServletRequest request) {
		logger.info("Requested deletion of row with hash " + hash
				+ " and target " + target);
		boolean accepted = false;
		if (username != null && !username.equals("") && hash != null
				&& !hash.equals("") && target != null && !target.equals("")) {
			shortURLRepositoryExtended.deleteByHash(username, hash, target);
			accepted = true;
		}
		if (accepted) {
			return new ResponseEntity<>(accepted, HttpStatus.ACCEPTED);
		}
		else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/listUsers", method = RequestMethod.POST)
	public ResponseEntity<List<Usuario>> listUsers(
			@RequestParam(value = "username") String username,
			HttpServletRequest request) {
		logger.info("Requested list of users from admin with username "
				+ username);
		List<Usuario> list = null;
		if (username != null && !username.equals("")) {
			list = usuarioRepository.findUsers();
		}
		if (list != null) {
			return new ResponseEntity<>(list, HttpStatus.ACCEPTED);
		}
		else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/deleteLinksUser", method = RequestMethod.POST)
	public ResponseEntity<Boolean> deleteLinksUser(
			@RequestParam(value = "username") String username,
			HttpServletRequest request) {
		logger.info("Requested deletion of links from user with username "
				+ username);
		boolean borrado = false;
		if (username != null && !username.equals("")) {
			shortURLRepositoryExtended.deleteByUsername(username);
			borrado = true;
		}
		if (borrado) {
			return new ResponseEntity<>(borrado, HttpStatus.ACCEPTED);
		}
		else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	protected ShortURL createAndSaveIfValid(String url, String sponsor,
			String brand, String owner, String ip, String name, String username) {
		UrlValidator urlValidator = new UrlValidator(new String[] { "http",
				"https" });
		if (urlValidator.isValid(url)) {
			ShortURL su = null;
			if (!name.equals("")) {
				su = new ShortURL(name, url, linkTo(
						methodOn(UrlShortenerControllerWithLogs.class)
								.redirectTo(name, null)).toUri(), sponsor,
						new Date(System.currentTimeMillis()), owner,
						HttpStatus.TEMPORARY_REDIRECT.value(), true, ip, null,
						username);
			}
			else {
				String id = Hashing.murmur3_32()
						.hashString(url, StandardCharsets.UTF_8).toString();
				su = new ShortURL(id, url, linkTo(
						methodOn(UrlShortenerController.class).redirectTo(id,
								null)).toUri(), sponsor, new Date(
						System.currentTimeMillis()), owner,
						HttpStatus.TEMPORARY_REDIRECT.value(), true, ip, null,
						username);
			}
			return shortURLRepositoryExtended.save(su);
		}
		else {
			return null;
		}
	}
}
