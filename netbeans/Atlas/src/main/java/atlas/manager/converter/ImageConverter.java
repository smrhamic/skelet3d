package atlas.manager.converter;

import atlas.entity.Image;
import atlas.service.ImageService;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

/**
 * Simple converter implementation for Image.
 * Converts between Image entity and Image.id string
 *
 * @author Michal Smrha
 * @see atlas.entity.Image
 */
@Named("imageConverter")
@RequestScoped
public class ImageConverter implements Converter {

    @EJB
    private ImageService imageService;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String submittedValue) {

        if (submittedValue == null || submittedValue.isEmpty()) {
            return null;
        }
        
        int id;
        try { 
            id = Integer.parseInt(submittedValue); 
        } catch(NumberFormatException e) { 
            return null; 
        }

        return imageService.find(id);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object modelValue) {

        if (modelValue == null) {
            return "";
        }

        if (modelValue instanceof Image) {
            return String.valueOf(((Image) modelValue).getId());
        } else {
            return "";
        }
    }

}
