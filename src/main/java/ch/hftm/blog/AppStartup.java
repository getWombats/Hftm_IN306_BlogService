package ch.hftm.blog;

import ch.hftm.blog.control.BlogService;
import ch.hftm.blog.model.entity.Blog;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
@Startup 
public class AppStartup {
    
    @Inject
    BlogService blogService;

    @PostConstruct
    public void init() {
        // blogService.addBlog(new Blog(null, "Ich und mein Quarkus", "Quarkus ist ein Framework, das auf Java basiert und für die Entwicklung von Cloud-nativen Anwendungen optimiert ist."));
        // blogService.addBlog(new Blog(null, "Was sind GET Requests?", "GET ist eine Methode, die von einem Client verwendet wird, um Daten von einem Server abzurufen."));
        // blogService.addBlog(new Blog(null, "Was ist ein POST Request?", "POST ist eine Methode, die von einem Client verwendet wird, um Daten an einen Server zu senden."));
        // blogService.addBlog(new Blog(null, "Was ist ein PUT Request?", "PUT ist eine Methode, die von einem Client verwendet wird, um Daten an einen Server zu senden und zu ersetzen."));
        // blogService.addBlog(new Blog(null, "Was ist ein PATCH Request?", "PATCH ist eine Methode, die von einem Client verwendet wird, um Daten an einen Server zu senden und zu aktualisieren."));
    }
}
