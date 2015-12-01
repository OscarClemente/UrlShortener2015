package urlshortener2015.goldenbrown.domain;

public class MultiplesURIs {

	private String hash;
	private String target;
	
	public MultiplesURIs(String hash, String target) {
		this.hash=hash;
		this.target=target;
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
}
