package atlas.manager;

import atlas.entity.Model;
import atlas.service.ModelService;
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
@Named("modelManager")
public class ModelManager implements Serializable {

    private static final long serialVersionUID = 1L;
    
    // persistence services
    @EJB
    ModelService modelService;

    // bound properties
    private List<Model> allModels;

    @PostConstruct
    private void init() {
        allModels = modelService.findAll();
    }

    public List<Model> getAllModels() {
        return allModels;
    }
 
}
