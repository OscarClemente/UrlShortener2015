package urlshortener2015.goldenbrown.domain;

public class UsuarioURIs {

	private String hash;
	private String username;
	
	public UsuarioURIs(String hash, String username) {
		this.hash=hash;
		this.username=username;
	}

	public UsuarioURIs() {
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
