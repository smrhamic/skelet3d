package atlas.controller;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * Contains basic methods useful for all inheriting Controllers.
 *
 * @author Michal Smrha
 */
public abstract class BasicController {
    
    /**
     * Sets message with info severity to current FacesContext.
     *
     * @param message Message to be displayed. Can be plain string or EL expression
     * to get localized messages such as "#{bundle.key}"
     */
    protected void showInfo(String message) {
        // context
        FacesContext context = FacesContext.getCurrentInstance();
        // evaluate message
        String msg = context.getApplication()
                .evaluateExpressionGet(context, message, String.class);
        // set it
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
    }
    
    /**
     * Sets message with info severity to a specific component in current FacesContext.
     *
     * @param component Component to show a message for.
     * @param message Message to be displayed. Can be plain string or EL expression
     * to get localized messages such as "#{bundle.key}"
     */
    protected void showInfo(UIComponent component, String message) {
        // context 
        FacesContext context = FacesContext.getCurrentInstance();
        // evaluate message
        String msg = context.getApplication()
                .evaluateExpressionGet(context, message, String.class);
        // add message to component
        context.addMessage(component.getClientId(context),
                    new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null)); 
    }
    
    /**
     * Sets message with warning severity to current FacesContext.
     *
     * @param message Message to be displayed. Can be plain string or EL expression
     * to get localized messages such as "#{bundle.key}"
     */
    protected void showWarning(String message) {
        FacesContext context = FacesContext.getCurrentInstance();
        String msg = context.getApplication()
                .evaluateExpressionGet(context, message, String.class);
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, msg, null));
    }
    
    /**
     * Sets message with info severity to a specific component in current FacesContext.
     *
     * @param component Component to show a message for.
     * @param message Message to be displayed. Can be plain string or EL expression
     * to get localized messages such as "#{bundle.key}"
     */
    protected void showWarning(UIComponent component, String message) {
        // context 
        FacesContext context = FacesContext.getCurrentInstance();
        // evaluate message
        String msg = context.getApplication()
                .evaluateExpressionGet(context, message, String.class);
        // add message to component
        context.addMessage(component.getClientId(context),
                    new FacesMessage(FacesMessage.SEVERITY_WARN, msg, null)); 
    }
    

}
