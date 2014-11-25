package atlas.manager;

import atlas.entity.Image;
import atlas.service.ImageService;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
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
        // get all images
        allImages = imageService.findAll();
        // sort by name, alphabetical, not case sensitive
        Collections.sort(allImages, new Comparator<Image>() {
            @Override
            public int compare(Image im1, Image im2 ) {
                return im1.getName().toLowerCase()
                        .compareTo(im2.getName().toLowerCase());
            }
        });
    }

    public List<Image> getAllImages() {
        return allImages;
    }
 
}
