package telran.java52.post.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {
	String id;
    String title;
    String content;
    String author;
    LocalDateTime dateCreated;
    @Singular
    Set<String> tags;
    Integer likes;
    @Singular
    List<CommentDto> comments;
}
