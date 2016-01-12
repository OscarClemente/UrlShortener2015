package urlshortener2015.goldenbrown.domain;

public class MultiplesURIs {

	private String hash;
	private String username;
	private String target;
	private String expression;
	
	public MultiplesURIs(String hash, String username, String target, String expression) {
		this.hash = hash;
		this.username = username;
		this.target = target;
		this.expression = expression;
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
	
	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}
}
