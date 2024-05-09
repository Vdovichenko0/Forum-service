package telran.java52.post.model;

import java.time.LocalDateTime;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = {"user", "dateCreated"})
public class Comment {
	@Setter
	String user;
	@Setter
	String message;
	LocalDateTime dateCreated = LocalDateTime.now();
	int likes;

	public Comment(String user, String message) {
		this.user = user;
		this.message = message;
	}
	
	public void addLike() {
		likes++;
	}

}
