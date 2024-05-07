package telran.java52.forum.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentDto {
	 String user;
     String message;
     LocalDateTime dateCreated;
     Integer likes;
}
