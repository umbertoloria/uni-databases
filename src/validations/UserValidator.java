package validations;

public class UserValidator {

	public static void validate(String email, String username, String password, String ruolo) throws Exception {
		if (!email.contains("@")) {
			throw new Exception("L'E-Mail fornita non è valida.");
		}
		if (username.length() < 3) {
			throw new Exception("L'username è troppo breve: minimo 3 caratteri.");
		}
		if (password.length() < 5) {
			throw new Exception("La password è troppo breve: minimo 5 caratteri.");
		}
		if (ruolo == null) {
			throw new Exception("Il ruolo fornito non è valido.");
		} else if (!ruolo.equals("NULL") && !ruolo.equals("Amministratore") && !ruolo.equals("Moderatore")) {
			throw new Exception("Il ruolo fornito non è valido.");
		}
	}

}
