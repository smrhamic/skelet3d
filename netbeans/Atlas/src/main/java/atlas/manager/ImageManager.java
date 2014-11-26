package atlas.manager;

import atlas.entity.Image;
import atlas.service.ImageService;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.Part;

/**
 *
 * @author Michal
 */
@ViewScoped
@Named("imageManager")
public class ImageManager implements Serializable {

    private static final long serialVersionUID = 1L;
    
    // current session's LoginManager to verify the right to edit
    @Inject
    LoginManager loginManager;
    
    // persistence services
    @EJB
    ImageService imageService;

    // bound properties
    private List<Image> allImages;
    private Part imageFile;
    private String newName;

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
    
    public String upload() {
        // check edit rights
        if (!loginManager.isEditor()) {
            // add localized message if bad login
            FacesContext context = FacesContext.getCurrentInstance();
            String msg = context.getApplication()
                    .evaluateExpressionGet(context, "#{strings.noRights}", String.class);
            context.addMessage(null, new FacesMessage(msg));
            return null;
        }
        // do nothing if no file uploaded
        if (imageFile == null) {
            return null;
        }
        // send to service
        imageService.uploadImage(newName, imageFile);
        // refresh
        return FacesContext.getCurrentInstance().getViewRoot().getViewId()
                + "?faces-redirect=true&includeViewParams=true";
    }
    
    public String updateImage(Image image) {
        // check edit rights
        if (!loginManager.isEditor()) {
            // add localized message if bad login
            FacesContext context = FacesContext.getCurrentInstance();
            String msg = context.getApplication()
                    .evaluateExpressionGet(context, "#{strings.noRights}", String.class);
            context.addMessage(null, new FacesMessage(msg));
            return null;
        }
        // simple update
        imageService.update(image);
        // refresh
        return FacesContext.getCurrentInstance().getViewRoot().getViewId()
                + "?faces-redirect=true&includeViewParams=true";
    }
    
    public String removeImage(Image image) {
        // check edit rights
        if (!loginManager.isEditor()) {
            // add localized message if bad login
            FacesContext context = FacesContext.getCurrentInstance();
            String msg = context.getApplication()
                    .evaluateExpressionGet(context, "#{strings.noRights}", String.class);
            context.addMessage(null, new FacesMessage(msg));
            return null;
        }
        // check if image is used somewhere
        if (imageService.isUsed(image)) {
            // add localized message if used
            FacesContext context = FacesContext.getCurrentInstance();
            String msg = context.getApplication()
                    .evaluateExpressionGet(context, "#{strings.imageIsUsed}", String.class);
            context.addMessage(null, new FacesMessage(msg));
            return null;
        }
        // service does the dirty job
        imageService.deleteImage(image);
        // refresh
        return FacesContext.getCurrentInstance().getViewRoot().getViewId()
                + "?faces-redirect=true&includeViewParams=true";
    }

    public List<Image> getAllImages() {
        return allImages;
    }
    
    public Part getImageFile() {
        return imageFile;
    }

    public void setImageFile(Part imageFile) {
        this.imageFile = imageFile;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
 
}
