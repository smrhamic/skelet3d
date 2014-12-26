package atlas.service;

import atlas.entity.Language;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 * Provides CRUD persistence for Language entity.
 * This is an EJB Stateless session bean.
 * It can also find default Language and find by ISO code.
 *
 * @author Michal Smrha
 * @see atlas.entity.Language
 * @see atlas.service.BasicService
 */
@Stateless
public class LanguageService extends BasicService<Language, Integer> {

    /**
     * Constructs a LanguageService.
     */
    public LanguageService() {
        super(Language.class);
    }
    
    /**
     * Finds the default persisted Language.
     *
     * @return First result of a "default" flagged Language; or null.
     */
    public Language findDefaultLanguage() {
        // get default flagged language
        try {
            TypedQuery<Language> query = em.createQuery(
                    "SELECT l FROM Language l "
                            + "WHERE l.defaultLang = TRUE",
                    Language.class);
            return query.getSingleResult();
        } catch (NoResultException e) {
            // if none, null
            return null;
        }
    }
    
    /**
     * Finds persisted Language by ISO short code.
     *
     * @param code ISO code to search for. Examples: "en", "cs"
     * @return Language matching the ISO code, null if no match.
     */
    public Language findLanguageByISO639(String code) {
        // get language with matching code
        try {
            TypedQuery<Language> query = em.createQuery(
                    "SELECT l FROM Language l "
                            + "WHERE l.short1 = :code",
                    Language.class);
            return query.setParameter("code", code).getSingleResult();
        } catch (NoResultException e) {
            // if none, null
            return null;
        }
    }
}
