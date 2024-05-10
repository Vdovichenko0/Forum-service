package telran.java52.post.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import telran.java52.post.model.Post;

public interface PostRepository extends MongoRepository<Post, String> {
	List<Post> findByAuthor(String author);
	List<Post> findByTagsIn(List<String> tags);
	List<Post> findByDateCreatedBetween(LocalDate dateFrom, LocalDate dateTo);
}
