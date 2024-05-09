package telran.java52.post.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NewPostDto {
	String title;
	String content;
	Set<String> tags;
} 
