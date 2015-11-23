package urlshortener2015.common.domain;

public class Usuario {

	private String correo;
	private String password;
	private String nick;
	
	public Usuario(String correo, String password, String nick) {
		this.correo=correo;
		this.password=password;
		this.nick=nick;
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
}
