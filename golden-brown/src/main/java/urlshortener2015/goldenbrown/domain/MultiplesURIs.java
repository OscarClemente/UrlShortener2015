package urlshortener2015.goldenbrown.domain;

public class MultiplesURIs {

	private String hash;
	private String target;
	private String username;
	
	public MultiplesURIs(String hash, String target, String username) {
		this.hash=hash;
		this.target=target;
		this.username=username;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
