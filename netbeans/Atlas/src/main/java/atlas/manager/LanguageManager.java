package atlas.manager;

import atlas.entity.Language;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * TODO : actual functionality (returns default lang every time)
 * 
 * @author Michal
 */
@SessionScoped
@Named("languageManager")
public class LanguageManager implements Serializable {

    private static final long serialVersionUID = 1L;
    
    // EntityManager responsible for model persistence
    @PersistenceContext
    private EntityManager em;
    
    // bound property
    private Language currentLanguage;
    
    // initialize current language
    @PostConstruct
    private void init() {
        // set to default for now
        currentLanguage = Language.getDefaultLanguage(em);
    }

    /**
     * @return Current session's Language.
     */
    public Language getCurrentLanguage() {
        return currentLanguage;
    }

    /**
     * @param currentLanguage Current session's Language.
     */
    public void setCurrentLanguage(Language currentLanguage) {
        this.currentLanguage = currentLanguage;
    }
   
}
