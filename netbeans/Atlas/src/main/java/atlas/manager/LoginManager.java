package atlas.manager;

import atlas.entity.AtlasUser;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Michal
 */
@SessionScoped
@Named("loginManager")
public class LoginManager implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @PersistenceContext
    private EntityManager em;

    private boolean logged, mismatched;
    
    private AtlasUser currentUser = null;

    private String userName, password;

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
    
    public boolean isMismatched() {
        return mismatched;
    }
    
    public String login() {
        currentUser = AtlasUser.login(em, userName, password);
        mismatched = currentUser == null;
        return "";
    }

    public String logout() {
        HttpSession session = (HttpSession)
          FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.invalidate();
        return "/index.xhtml?faces-redirect=true";
    }
}
