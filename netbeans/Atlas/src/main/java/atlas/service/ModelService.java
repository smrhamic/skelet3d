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
 *
 * @author Michal
 */
@Stateless
public class ModelService extends BasicService<Model, Integer> {

    public ModelService() {
        super(Model.class);
    }
    
    public void uploadModel(String name, Part modelFile) {
        String folder = uploadFolder + "models/";
        // new file
        File file = new File(folder + modelFile.getSubmittedFileName());
        // if exists, append numbers
        if (file.exists()) {
            int i = 1;
            while (file.exists()) {
                file = new File(folder + modelFile.getSubmittedFileName() + "_" + i);
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

        try (InputStream input = modelFile.getInputStream()) {
            Files.copy(input, file.toPath());
        } catch (Exception e) {
            return;
        }
        // if file was uploaded without errors, add model to database
        save(model);
    }
    
    public void deleteModel(Model model) {
        String folder = uploadFolder + "models/";
        File file = new File(folder + model.getFilename());
        // delete file and persisted entity
        delete(model);
        file.delete();
    }
    
    public boolean isUsed(Model model) {
        // check if any component uses the model
        TypedQuery<ModelComponent> query = em.createQuery(
                "SELECT mc FROM ModelComponent mc "
                        + "WHERE mc.model = :model",
                ModelComponent.class);
        return !query.setParameter("model", model).getResultList().isEmpty();
    }

}