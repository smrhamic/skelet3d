package atlas.controller;

import atlas.entity.Page;
import atlas.entity.view.PageView;
import atlas.service.PageService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

/**
 * Manages the search results.
 * ViewScoped managed bean, controller for search.xhtml page.
 *
 * @author Michal Smrha
 */
@ViewScoped
@Named("searchController")
public class SearchController extends BasicController implements Serializable {

    private static final long serialVersionUID = 1L;
    
    // persistence services
    @EJB
    PageService pageService;
    
    // current session's LanguageController to get current language
    @Inject
    LanguageController languageController;

    // bound properties
    private String searchTerm;
    private List<PageView> results;

    /**
     * Initializes SearchController.
     * Service is asked for search results, which are then converted
     * to localized PageViews (those are easier to display).
     */
    public void init() {
        results = new ArrayList<>();
        // null should be an empty string
        // but we would rather display 0 results than ALL in that case
        if (searchTerm == null) {
            searchTerm = "";
            return;
        }
        // search for pages
        List<Page> pages = pageService.searchByName(
                searchTerm, languageController.getCurrentLanguage());
        
        // make them into PageViews
        for (Page p : pages) {
            results.add(pageService.createPageView(
                    p, languageController.getCurrentLanguage()));
        }
    }

    /**
     * @return Search term for this search, case insensitive.
     */
    public String getSearchTerm() {
        return searchTerm;
    }

    /**
     * @param searchTerm Search term for this search, case insensitive.
     */
    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    /**
     * @return Results of this search, pages with matching names.
     */
    public List<PageView> getResults() {
        return results;
    }
}
