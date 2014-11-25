package atlas.manager;

import atlas.entity.Model;
import atlas.service.ModelService;
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
@Named("modelManager")
public class ModelManager implements Serializable {

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
        if (modelFile == null) {
            return null;
        }
        // send to service
        modelService.uploadModel(newName, modelFile);
        // refresh
        return FacesContext.getCurrentInstance().getViewRoot().getViewId()
                + "?faces-redirect=true&includeViewParams=true";
    }
    
    public String removeModel(Model model) {
        // check edit rights
        if (!loginManager.isEditor()) {
            // add localized message if bad login
            FacesContext context = FacesContext.getCurrentInstance();
            String msg = context.getApplication()
                    .evaluateExpressionGet(context, "#{strings.noRights}", String.class);
            context.addMessage(null, new FacesMessage(msg));
            return null;
        }
        // check if model is used somewhere
        if (modelService.isUsed(model)) {
            // add localized message if used
            FacesContext context = FacesContext.getCurrentInstance();
            String msg = context.getApplication()
                    .evaluateExpressionGet(context, "#{strings.modelIsUsed}", String.class);
            context.addMessage(null, new FacesMessage(msg));
            return null;
        }
        // service does the dirty job
        modelService.deleteModel(model);
        // refresh
        return FacesContext.getCurrentInstance().getViewRoot().getViewId()
                + "?faces-redirect=true&includeViewParams=true";
    }

    public List<Model> getAllModels() {
        return allModels;
    }

    public Part getModelFile() {
        return modelFile;
    }

    public void setModelFile(Part modelFile) {
        this.modelFile = modelFile;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
 
}
