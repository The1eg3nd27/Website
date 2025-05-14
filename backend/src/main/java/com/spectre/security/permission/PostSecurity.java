package com.spectre.security.permission;

import com.spectre.model.Post;
import com.spectre.model.User;
import com.spectre.repository.PostRepository;
import com.spectre.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("postSecurity")
public class PostSecurity {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public boolean isAuthorOrAdmin(Long postId, String username) {
        Post post = postRepository.findById(postId).orElse(null);
        User user = userRepository.findByUsername(username).orElse(null);

        if (post == null || user == null) {
            return false;
        }

        boolean isAuthor = post.getAuthor().getId().equals(user.getId());
        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> role.getName().name().equals("ROLE_ADMIN"));

        return isAuthor || isAdmin;
    }
}
