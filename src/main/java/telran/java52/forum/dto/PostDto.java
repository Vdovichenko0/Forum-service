package telran.java52.forum.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
	String id;
	String title;
	String content;
	String author;
	LocalDateTime dateCreated;
	Set<String> tags;
	int likes;
	List<CommentDto> comments;
}
