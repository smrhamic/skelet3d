package atlas.manager.converter;

import atlas.entity.Model;
import atlas.service.ModelService;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

/**
 *
 * @author Michal
 */
@Named("modelConverter")
@RequestScoped
public class ModelConverter implements Converter {

    @EJB
    private ModelService modelService;

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

        return modelService.find(id);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object modelValue) {

        if (modelValue == null) {
            return "";
        }

        if (modelValue instanceof Model) {
            return String.valueOf(((Model) modelValue).getId());
        } else {
            return "";
        }
    }

}
