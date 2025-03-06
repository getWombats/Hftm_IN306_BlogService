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
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheQuery;
// import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.logging.Log;
import io.quarkus.panache.common.Page;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
// import jakarta.transaction.Transactional;

@ApplicationScoped
public class BlogPostService extends ServiceBase {
    @Inject
    BlogPostRepository blogRepository;

    // public Uni<ServiceResponse<List<BlogPostDTO>>> getAllBlogPostsWithOptionalFiltering(Optional<String> searchString,
    //         Optional<Long> page) {

    //     ServiceResponse<List<BlogPostDTO>> serviceResponse = new ServiceResponse<>(RequestType.GET);
    //     PanacheQuery<BlogPost> blogQuery;

    //     try {
    //         if (searchString == null || searchString.isEmpty()) {
    //             blogQuery = blogRepository.findAll();
    //         } else {
    //             blogQuery = blogRepository.find("title like ?1 or content like ?1", "%" +
    //                     searchString.get() + "%");
    //         }
    //     } catch (Exception ex) {
    //         handleError(serviceResponse, ex, "Error while getting blogs");
    //         return serviceResponse;
    //     }

    //     blogs.subscribe().with(b -> serviceResponse.setData(BncConverter.toBlogDtoCollection(b)));

    //     serviceResponse.setData(BncConverter.toBlogDtoCollection(blogs));
    // }

    // create getAllBlogPostsWithOptionalFiltering reactive with return type Uni<List<BlogPostDTO>>
    public Uni<List<BlogPostDTO>> getAllBlogPostsWithOptionalFiltering(Optional<String> searchString, Optional<Long> page) {
        PanacheQuery<BlogPost> blogQuery;

        if (searchString.isEmpty()) {
            blogQuery = blogRepository.findAll();
        } else {
            blogQuery = blogRepository.find("title like ?1 or content like ?1", "%" + searchString.get() + "%");
        }

        return blogQuery.page(Page.ofSize(10))
                .list()
                .map(BncConverter::toBlogDtoCollection);
    }

    public Uni<ServiceResponse<BlogPostDTO>> getBlogById(long blogId) {

        return blogRepository.findById(blogId)
                .map(blog -> {
                    ServiceResponse<BlogPostDTO> response = new ServiceResponse<>(RequestType.GET);
                    response.setData(BncConverter.toBlogDto(blog));
                    return response;
                })
                .onFailure().recoverWithItem(err -> {
                    ServiceResponse<BlogPostDTO> errorResponse = new ServiceResponse<>(RequestType.GET);
                    errorResponse.setError(err);
                    return errorResponse;
                });

        // ServiceResponse<BlogPostDTO> serviceResponse = new
        // ServiceResponse<>(RequestType.GET);
        // BlogPost foundBlog;

        // try {
        // foundBlog = blogRepository.findById(blogId);
        // } catch (Exception ex) {
        // handleError(serviceResponse, ex, "Error while getting blog with id " +
        // blogId);
        // return serviceResponse;
        // }

        // if (foundBlog == null) {
        // return serviceResponse;
        // }

        // serviceResponse.setData(BncConverter.toBlogDto(foundBlog));
        // return serviceResponse;
    }

    // @Transactional
    // public ServiceResponse<BlogPostDTO> addBlogPost(BlogPostDTO blogDto) {
    // ServiceResponse<BlogPostDTO> serviceResponse = new
    // ServiceResponse<>(RequestType.POST);

    // // ? hibernate error / bug?
    // // ? Converting blog with BncConverter as static or service throws
    // // ? IllegalArgumentException
    // // ! Message:
    // // ! Class 'class ch.hftm.blog.util.BncConverter$5' is not an entity
    // // ! classontext-propagation
    // // Blog blogToAdd = BncConverter.toBlogEntity(blogDto);

    // // Blog blogToAdd = blogDto.toBlogEntity(); // EntityExistsException

    // BlogPost blogToAdd = new BlogPost();
    // blogToAdd.setTitle(blogDto.getTitle());
    // blogToAdd.setContent(blogDto.getContent());
    // blogToAdd.setAuthor(blogDto.getAuthor());
    // blogToAdd.setCreatedAt(Instant.now());
    // blogToAdd.setApproved(blogDto.isApproved());

    // try {
    // blogRepository.persist(blogToAdd);
    // } catch (Exception ex) {
    // handleError(serviceResponse, ex, "Error while persisting blog.");
    // return serviceResponse;
    // }

    // if (blogRepository.isPersistent(blogToAdd)) {
    // serviceResponse.setData(BncConverter.toBlogDto(blogToAdd));
    // }

    // return serviceResponse;
    // }

    public Uni<ServiceResponse<BlogPostDTO>> addBlogPost(BlogPostDTO blogDto) {
        BlogPost blogToAdd = new BlogPost();
        blogToAdd.setTitle(blogDto.getTitle());
        blogToAdd.setContent(blogDto.getContent());
        blogToAdd.setAuthor(blogDto.getAuthor());
        blogToAdd.setCreatedAt(Instant.now());
        blogToAdd.setApproved(blogDto.isApproved());

        return blogRepository.persist(blogToAdd)
                .map(b -> {
                    ServiceResponse<BlogPostDTO> response = new ServiceResponse<>(RequestType.POST);
                    response.setData(BncConverter.toBlogDto(b));
                    return response;
                })
                .onFailure().recoverWithItem(err -> {
                    ServiceResponse<BlogPostDTO> errorResponse = new ServiceResponse<>(RequestType.POST);
                    errorResponse.setError(err);
                    return errorResponse;
                });
    }

    // @Transactional
    // public ServiceResponse<BlogPostDTO> deleteBlogPost(long blogId) {
    // ServiceResponse<BlogPostDTO> serviceResponse = new
    // ServiceResponse<>(RequestType.DELETE);

    // BlogPost blogToBeDeleted = blogRepository.findById(blogId);

    // if (blogToBeDeleted != null) {
    // serviceResponse.setData(BncConverter.toBlogDto(blogToBeDeleted));
    // }

    // try {
    // blogRepository.deleteById(blogId);
    // } catch (Exception ex) {
    // handleError(serviceResponse, ex, "Error while deleting blog with id " +
    // blogId);
    // return serviceResponse;
    // }

    // return serviceResponse;
    // }

    public Uni<ServiceResponse<BlogPostDTO>> deleteBlogPost(long blogId) {
        return blogRepository.findById(blogId)
                .flatMap(blog -> blogRepository.deleteById(blogId)
                        .map(deleted -> {
                            ServiceResponse<BlogPostDTO> response = new ServiceResponse<>(RequestType.DELETE);
                            response.setData(BncConverter.toBlogDto(blog));
                            return response;
                        }))
                .onFailure().recoverWithItem(err -> {
                    ServiceResponse<BlogPostDTO> errorResponse = new ServiceResponse<>(RequestType.DELETE);
                    errorResponse.setError(err);
                    return errorResponse;
                });
    }

    // @Transactional
    // public boolean deleteAllBlogPosts() {
    // try {
    // blogRepository.deleteAll();
    // } catch (Exception ex) {
    // Log.error(ex.getMessage(), ex);
    // return false;
    // }

    // return true;
    // }

    public Uni<Boolean> deleteAllBlogPosts() {
        return blogRepository.deleteAll()
                .map(deleted -> {
                    return true;
                })
                .onFailure().recoverWithItem(err -> {
                    Log.error(err.getMessage(), err);
                    return false;
                });
    }

    // @Transactional
    // public ServiceResponse<BlogPostDTO> updateBlogPost(BlogPostDTO blogDto) {
    // ServiceResponse<BlogPostDTO> serviceResponse = new
    // ServiceResponse<>(RequestType.PATCH);

    // BlogPost blogToUpdate;

    // try {
    // blogToUpdate = blogRepository.findById(blogDto.getId());
    // } catch (Exception ex) {
    // handleError(serviceResponse, ex, "Error while getting blog with id " +
    // blogDto.getId() + " for update.");
    // return serviceResponse;
    // }

    // if (blogToUpdate == null) {
    // return serviceResponse;
    // }

    // blogToUpdate.setTitle(blogDto.getTitle());
    // blogToUpdate.setContent(blogDto.getContent());
    // blogToUpdate.setLastEditedAt(Instant.now());
    // blogToUpdate.setApproved(blogDto.isApproved());

    // try {
    // blogRepository.persist(blogToUpdate);
    // } catch (Exception ex) {
    // handleError(serviceResponse, ex, "Error while updating blog with id " +
    // blogToUpdate.getId());
    // return serviceResponse;
    // }

    // serviceResponse.setData(BncConverter.toBlogDto(blogToUpdate));
    // return serviceResponse;
    // }

    public Uni<ServiceResponse<BlogPostDTO>> updateBlogPost(BlogPostDTO blogDto) {
        return blogRepository.findById(blogDto.getId())
                .flatMap(blog -> {
                    blog.setTitle(blogDto.getTitle());
                    blog.setContent(blogDto.getContent());
                    blog.setLastEditedAt(Instant.now());
                    blog.setApproved(blogDto.isApproved());

                    return blogRepository.persist(blog)
                            .map(updated -> {
                                ServiceResponse<BlogPostDTO> response = new ServiceResponse<>(RequestType.PATCH);
                                response.setData(BncConverter.toBlogDto(updated));
                                return response;
                            });
                })
                .onFailure().recoverWithItem(err -> {
                    ServiceResponse<BlogPostDTO> errorResponse = new ServiceResponse<>(RequestType.PATCH);
                    errorResponse.setError(err);
                    return errorResponse;
                });
    }
}