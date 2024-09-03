package ch.hftm.blog;

import ch.hftm.blog.control.BlogPostService;
import ch.hftm.blog.control.CommentService;
import ch.hftm.blog.model.dto.BlogPostDTO;
import ch.hftm.blog.model.dto.CommentDTO;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
@Startup
public class AppStartup {
        @Inject
        BlogPostService blogService;

        @Inject
        CommentService commentService;

        @PostConstruct
        public void init() {
                initializeDummyBlogs();
                initializeDummyComments();
        }

        private void initializeDummyBlogs() {
                blogService.addBlogPost(
                                new BlogPostDTO(null,
                                                "First Blog Title",
                                                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.",
                                                "alice",
                                                null,
                                                null,
                                                null));

                blogService.addBlogPost(
                                new BlogPostDTO(null,
                                                "Second Blog Title",
                                                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.",
                                                "alice",
                                                null,
                                                null,
                                                null));
        }

        private void initializeDummyComments() {
                commentService.addCommentToBlog(1,
                                new CommentDTO(
                                                null,
                                                null,
                                                0,
                                                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum.",
                                                "alice",
                                                null,
                                                null));

                commentService.addCommentToBlog(1,
                                new CommentDTO(
                                                null,
                                                null,
                                                0,
                                                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr...",
                                                "alice",
                                                null,
                                                null));

                commentService.addCommentToBlog(4,
                                new CommentDTO(
                                                null,
                                                null,
                                                0,
                                                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua",
                                                "alice",
                                                null,
                                                null));
        }
}
