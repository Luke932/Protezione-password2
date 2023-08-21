package luke.SpringSecurity.users.payloads;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class NewUserResponsePayload {
	private UUID id;
}
