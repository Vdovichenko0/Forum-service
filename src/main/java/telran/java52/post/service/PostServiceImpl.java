package telran.java52.post.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java52.post.dao.PostRepository;
import telran.java52.post.dto.DatePeriodDto;
import telran.java52.post.dto.NewCommentDto;
import telran.java52.post.dto.NewPostDto;
import telran.java52.post.dto.PostDto;
import telran.java52.post.model.Post;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {


	final PostRepository postRepository;
	final ModelMapper modelMapper;
	
	@Override
	public PostDto addNewPost(String author, NewPostDto newPostDto) {
		Post post = modelMapper.map(newPostDto, Post.class);
		post.setAuthor(author);
		postRepository.save(post); //witch id from mongo
		return modelMapper.map(post, PostDto.class);
	}

	@Override
	public PostDto findPostById(String id) {
		return null;
	}

	@Override
	public PostDto removePost(String id) {
		return null;
	}

	@Override
	public PostDto updatePost(String id, NewPostDto newPostDto) {
		return null;
	}

	@Override
	public PostDto addComment(String id, String author, NewCommentDto newCommentDto) {
		return null;
	}

	@Override
	public void addLike(String id) {
		
	}

	@Override
	public Iterable<PostDto> findPostsByAuthor(String author) {
		return null;
	}

	@Override
	public Iterable<PostDto> findPostsByTags(List<String> tags) {
		return null;
	}

	@Override
	public Iterable<PostDto> findPostsByPeriod(DatePeriodDto datePeriodDto) {
		return null;
	}

}
