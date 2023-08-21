package luke.SpringSecurity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import luke.SpringSecurity.exceptions.UnauthorizedException;
import luke.SpringSecurity.users.User;
import luke.SpringSecurity.users.UsersService;
import luke.SpringSecurity.users.payloads.LoginSuccessfullPayload;
import luke.SpringSecurity.users.payloads.UserLoginPayload;
import luke.SpringSecurity.users.payloads.UserRequestPayload;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	UsersService srv;

	@Autowired
	JWTTools jwtT;

	@Autowired
	PasswordEncoder bcrypt;

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public User saveUser(@RequestBody UserRequestPayload body) {
		body.setPassword(bcrypt.encode(body.getPassword()));
		body.setCreditCard("5389563214568975");
		User created = srv.create(body);

		return created;
	}

	@PostMapping("/login")
	public LoginSuccessfullPayload login(@RequestBody UserLoginPayload body) {
		User user = srv.findByEmail(body.getEmail());

		if (bcrypt.matches(body.getPassword(), user.getPassword())) {
			String token = jwtT.createToken(user);
			System.out.println(token);
			return new LoginSuccessfullPayload(token);

		} else {

			throw new UnauthorizedException("CREDENZIALI NON VALIDE");
		}

	}
}
