package telran.java52.post.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CommentDto {
	 String user;
     String message;
     LocalDateTime dateCreated;
     Integer likes;
}
