package ch.hftm.blog.repository;

import ch.hftm.blog.model.entity.BlogPost;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BlogPostRepository implements PanacheRepository<BlogPost> {
}