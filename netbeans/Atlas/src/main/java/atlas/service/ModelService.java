package atlas.service;

import atlas.entity.Label;
import atlas.entity.Model;
import atlas.entity.ModelComponent;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.servlet.http.Part;

/**
 * Provides CRUD persistence and other methods for Model entity.
 * This is an EJB Stateless session bean.
 * It can also check if Model is used in any ModelComponents.
 *
 * @author Michal Smrha
 * @see atlas.entity.Model
 * @see atlas.service.BasicService
 */
@Stateless
public class ModelService extends BasicService<Model, Integer> {

    /**
     * Constructs a ModelService.
     */
    public ModelService() {
        super(Model.class);
    }
    
    /**
     * Saves an uploaded model file and persists a new Model entity.
     *
     * @param name Name for the Model entity.
     * @param modelFile Model file to save.
     */
    public void uploadModel(String name, Part modelFile) {
        String folder = uploadFolder + "models/";
        String safeName = modelFile.getSubmittedFileName().replaceAll("[\\W+&&[^.]]", "_");
        // new file
        File file = new File(folder + safeName);
        // if exists, append numbers
        if (file.exists()) {
            int i = 1;
            while (file.exists()) {
                file = new File(folder + safeName + "_" + i);
                i++;
            }
        }
        
        // new model
        Model model = new Model();
        model.setId(0); // will be generated
        // make sure there is a name (if none, use filename)
        if (name == null || name.isEmpty()) {
            model.setName(file.getName());
        } else {
            model.setName(name);
        }
        // empty relationship lists
        model.setLabelList(new ArrayList<Label>());
        model.setModelComponentList(new ArrayList<ModelComponent>());
        // actual file name
        model.setFilename(file.getName());

        // save file
        try (InputStream input = modelFile.getInputStream()) {
            Files.copy(input, file.toPath());
        } catch (Exception e) {
            // todo: better error handling?
            return;
        }
        // if file was uploaded without errors, add model to database
        save(model);
    }
    
    /**
     * Removes Model entity from persistence AND deletes its file.
     *
     * @param model Model entity to completely remove.
     */
    public void deleteModel(Model model) {
        String folder = uploadFolder + "models/";
        File file = new File(folder + model.getFilename());
        // delete file and persisted entity
        delete(model);
        file.delete();
    }
    
    /**
     * Checks if Model entity is used in any ModelComponent.
     *
     * @param model Model entity to check.
     * @return True if Model is used, false if not.
     */
    public boolean isUsed(Model model) {
        // check if any component uses the model
        TypedQuery<ModelComponent> query = em.createQuery(
                "SELECT mc FROM ModelComponent mc "
                        + "WHERE mc.model = :model",
                ModelComponent.class);
        return !query.setParameter("model", model).getResultList().isEmpty();
    }

}