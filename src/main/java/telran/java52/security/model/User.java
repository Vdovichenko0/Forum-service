package telran.java52.security.model;

import java.security.Principal;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

@AllArgsConstructor
@Getter
@Builder
public class User implements Principal {
	private String name;
	@Singular
	private Set<String> roles;

}
