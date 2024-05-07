package telran.java52.forum.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostAddDto {
	String title;
	String content;
	Set<String> tags;
} 
