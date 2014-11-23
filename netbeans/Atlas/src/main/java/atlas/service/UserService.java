package atlas.service;

import atlas.entity.AtlasUser;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 *
 * @author Michal
 */
@Stateless
public class UserService extends BasicService<AtlasUser, Integer> {

    public UserService() {
        super(AtlasUser.class);
    }
    
    public AtlasUser login(String login, String pass) {
        // find user with this login, password
        // if nothing matches, null is returned
        try {
            TypedQuery<AtlasUser> query = em.createQuery(
                    "SELECT u FROM AtlasUser u "
                            + "WHERE u.login = :login AND u.pass = :pass",
                    AtlasUser.class);
            return query.setParameter("login", login).setParameter("pass", pass)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}