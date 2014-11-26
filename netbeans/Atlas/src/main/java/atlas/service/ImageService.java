package atlas.service;

import atlas.entity.Image;
import atlas.entity.ImageComponent;
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
public class ImageService extends BasicService<Image, Integer> {

    public ImageService() {
        super(Image.class);
    }
    
    public void uploadImage(String name, Part imageFile) {
        String folder = uploadFolder + "images/";
        // new file
        File file = new File(folder + imageFile.getSubmittedFileName());
        // if exists, append numbers
        if (file.exists()) {
            int i = 1;
            while (file.exists()) {
                file = new File(folder + imageFile.getSubmittedFileName() + "_" + i);
                i++;
            }
        }
        
        // new image
        Image image = new Image();
        image.setId(0); // will be generated
        // make sure there is a name (if none, use filename)
        if (name == null || name.isEmpty()) {
            image.setName(file.getName());
        } else {
            image.setName(name);
        }
        // empty relationship lists
        image.setImageComponentList(new ArrayList<ImageComponent>());
        // actual file name
        image.setFilename(file.getName());

        try (InputStream input = imageFile.getInputStream()) {
            Files.copy(input, file.toPath());
        } catch (Exception e) {
            return;
        }
        // if file was uploaded without errors, add image to database
        save(image);
    }
    
    public void deleteImage(Image image) {
        String folder = uploadFolder + "images/";
        File file = new File(folder + image.getFilename());
        // delete file and persisted entity
        delete(image);
        file.delete();
    }
    
    public boolean isUsed(Image image) {
        // check if any component uses the image
        TypedQuery<ImageComponent> query = em.createQuery(
                "SELECT ic FROM ImageComponent ic "
                        + "WHERE ic.image = :image",
                ImageComponent.class);
        return !query.setParameter("image", image).getResultList().isEmpty();
    }

}