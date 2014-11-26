package atlas.manager;

import atlas.entity.AtlasUser;
import atlas.service.UserService;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.context.FacesContext;

/**
 * Manages user login and privileges.
 *
 * @author Michal
 */
@SessionScoped
@Named("loginManager")
public class LoginManager implements Serializable {

    private static final long serialVersionUID = 1L;
    
    // persistence services
    @EJB
    UserService userService;

    // bound properties
    private boolean logged, editor;
    private AtlasUser currentUser;
    private String userName, password;

    @PostConstruct
    private void init() {
        logged = editor = false;
        currentUser = null;
    }
    
    /**
     * Attempts to log a user based on bound "userName" and "password".
     * Also sets user privileges such as "editor".
     * If there's a match, refreshing redirect is returned.
     * If there's no match, FaceMessage is set and redirect is null.
     *
     * @return Redirection string.
     */
    public String login() {
        currentUser =  userService.login(userName, password);
        if (currentUser != null) {
            editor = currentUser.getUserRole().getRole().equals("editor");
        }
        if (currentUser == null) {
            // add localized message if bad login
            FacesContext context = FacesContext.getCurrentInstance();
            String msg = context.getApplication()
                    .evaluateExpressionGet(context, "#{strings.badLogin}", String.class);
            context.addMessage(null, new FacesMessage(msg));
            return null;
        }
        return FacesContext.getCurrentInstance().getViewRoot().getViewId()
                + "?faces-redirect=true&includeViewParams=true";
    }

    /**
     * Logs out current user.
     * Sets current user to null, "logged" flag and all privileges to false.
     * Refreshes current page.
     * 
     * @return Redirection string.
     */
    public String logout() {
//        HttpSession session = (HttpSession)
//          FacesContext.getCurrentInstance().getExternalContext().getSession(false);
//        session.invalidate();
        currentUser = null;
        editor = false;
        logged = false;
        return FacesContext.getCurrentInstance().getViewRoot().getViewId()
                + "?faces-redirect=true&includeViewParams=true";
    }
    
    /**
     * @return Currently logged user.
     */
    public AtlasUser getCurrentUser() {
        return currentUser;
    }

    /**
     * @return Password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password Password to be used for "login()".
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return User name.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName User name to be used for "login()".
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return True if a user is logged in, otherwise false.
     */
    public boolean isLogged() {
        return currentUser != null;
    }
    
    /**
     * @return True if a user with editor's rights is logged in, otherwise false.
     */
    public boolean isEditor() {
        return editor;
    }
    
}
