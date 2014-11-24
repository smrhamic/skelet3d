package atlas.manager.converter;

import atlas.entity.Image;
import atlas.service.ImageService;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Michal
 */
@ViewScoped
@Named("imageManager")
public class ImageManager implements Serializable {

    private static final long serialVersionUID = 1L;
    
    // persistence services
    @EJB
    ImageService imageService;

    // bound properties
    private List<Image> allImages;

    @PostConstruct
    private void init() {
        allImages = imageService.findAll();
    }

    public List<Image> getAllImages() {
        return allImages;
    }
 
}
