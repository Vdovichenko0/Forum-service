package telran.java52.forum.controller;

import java.util.List;
import java.util.Set;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import telran.java52.forum.dto.CommentAddDto;
import telran.java52.forum.dto.PeriodDto;
import telran.java52.forum.dto.PostAddDto;
import telran.java52.forum.dto.PostDto;
import telran.java52.forum.service.ForumService;

@RestController
@RequiredArgsConstructor
public class ForumController {

	final ForumService forumService;

	@PutMapping("/forum/post/{user}")
	public PostDto addPost(@PathVariable String user, @RequestBody PostAddDto postAddDto) {
		return forumService.addPost(user,postAddDto);
	}

	@GetMapping("/forum/post/{postId}")
	public PostDto findPostById(@PathVariable String postId) {
		return forumService.findPostById(postId);
	}

	@PutMapping("/forum/post/{postId}/like")
	public void addLike(@PathVariable String postId) {
		forumService.addLike(postId);
	}

	@GetMapping("/forum/posts/author/{user}")
	public List<PostDto> findPostsByAuthor(@PathVariable String user) {
		return forumService.findPostsByAuthor(user);
	}

	@PutMapping("/forum/post/{postId}/comment/{user}")
	public PostDto addComment(@PathVariable String postId, @PathVariable String user,
			@RequestBody CommentAddDto commentAddDto) {
		return forumService.addComment(postId, user, commentAddDto);
	}
	
	@DeleteMapping("/forum/post/{postId}")
	public PostDto deletePost(@PathVariable String postId) {
		return forumService.deletePost(postId);
	}
	
	@PostMapping("/forum/posts/tags")
	public List<PostDto> findPostsByTags(@RequestBody Set<String> tags){
		return forumService.findPostsByTags(tags);
	}
	
	@PostMapping("/forum/posts/period")
	public List<PostDto> findPostsByPeriod(@RequestBody PeriodDto periodDto){
	    return forumService.findPostsByPeriod(periodDto);
	}
	
	@PutMapping("/forum/post/{postId}")
	public PostDto updatePost(@PathVariable String postId, @RequestBody PostAddDto postUpdateDto) {
		return forumService.updatePost(postId, postUpdateDto);
	}
	
}
