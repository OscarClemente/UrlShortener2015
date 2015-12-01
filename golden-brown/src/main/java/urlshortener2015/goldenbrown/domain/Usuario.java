package urlshortener2015.goldenbrown.domain;

public class Usuario {

	private String correo;
	private String password;
	private String nick;
	private String rolAdmin;
	
	public Usuario(String correo, String password, String nick, String rolAdmin) {
		this.correo=correo;
		this.password=password;
		this.nick=nick;
		this.rolAdmin=rolAdmin;
	}

	public Usuario() {
	}
	
	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}
	
	public String getRolAdmin() {
		return rolAdmin;
	}

	public void setRolAdmin(String rolAdmin) {
		this.rolAdmin = rolAdmin;
	}
}
