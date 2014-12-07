package atlas.manager;

import atlas.entity.Model;
import atlas.service.ModelService;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.Part;

/**
 * Provides ways to manage 3D models.
 * ViewScoped managed bean, controller for models.xhtml component.
 * Provides methods to manage models and delegate related
 * client requests to service layer.
 * Initializes list of available models post-construct.
 *
 * @author Michal SmrhaInitialized list of available models post-construct.
 */
@ViewScoped
@Named("modelManager")
public class ModelManager extends BasicManager implements Serializable {

    private static final long serialVersionUID = 1L;
    
    // current session's LoginManager to verify the right to edit
    @Inject
    LoginManager loginManager;
    
    // persistence services
    @EJB
    ModelService modelService;

    // bound properties
    private List<Model> allModels;
    private Part modelFile;
    private String newName;

    @PostConstruct
    private void init() {
        // get all models
        allModels = modelService.findAll();
        // sort by name, alphabetical, not case sensitive
        Collections.sort(allModels, new Comparator<Model>() {
            @Override
            public int compare(Model m1, Model m2 ) {
                return m1.getName().toLowerCase()
                        .compareTo(m2.getName().toLowerCase());
            }
        });
    }
    
    /**
     * Uploads a new model file and adds it to the database.
     * File and name are taken from bound properties "modelFile" and "newName".
     * Checks if editor is logged and file is selected.
     * If these checks fail, FacesMessage is set and redirect is null.
     * If all checks pass, file is uploaded and page is reloaded.
     *
     * @return Redirection string.
     */
    public String upload() {
        // check edit rights
        if (!loginManager.isEditor()) {
            // add localized message if lacking edit rights
            showWarning("#{messages.noRights}");
            return null;
        }
        // do nothing if no file uploaded
        if (modelFile == null) {
            return null;
        }
        // send to service
        modelService.uploadModel(newName, modelFile);
        
        // set message
        showInfo("#{messages.uploadedFile} "+modelFile.getSubmittedFileName());
        
        // refresh without really refreshing...
        init();
        return "";
    }
    
    /**
     * Updates a database entry of a model.
     * Checks if editor is logged.
     * If this check fails, FacesMessage is set and redirect is null.
     * If all checks pass, entry is updated and redirect reloads page.
     *
     * @param model Model entity to be updated.
     * @return Redirection string.
     */
    public String updateModel(Model model) {
        // check edit rights
        if (!loginManager.isEditor()) {
            // add localized message if lacking edit rights
            showWarning("#{messages.noRights}");
            return null;
        }
        // simple update
        modelService.update(model);
        // refresh
        return FacesContext.getCurrentInstance().getViewRoot().getViewId()
                + "?faces-redirect=true&includeViewParams=true";
    }
    
    /**
     * Deletes a model.
     * Deletes both the file and database entry.
     * Checks if editor is logged.
     * If this check fails, FacesMessage is set and redirect is null.
     * If all checks pass, image is deleted and redirect reloads page.
     *
     * @param model Model entity to be removed.
     * @return Redirection string.
     */
    public String removeModel(Model model) {
        // check edit rights
        if (!loginManager.isEditor()) {
            // add localized message if lacking edit rights
            showWarning("#{messages.noRights}");
            return null;
        }
        // check if model is used somewhere
        if (modelService.isUsed(model)) {
            // add localized message if used
            showWarning("#{messages.modelIsUsed}");
            return null;
        }
        // service does the dirty job
        modelService.deleteModel(model);
        // refresh
        return FacesContext.getCurrentInstance().getViewRoot().getViewId()
                + "?faces-redirect=true&includeViewParams=true";
    }

    /**
     * @return List of all available Model entities.
     */
    public List<Model> getAllModels() {
        return allModels;
    }

    /**
     * @return File of a new Model in form of "Part" instance.
     */
    public Part getModelFile() {
        return modelFile;
    }

    /**
     * Sets the model file to be uploaded by the "upload()" method.
     *
     * @param modelFile Model file in form of "Part" instance.
     */
    public void setModelFile(Part modelFile) {
        this.modelFile = modelFile;
    }

    /**
     * @return Name of a new Model.
     */
    public String getNewName() {
        return newName;
    }

    /**
     * Sets the name of the model to be persisted by the "upload()" method.
     *
     * @param newName Model name.
     */
    public void setNewName(String newName) {
        this.newName = newName;
    }
 
}
