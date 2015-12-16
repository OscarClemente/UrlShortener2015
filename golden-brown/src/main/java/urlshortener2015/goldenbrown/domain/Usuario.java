package urlshortener2015.goldenbrown.domain;

public class Usuario {

	private String username;
	private String nick;
	private String password;
	
	public Usuario(String username, String nick, String password) {
		this.username=username;
		this.nick=nick;
		this.password=password;
	}

	public Usuario() {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
