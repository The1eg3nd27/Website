package com.spectre.security.permission;

import com.spectre.model.Post;
import com.spectre.model.User;
import com.spectre.repository.PostRepository;
import com.spectre.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("postSecurity")
@RequiredArgsConstructor
public class PostSecurity {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public boolean isOwner(Long postId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) return false;

        Post post = postRepository.findById(postId).orElse(null);
        return post != null && post.getUser().getId().equals(user.getId());
    }
}
