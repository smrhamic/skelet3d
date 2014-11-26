package atlas.manager;

import atlas.entity.Category;
import atlas.entity.Page;
import atlas.entity.view.CategoryView;
import atlas.service.CategoryService;
import atlas.service.PageService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

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
    
    // persistence services
    @EJB
    CategoryService categoryService;
    @EJB
    PageService pageService;
    
    // bound properties
    private int id;
    private List<CategoryView> pathToCurrent;
    
    /**
     * Initializes path based on bound "id".
     *
     * @param isPage True if making path for a page, otherwise false (for a category).
     */
    public void init(boolean isPage) {
        // setup the path
        pathToCurrent = new ArrayList<>();
        Category parentEntity = null;
        // if currently at a page, start with its parent
        // else start with category's parent
        if (isPage) {
            Page thisPage = pageService.find(id);
            if (thisPage != null) {
                parentEntity = thisPage.getCategory();
            }
        } else {
            parentEntity = categoryService.find(id);
            if (parentEntity != null) {
                parentEntity = parentEntity.getParentCategory();
            }            
        }   
        // loop for parents all the way to root, add to beginning for correct order
        while (parentEntity != null){
            pathToCurrent.add(0, categoryService.getCategoryView(
                    parentEntity, languageManager.getCurrentLanguage()));
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
