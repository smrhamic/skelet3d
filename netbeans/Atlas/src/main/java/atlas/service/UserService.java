package atlas.service;

import atlas.entity.AtlasUser;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 * Provides CRUD persistence for AtlasUser entity.
 * This is an EJB Stateless session bean.
 * It can also check if login credentials are valid.
 *
 * @author Michal Smrha
 * @see atlas.entity.AtlasUser
 * @see atlas.service.BasicService
 */
@Stateless
public class UserService extends BasicService<AtlasUser, Integer> {

    /**
     * Constructs a UserService
     */
    public UserService() {
        super(AtlasUser.class);
    }
    
    /**
     * Compares login credentials to persisted AtlasUsers.
     * TO DO: Encrypting passwords
     *
     * @param login Username.
     * @param pass Password.
     * @return AtlasUser with matching username and password, null if no match.
     */
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