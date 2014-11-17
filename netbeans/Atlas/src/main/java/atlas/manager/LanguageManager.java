package atlas.manager;

import atlas.entity.Language;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Manages internationalization (locales and site languages)
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
    
    // bound properties
    private Language currentLanguage;
    private Locale locale;
    private List<Language> supportedLanguages;
    
    // initialize current language
    @PostConstruct
    private void init() {
        
        locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        String langCode = locale.getLanguage();
        
        currentLanguage = Language.getLanguageByISO639(em, langCode);
        
        // set to default if it fails
        if (currentLanguage == null) {
            currentLanguage = Language.getDefaultLanguage(em);
        }
        
        // get all supported languages
        supportedLanguages = Language.getAllLanguages(em);
    }

    /**
     * Switch current language for a new one
     * @param language Language to be used from now on
     * @return URL string to current page to refresh
     */
    public String switchLanguage(Language language) {
        currentLanguage = language;
        locale = new Locale(language.getShort1());
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);

        return FacesContext.getCurrentInstance().getViewRoot().getViewId()
                + "?faces-redirect=true&includeViewParams=true";
    }
    
    /**
     * @return Current session's Locale.
     */
    public Locale getLocale() {
        return locale;
    }
    
    /**
     * @return Current session's Language.
     */
    public Language getCurrentLanguage() {
        return currentLanguage;
    }

    /**
     * @return All supported Languages.
     */
    public List<Language> getSupportedLanguages() {
        return supportedLanguages;
    }
    
}
