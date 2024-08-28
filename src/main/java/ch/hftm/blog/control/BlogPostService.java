package ch.hftm.blog.control;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import ch.hftm.blog.model.domain.ServiceResponse;
import ch.hftm.blog.model.dto.BlogPostDTO;
import ch.hftm.blog.model.entity.BlogPost;
import ch.hftm.blog.repository.BlogPostRepository;
import ch.hftm.blog.util.BncConverter;
import ch.hftm.blog.util.RequestType;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.logging.Log;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class BlogPostService extends ServiceBase {
    @Inject
    BlogPostRepository blogRepository;

    public ServiceResponse<List<BlogPostDTO>> getAllBlogPostsWithOptionalFiltering(Optional<String> searchString,
            Optional<Long> page) {
        ServiceResponse<List<BlogPostDTO>> serviceResponse = new ServiceResponse<>(RequestType.GET);
        PanacheQuery<BlogPost> blogQuery;

        try {
            if (searchString == null || searchString.isEmpty()) {
                blogQuery = blogRepository.findAll();
            } else {
                blogQuery = blogRepository.find("title like ?1 or content like ?1", "%" + searchString.get() + "%");
            }
        } catch (Exception ex) {
            handleError(serviceResponse, ex, "Error while getting blogs");
            return serviceResponse;
        }

        List<BlogPost> blogs = blogQuery.page(Page.ofSize(20)).list();
        serviceResponse.setData(BncConverter.toBlogDtoCollection(blogs));
        return serviceResponse;
    }

    public ServiceResponse<BlogPostDTO> getBlogById(long blogId) {
        ServiceResponse<BlogPostDTO> serviceResponse = new ServiceResponse<>(RequestType.GET);
        BlogPost foundBlog;

        try {
            foundBlog = blogRepository.findById(blogId);
        } catch (Exception ex) {
            handleError(serviceResponse, ex, "Error while getting blog with id " + blogId);
            return serviceResponse;
        }

        if (foundBlog == null) {
            return serviceResponse;
        }

        serviceResponse.setData(BncConverter.toBlogDto(foundBlog));
        return serviceResponse;
    }

    @Transactional
    public ServiceResponse<BlogPostDTO> addBlogPost(BlogPostDTO blogDto) {
        ServiceResponse<BlogPostDTO> serviceResponse = new ServiceResponse<>(RequestType.POST);

        // ? hibernate error / bug?
        // ? Converting blog with BncConverter as static or service throws
        // ? IllegalArgumentException
        // ! Message:
        // ! Class 'class ch.hftm.blog.util.BncConverter$5' is not an entity
        // ! classontext-propagation
        // Blog blogToAdd = BncConverter.toBlogEntity(blogDto);

        // Blog blogToAdd = blogDto.toBlogEntity(); // EntityExistsException

        BlogPost blogToAdd = new BlogPost();
        blogToAdd.setTitle(blogDto.getTitle());
        blogToAdd.setContent(blogDto.getContent());
        blogToAdd.setAuthor(blogDto.getAuthor());
        blogToAdd.setCreatedAt(Instant.now());

        try {
            blogRepository.persist(blogToAdd);
        } catch (Exception ex) {
            handleError(serviceResponse, ex, "Error while persisting blog.");
            return serviceResponse;
        }

        if (blogRepository.isPersistent(blogToAdd)) {
            serviceResponse.setData(BncConverter.toBlogDto(blogToAdd));
        }

        return serviceResponse;
    }

    @Transactional
    public ServiceResponse<BlogPostDTO> deleteBlogPost(long blogId) {
        ServiceResponse<BlogPostDTO> serviceResponse = new ServiceResponse<>(RequestType.DELETE);

        BlogPost blogToBeDeleted = blogRepository.findById(blogId);

        if (blogToBeDeleted != null) {
            serviceResponse.setData(BncConverter.toBlogDto(blogToBeDeleted));
        }

        try {
            blogRepository.deleteById(blogId);
        } catch (Exception ex) {
            handleError(serviceResponse, ex, "Error while deleting blog with id " + blogId);
            return serviceResponse;
        }

        return serviceResponse;
    }

    @Transactional
    public boolean deleteAllBlogPosts() {
        try {
            blogRepository.deleteAll();
        } catch (Exception ex) {
            Log.error(ex.getMessage(), ex);
            return false;
        }

        return true;
    }

    @Transactional
    public ServiceResponse<BlogPostDTO> updateBlogPost(BlogPostDTO blogDto) {
        ServiceResponse<BlogPostDTO> serviceResponse = new ServiceResponse<>(RequestType.PATCH);

        BlogPost blogToUpdate;

        try {
            blogToUpdate = blogRepository.findById(blogDto.getId());
        } catch (Exception ex) {
            handleError(serviceResponse, ex, "Error while getting blog with id " + blogDto.getId() + " for update.");
            return serviceResponse;
        }

        if (blogToUpdate == null) {
            return serviceResponse;
        }

        blogToUpdate.setTitle(blogDto.getTitle());
        blogToUpdate.setContent(blogDto.getContent());
        blogToUpdate.setLastEditedAt(Instant.now());

        try {
            blogRepository.persist(blogToUpdate);
        } catch (Exception ex) {
            handleError(serviceResponse, ex, "Error while updating blog with id " + blogToUpdate.getId());
            return serviceResponse;
        }

        serviceResponse.setData(BncConverter.toBlogDto(blogToUpdate));
        return serviceResponse;
    }
}