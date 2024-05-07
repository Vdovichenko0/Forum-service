package telran.java52.forum.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import telran.java52.forum.dto.CommentAddDto;
import telran.java52.forum.dto.PeriodDto;
import telran.java52.forum.dto.PostAddDto;
import telran.java52.forum.dto.PostDto;

@Component
public interface ForumService {
	    PostDto addPost(String user,PostAddDto postAddDto);

	    PostDto findPostById(String id);

	    void addLike(String id);
	    
	    List<PostDto> findPostsByAuthor(String author);

	    PostDto addComment(String id, String user, CommentAddDto commentAddDto);

	    PostDto deletePost(String id);
	    
	    List<PostDto> findPostsByTags(Set<String> tags);

	    List<PostDto> findPostsByPeriod(PeriodDto periodRequest);

	    PostDto updatePost(String id, PostAddDto postUpdateDto);
	
	
}
