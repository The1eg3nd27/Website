package com.spectre.security.services.tools;


import com.spectre.model.Post;
import com.spectre.model.User;
import com.spectre.payload.tools.PostRequestDto;
import com.spectre.payload.tools.PostResponseDto;
import com.spectre.repository.PostRepository;
import com.spectre.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public List<PostResponseDto> getAllPostDtos() {
        return postRepository.findAll().stream()
                .map(PostResponseDto::fromEntity)
                .toList();
    }

    public PostResponseDto getPostDtoById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        return PostResponseDto.fromEntity(post);
    }

    public PostResponseDto createPost(PostRequestDto request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Post post = new Post(request.getTitle(), request.getContent(), user);
        Post savedPost = postRepository.save(post);

        return PostResponseDto.fromEntity(savedPost);
    }

    public void deletePost(Long id, String username) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        if (!post.getAuthor().getUsername().equals(username)) {
            throw new SecurityException("Unauthorized to delete this post");
        }

        postRepository.delete(post);
    }

    public void adminDeletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        postRepository.delete(post);
    }
}
