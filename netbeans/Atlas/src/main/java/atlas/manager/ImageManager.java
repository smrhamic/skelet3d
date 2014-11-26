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
 * Controller for images.xhtml component.
 * Provides methods to manage images and delegate related
 * user requests to service layer.
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
    
    /**
     * Uploads a new image file and adds it to the database.
     * File and name are taken from bound properties "imageFile" and "newName".
     * Checks if editor is logged and file is selected.
     * If these checks fail, FacesMessage is set and redirect is null.
     * If all checks pass, file is uploaded and page is reloaded.
     *
     * @return Redirection string.
     */
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
    
    /**
     * Updates a database entry of an image.
     * Checks if editor is logged.
     * If this check fails, FacesMessage is set and redirect is null.
     * If all checks pass, entry is updated and redirect reloads page.
     *
     * @param image Image entity to be updated.
     * @return Redirection string.
     */
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
    
    /**
     * Deletes an image.
     * Deletes both the file and database entry.
     * Checks if editor is logged.
     * If this check fails, FacesMessage is set and redirect is null.
     * If all checks pass, image is deleted and redirect reloads page.
     *
     * @param image Image entity to be removed.
     * @return Redirection string.
     */
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

    /**
     * @return List of all available Image entities.
     */
    public List<Image> getAllImages() {
        return allImages;
    }
    
    /**
     * @return File of a new Image in form of "Part" instance.
     */
    public Part getImageFile() {
        return imageFile;
    }

    /**
     * Sets the image file to be uploaded by the "upload()" method.
     *
     * @param imageFile Image file in form of "Part" instance.
     */
    public void setImageFile(Part imageFile) {
        this.imageFile = imageFile;
    }

    /**
     * @return Name of a new Image.
     */
    public String getNewName() {
        return newName;
    }

    /**
     * Sets the name of the image to be persisted by the "upload()" method.
     *
     * @param newName Image name.
     */
    public void setNewName(String newName) {
        this.newName = newName;
    }
 
}
