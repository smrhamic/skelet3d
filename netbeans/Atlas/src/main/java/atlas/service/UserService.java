package atlas.service;

import atlas.entity.AtlasUser;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
     * Passwords are hashed with SHA-256 and salted.
     *
     * @param login Username.
     * @param pass Password.
     * @return AtlasUser with matching username and password, null if no match.
     */
    public AtlasUser login(String login, String pass) {
        // find user by login (unique assumed), return null if no match
        AtlasUser user;
        try {
            TypedQuery<AtlasUser> query = em.createQuery(
                    "SELECT u FROM AtlasUser u "
                            + "WHERE u.login = :login",
                    AtlasUser.class);
            user = query.setParameter("login", login).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
        // hash the pass
        String securePass = hashWithSalt(pass, user.getSalt());
        // find user with this login, password
        // if nothing matches, null is returned
        try {
            TypedQuery<AtlasUser> query = em.createQuery(
                    "SELECT u FROM AtlasUser u "
                            + "WHERE u.login = :login AND u.pass = :pass",
                    AtlasUser.class);
            return query.setParameter("login", login).setParameter("pass", securePass)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    // hashes password with salt using SHA-256
    // returns empty string is pass / salt is empty
    private String hashWithSalt(String pass, String salt) {
        if (pass == null || salt == null) {
            return "";
        }
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(pass.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }
}