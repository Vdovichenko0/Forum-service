package telran.java52.post.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import telran.java52.post.model.Post;

public interface PostRepository extends MongoRepository<Post, String> {

}
