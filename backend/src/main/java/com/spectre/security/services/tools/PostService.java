package com.spectre.security.services.tools;

import com.spectre.model.Post;
import com.spectre.model.User;
import com.spectre.payload.tools.PostRequestDto;
import com.spectre.payload.tools.PostResponseDto;
import com.spectre.repository.PostRepository;
import com.spectre.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createPost(PostRequestDto dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setUser(user);

        postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> getAllPosts() {
        return postRepository.findAllWithUser().stream()
            .map(post -> PostResponseDto.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .createdAt(post.getCreatedAt())
                    .username(post.getUser().getUsername())
                    .build())
            .toList();
    }
    @Transactional
    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }
}
