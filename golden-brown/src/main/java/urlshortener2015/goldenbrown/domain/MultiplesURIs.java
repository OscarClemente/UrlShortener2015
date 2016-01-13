package urlshortener2015.goldenbrown.domain;

public class MultiplesURIs {

	private String hash;
	private String target;
	private String expression;
	
	public MultiplesURIs(String hash, String target, String expression) {
		this.hash = hash;
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
	
	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}
}
