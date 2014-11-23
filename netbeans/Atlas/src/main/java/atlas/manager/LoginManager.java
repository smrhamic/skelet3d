package atlas.manager;

import atlas.entity.AtlasUser;
import atlas.service.UserService;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
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
    private boolean logged, mismatched, editor;
    private AtlasUser currentUser;
    private String userName, password;

    @PostConstruct
    private void init() {
        logged = mismatched = editor = false;
        currentUser = null;
    }
    
    public String login() {
        currentUser =  userService.login(userName, password);
        if (currentUser != null) {
            editor = currentUser.getUserRole().getRole().equals("editor");
        }
        mismatched = currentUser == null;
        return FacesContext.getCurrentInstance().getViewRoot().getViewId()
                + "?faces-redirect=true&includeViewParams=true";
    }

    public String logout() {
        HttpSession session = (HttpSession)
          FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.invalidate();
        return "/index.xhtml?faces-redirect=true";
    }
    
    public AtlasUser getCurrentUser() {
        return currentUser;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isLogged() {
        return currentUser != null;
    }
    
    public boolean isEditor() {
        return editor;
    }
    
    public boolean isMismatched() {
        return mismatched;
    }
    
}
