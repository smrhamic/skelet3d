package atlas.manager;

import atlas.entity.Category;
import atlas.entity.Page;
import atlas.entity.view.CategoryView;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Controller of path.xhtml component.
 * Provides path to current page.
 *
 * @author Michal
 */
@RequestScoped
@Named("pathManager")
public class PathManager implements Serializable {

    private static final long serialVersionUID = 1L;
    
    // current session's LanguageManager to get current language
    @Inject
    LanguageManager languageManager;
    
    // EntityManager responsible for model persistence
    @PersistenceContext
    private EntityManager em;
    
    // bound properties
    private int id;
    private List<CategoryView> pathToCurrent;
    
    public void init(boolean isPage) {
        // setup the path
        pathToCurrent = new ArrayList<>();
        Category parentEntity = null;
        // if currently at a page, start with its parent
        // else start with category's parent
        if (isPage) {
            Page thisPage = Page.getPageById(em, id);
            if (thisPage != null) {
                parentEntity = thisPage.getCategory();
            }
        } else {
            parentEntity = Category.getCategoryById(em, id);
            if (parentEntity != null) {
                parentEntity = parentEntity.getParentCategory();
            }            
        }   
        // loop for parents all the way to root, add to beginning for correct order
        while (parentEntity != null){
            pathToCurrent.add(0, CategoryView.getCategoryView(
                    em, parentEntity, languageManager.getCurrentLanguage()));
            parentEntity = parentEntity.getParentCategory();
        }
    }
    
    /**
     * Gets categories that are current category's / page's ancestors
     * in the form of a list of CaregoryViews.
     *
     * @return List of ancestors sorted from root to current's parent.
     */
    public List<CategoryView> getPathToCurrent() {
        return pathToCurrent;
    }

    /**
     * @return ID of current page / category.
     */
    public int getId() {
        return id;
    }
    
    /**
     * @param id ID of current page / category.
     */
    public void setId(int id) {
        this.id = id;
    } 
    
}
