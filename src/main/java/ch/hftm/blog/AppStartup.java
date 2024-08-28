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
                // initializeDummyBlogs();
                // initializeDummyComments();
        }

        private void initializeDummyBlogs() {
                blogService.addBlogPost(
                                new BlogPostDTO(null,
                                                "Ich und mein Quarkus",
                                                "Quarkus ist ein Framework, das auf Java basiert und für die Entwicklung von Cloud-nativen Anwendungen optimiert ist.",
                                                "Chuck Norris",
                                                null,
                                                null,
                                                null));

                blogService.addBlogPost(
                                new BlogPostDTO(null,
                                                "Was sind GET Requests?",
                                                "GET ist eine Methode, die von einem Client verwendet wird, um Daten von einem Server abzurufen.",
                                                "Chuck Norris",
                                                null,
                                                null,
                                                null));

                blogService.addBlogPost(
                                new BlogPostDTO(null,
                                                "Was ist ein POST Request?",
                                                "POST ist eine Methode, die von einem Client verwendet wird, um Daten an einen Server zu senden.",
                                                "Chuck Norris",
                                                null,
                                                null,
                                                null));

                blogService.addBlogPost(
                                new BlogPostDTO(null,
                                                "Was ist ein PUT Request?",
                                                "PUT ist eine Methode, die von einem Client verwendet wird, um Daten an einen Server zu senden und zu ersetzen.",
                                                "Chuck Norris",
                                                null,
                                                null,
                                                null));

                blogService.addBlogPost(
                                new BlogPostDTO(null,
                                                "Was ist ein PATCH Request?",
                                                "PATCH ist eine Methode, die von einem Client verwendet wird, um Daten an einen Server zu senden und zu aktualisieren.",
                                                "Chuck Norris",
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
                                                "Ja Quarkus ist schon ganz gut aber ab und zu verwirrend, vorallem mit hibernate orm...",
                                                "Chuck Norris",
                                                null,
                                                null));

                commentService.addCommentToBlog(4,
                                new CommentDTO(
                                                null,
                                                null,
                                                0,
                                                "PUT Requests sind irgendwie nutzlos. Eine Mischung zwischen POST und PATCH... das Einsatzgebiet dafür erscheint mir nicht ganz plausibel.",
                                                "Chuck Norris",
                                                null,
                                                null));
        }
}
