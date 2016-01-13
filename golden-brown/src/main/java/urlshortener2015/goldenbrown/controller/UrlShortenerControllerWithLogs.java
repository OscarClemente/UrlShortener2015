package urlshortener2015.goldenbrown.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.hash.Hashing;

import rita.wordnet.RiWordnet;

import org.json.JSONObject;
import org.json.JSONArray;

import urlshortener2015.common.web.UrlShortenerController;
import urlshortener2015.goldenbrown.domain.Click;
import urlshortener2015.goldenbrown.domain.MultiplesURIs;
import urlshortener2015.goldenbrown.domain.ShortURL;
import urlshortener2015.goldenbrown.domain.Usuario;
import urlshortener2015.goldenbrown.repository.ClickRepositoryExtended;
import urlshortener2015.goldenbrown.repository.MultiplesURIsRepository;
import urlshortener2015.goldenbrown.repository.ShortURLRepositoryExtended;
import urlshortener2015.goldenbrown.repository.UsuarioRepository;

@Controller
public class UrlShortenerControllerWithLogs {

	private static final Logger logger = LoggerFactory
			.getLogger(UrlShortenerControllerWithLogs.class);

	@Autowired
	protected ShortURLRepositoryExtended shortURLRepositoryExtended;
	
	@Autowired
	protected MultiplesURIsRepository multiplesURIsRepository;

	@Autowired
	protected ClickRepositoryExtended clickRepositoryExtended;

	@Autowired
	protected UsuarioRepository usuarioRepository;
	
    @Autowired
    private SocialController socialController;

	@RequestMapping(value = "/{name:(?!link|home|login|users|advert).*}", method = RequestMethod.GET)
	public ResponseEntity<?> redirectTo(@PathVariable String name,
			HttpServletRequest request) {
		logger.info("Requested redirection with hash " + name);
		ShortURL l = shortURLRepositoryExtended.findByKey(name);
		if (l != null) {
			createAndSaveClick(extractIP(request), name);
			
			String conditionalTarget = exprMatching(name, request);
			if (conditionalTarget != null) l.setTarget(conditionalTarget);
			
			return createSuccessfulRedirectToResponse(l);
		}
		else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	protected void createAndSaveClick(String ip, String name) {
		Click cl = new Click(null, name, new Date(System.currentTimeMillis()),
				null, null, null, ip, null);
		cl = clickRepositoryExtended.save(cl);
		logger.info(cl != null ? "[" + name + "] saved with id [" + cl.getId()
				+ "]" : "[" + name + "] was not saved");
	}

	protected String extractIP(HttpServletRequest request) {
		return request.getRemoteAddr();
	}

	protected ResponseEntity<?> createSuccessfulRedirectToResponse(ShortURL l) {
		HttpHeaders h = new HttpHeaders();
		if (!l.getAdvert()) {
			h.setLocation(URI.create(l.getTarget()));
			return new ResponseEntity<>(h, HttpStatus.valueOf(l.getMode()));
		}
		else {
			h.setLocation(URI.create(l.getSponsor() + "?hash=" + l.getHash()));
			return new ResponseEntity<>(h, HttpStatus.valueOf(l.getMode()));
		}
	}

	@RequestMapping(value = "/link", method = RequestMethod.POST)
	public ResponseEntity<?> shortener(@RequestParam("url") String url,
			@RequestParam(value = "sponsor", required = false) String sponsor,
			@RequestParam(value = "brand", required = false) String brand,
			@RequestParam(value = "urlName", required = false) String name,
			@RequestParam("username") String username,
			@RequestParam(value = "advert", required = false) String advertisement,
			@RequestParam(value = "max", required = false) String max,
			@RequestParam MultiValueMap<String, String> params,
			HttpServletRequest request, Principal currentUser, Model model) {
		logger.info("Requested new short for uri " + url);
		sponsor = "http://localhost:8080/advert";
		
		boolean useConditional = false;
		//Comprobamos si se usan URIs condicionales
		if(params.getFirst("ifurl1") != null && !params.getFirst("ifurl1").equals("")
				&& params.getFirst("expr1") != null && !params.getFirst("expr1").equals("")){
			useConditional = true;
		}
		
		logger.info("Name (hash) is " + name);
		//Si se usan URIs condicionales pero no hay url estandar,
		//se crea un hash usando las targets de las condicionales
		if (name.equals("") && url.equals("") && useConditional) {
			logger.info("There are Conditional URIs but no default target" + name);
			String ifurl = "ifurl";
			String expr = "expr";
			String nueva = "a";
			
			int paramn = 1;
			
			String firstparam = ifurl + paramn;
			String secondparam = expr + paramn;
			
			while((params.getFirst(firstparam) != null && params.getFirst(secondparam) != null)
					&& (!params.getFirst(firstparam).equals("") && !params.getFirst(secondparam).equals(""))) {
				nueva += params.getFirst(firstparam);
				
				paramn++;
				firstparam = ifurl + paramn; secondparam = expr + paramn;
			}
			
			name = Hashing.murmur3_32().hashString(url + username + nueva, StandardCharsets.UTF_8)
					.toString();
			while (shortURLRepositoryExtended.findByHash(name, username) != null) {
				// seguir creando
				name = Hashing.murmur3_32()
						.hashString(url + username + "" + nueva, StandardCharsets.UTF_8)
						.toString();
				nueva += "a";
			}			
		}	
		
		//Si no existe una hash (id or name) se crea una
		else if (name.equals("")) {
			name = Hashing.murmur3_32().hashString(url + username, StandardCharsets.UTF_8)
					.toString();
			String nueva = "a";
			while (shortURLRepositoryExtended.findByHash(name, username) != null) {
				// seguir creando
				name = Hashing.murmur3_32()
						.hashString(url + username + "" + nueva, StandardCharsets.UTF_8)
						.toString();
				nueva += "a";
			}
		}
		logger.info("After name (hash) is " + name);
		
		boolean advert = false;
		int seconds = 0;
		if (advertisement != null) {
			advert = true;
			if (!max.equals("")) {
				seconds = Integer.valueOf(max);
				if (seconds < 1) {
					seconds = 5;
				}
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

			RiWordnet wordnet = new RiWordnet();

			// Crea 10 sinonimos
			String[] poss = wordnet.getPos(name);
			/*
			 * for (int j = 0; j < poss.length; j++) {
			 * System.out.println("\n\nSynonyms for " + name + " (pos: " +
			 * poss[j] + ")");
			 */
			JSONArray sinonimos = new JSONArray();
			try {
				String[] synonyms = wordnet.getAllSynonyms(name, poss[0], 10);
				Arrays.sort(synonyms);
				JSONObject tmpo;
				for (int i = 0; i < synonyms.length; i++) {
					ShortURL l = shortURLRepositoryExtended.findByHash(synonyms[i], username);
					if (l == null) {
						// No existe en la BD, luego no esta cogido
						tmpo = new JSONObject();
						tmpo.put("synonym", synonyms[i]);
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
					.randomUUID().toString(), extractIP(request), name,	username, advert, seconds, useConditional);
			if (su != null) {
				HttpHeaders h = new HttpHeaders();
				h.setLocation(su.getUri());
				
				saveConditionalURIs(name, params);
				
				return new ResponseEntity<>(su, h, HttpStatus.CREATED);
			}
			else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}
	}
	
	@RequestMapping(value = "/hash", method = RequestMethod.GET)
	public ResponseEntity<ShortURL> hash(
			@RequestParam(value = "hash") String hash,
			HttpServletRequest request) {
		logger.info("Requested shortURL from hash " + hash);
		ShortURL shortURL = null;
		if (hash != null && !hash.equals("")) {
			shortURL = shortURLRepositoryExtended.findByKey(hash);
		}
		if (shortURL != null) {
			return new ResponseEntity<>(shortURL, HttpStatus.CREATED);
		}
		else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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

	@RequestMapping(value="/dataSocial", method = RequestMethod.GET)
	public ResponseEntity<Usuario> dataSocial(HttpServletRequest request) {
		logger.info("Requested current user logged");
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = user.getUsername();
		Usuario usuario = null;
		if (username != null && !username.equals("")) {
			usuario = usuarioRepository.findByUsername(username);
		}
		if (usuario != null) {
			return new ResponseEntity<>(usuario, HttpStatus.ACCEPTED);
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
			String brand, String owner, String ip, String name, String username, 
			boolean advert, int seconds, boolean useConditional) {
		UrlValidator urlValidator = new UrlValidator(new String[] { "http",
				"https" });
		if (urlValidator.isValid(url) || (url.equals("") && useConditional)) {
			ShortURL su = null;
			if (!name.equals("")) {
				su = new ShortURL(name, url, linkTo(
						methodOn(UrlShortenerControllerWithLogs.class)
								.redirectTo(name, null)).toUri(), sponsor,
						new Date(System.currentTimeMillis()), owner,
						HttpStatus.TEMPORARY_REDIRECT.value(), true, ip, null,
						username, advert, seconds);
			}
			else {
				String id = Hashing.murmur3_32()
						.hashString(url, StandardCharsets.UTF_8).toString();
				su = new ShortURL(id, url, linkTo(
						methodOn(UrlShortenerController.class).redirectTo(id,
								null)).toUri(), sponsor, new Date(
						System.currentTimeMillis()), owner,
						HttpStatus.TEMPORARY_REDIRECT.value(), true, ip, null,
						username, advert, seconds);
			}
			return shortURLRepositoryExtended.save(su);
		}
		else {
			return null;
		}
	}
	
	private String exprMatching(String hash, HttpServletRequest request) {
		List<MultiplesURIs> listmu = multiplesURIsRepository.listConditionals(hash);
		if (listmu != null) {
			logger.info("In expr Matching");
					
			for (int i = 0; i < listmu.size(); i++) {
		    	Pattern exprReg = Pattern.compile(listmu.get(i).getExpression()); //"(.*)max:([0-9]*)"
		    	Enumeration headerNames = request.getHeaderNames();
		    	while (headerNames.hasMoreElements()) {
		    		String key = (String) headerNames.nextElement();
		    		String value = request.getHeader(key);
			    	Matcher m = exprReg.matcher(value);
			    	if(m.matches()){
			    		logger.info("MATCH FOUND!!" + value + " - " + listmu.get(i).getExpression());
			    		return listmu.get(i).getTarget();
			    	}
		    	}
		    	logger.info("didnt find a match this iteration");
			}
			logger.info("match NOT found");
		}
		return null;
		
	}
	
	private void saveConditionalURIs(String hash, MultiValueMap<String, String> params) {
		
		logger.info("Printing MultiValueMap params in next line");
		logger.info(":" + params);
		
		String ifurl = "ifurl";
		String expr = "expr";
		
		int paramn = 1;
		
		String firstparam = ifurl + paramn;
		String secondparam = expr + paramn;
		
		while((params.getFirst(firstparam) != null && params.getFirst(secondparam) != null)
				&& (!params.getFirst(firstparam).equals("") && !params.getFirst(secondparam).equals(""))) {
			logger.info("Seeking url&expression -- " + firstparam + params.getFirst(firstparam) + secondparam + params.getFirst(secondparam));
			MultiplesURIs mu = new MultiplesURIs(hash, params.getFirst(firstparam), params.getFirst(secondparam));
			multiplesURIsRepository.save(mu);
			
			paramn++;
			firstparam = ifurl + paramn; secondparam = expr + paramn;
		}
		
		logger.info("Already cicled around " + paramn + " conditional params");
	}
}
