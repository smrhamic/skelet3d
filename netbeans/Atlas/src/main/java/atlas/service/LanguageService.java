package atlas.service;

import atlas.entity.Language;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 *
 * @author Michal
 */
@Stateless
public class LanguageService extends BasicService<Language, Integer> {

    public LanguageService() {
        super(Language.class);
    }
    
    public Language getDefaultLanguage() {
        // get default flagged language
        // if none, null
        try {
            TypedQuery<Language> query = em.createQuery(
                    "SELECT l FROM Language l "
                            + "WHERE l.defaultLang = TRUE",
                    Language.class);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public Language getLanguageByISO639(String code) {
        // get language with matching code
        // if none, null
        try {
            TypedQuery<Language> query = em.createQuery(
                    "SELECT l FROM Language l "
                            + "WHERE l.short1 = :code",
                    Language.class);
            return query.setParameter("code", code).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
