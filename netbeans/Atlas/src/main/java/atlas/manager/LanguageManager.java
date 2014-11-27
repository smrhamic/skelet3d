package atlas.manager;

import atlas.entity.Language;
import atlas.service.LanguageService;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * Manages internationalization (locales and site languages)
 * 
 * @author Michal
 */
@SessionScoped
@Named("languageManager")
public class LanguageManager extends BasicManager implements Serializable {

    private static final long serialVersionUID = 1L;
    
    // persistence service
    @EJB
    private LanguageService languageService;
    
    // bound properties
    private Language currentLanguage;
    private Locale locale;
    private List<Language> supportedLanguages;
    
    // initialize current language
    @PostConstruct
    private void init() {
        
        locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        String langCode = locale.getLanguage();
        
        currentLanguage = languageService.getLanguageByISO639(langCode);
        
        // set to default if it fails
        if (currentLanguage == null) {
            currentLanguage = languageService.getDefaultLanguage();
        }
        
        // get all supported languages
        supportedLanguages = languageService.findAll();
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
