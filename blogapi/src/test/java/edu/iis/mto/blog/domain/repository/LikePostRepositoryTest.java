package edu.iis.mto.blog.domain.repository;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class LikePostRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikePostRepository likePostRepository;

    private BlogPost blogPost;
    private LikePost likePost;
    private User user;
    private User blogPostuser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        userRepository.flush();
        likePostRepository.deleteAll();
        likePostRepository.flush();
        blogPostRepository.deleteAll();
        blogPostRepository.flush();

        user = new User();
        user.setEmail("john@domain.com");
        user.setFirstName("Jan");
        user.setAccountStatus(AccountStatus.NEW);
        userRepository.save(user);

        blogPost = new BlogPost();
        blogPost.setEntry("Hi");
        List<LikePost> likes = new ArrayList<>();
        blogPost.setLikes(likes);

        blogPostuser = new User();
        blogPostuser.setEmail("johnson@domain.com");
        blogPostuser.setFirstName("Janusz");
        blogPostuser.setAccountStatus(AccountStatus.NEW);
        userRepository.save(blogPostuser);

        blogPost.setUser(blogPostuser);

        likePost = new LikePost();
        likePost.setUser(user);
        likePost.setPost(blogPost);

        blogPostRepository.save(blogPost);

    }
    @Test
    void zeroPostLikes() {
        Optional<LikePost> optional = likePostRepository.findByUserAndPost(user, blogPost);
        assertFalse(optional.isPresent());
    }

    @Test
    void shouldAddNewPost() {
        likePost.setPost(blogPost);
        blogPost.getLikes().add(likePost);
        LikePost testLikePost = likePostRepository.save(likePost);

        assertThat(testLikePost.getId(), notNullValue());
    }
    @Test
    void likePostIsPresent() {
        likePost.setPost(blogPost);
        blogPost.getLikes().add(likePost);
        LikePost testLikePost = likePostRepository.save(likePost);
        Optional<LikePost> optional = likePostRepository.findByUserAndPost(user, blogPost);

        assertTrue(optional.isPresent());
        assertThat(optional.get().getUser().getFirstName(),  equalTo(testLikePost.getUser().getFirstName()));
        assertThat(optional.get().getUser().getEmail(),  equalTo(testLikePost.getUser().getEmail()));
    }


}
