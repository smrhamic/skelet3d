package atlas.service;

import atlas.entity.Label;
import atlas.entity.LabelContent;
import atlas.entity.LabelContentPK;
import atlas.entity.Language;
import atlas.entity.Model;
import atlas.entity.view.LabelView;
import com.google.gson.Gson;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

/**
 * Provides CRUD persistence and other methods for Label entity.
 * This is an EJB Stateless session bean.
 * It can also create LabelViews for a model and update labels
 * from a JSON string.
 *
 * @author Michal Smrha
 * @see atlas.entity.Label
 * @see atlas.service.BasicService
 */
@Stateless
public class LabelService extends BasicService<Label, Integer> {

    @EJB
    LanguageService languageService;
    @EJB
    LabelContentService labelContentService;
    
    /**
     * Constructs a LabelService.
     */
    public LabelService() {
        super(Label.class);
    }
    
    /**
     * Creates LabelViews for a given Model in given Language.
     *
     * @param model Model to create LabelViews for.
     * @param lang Language to create LabelViews in.
     * @return LabelViews for all Labels of given Model in given Language.
     * @see atlas.entity.Label
     * @see atlas.entity.view.LabelView
     */
    public List<LabelView> createLabelViews(Model model, Language lang) {
        // find labels for model in language
        TypedQuery<LabelView> query = em.createQuery(
                "SELECT NEW atlas.entity.view.LabelView(l.id, lc.title, lc.text, "
                        + "l.markX, l.markY, l.markZ, "
                        + "l.labelX, l.labelY, l.labelZ) "
                        + "FROM Label l JOIN l.labelContentList lc "
                        + "WHERE l.model = :model AND lc.language1 = :lang",
                LabelView.class);
        List<LabelView> lvs = query.setParameter("model", model).setParameter("lang", lang)
                .getResultList();
        return lvs;
    }
    
    /**
     * Updates Labels for a Model based on a JSON String.
     * The changes include creation, deletion and updates.
     * Changes are persisted.
     *
     * @param json JSON String representing an array of changed LabelViews.
     * @param model Model whose Labels should be updated.
     * @param lang For which Language the Labels should be updated.
     */
    public void updateLabelsFromJSON(String json, Model model, Language lang) {
        // parse JSON
        Gson gson = new Gson();
        LabelView[] updates = gson.fromJson(json, LabelView[].class);
        
        // treat each change according to "action"
        for(LabelView update:updates) {
            switch (update.getAction()) {
                // create a new label + contents
                case "create":
                    // populate label
                    Label label = new Label();
                    label.setLabelX(update.getLabelX());
                    label.setLabelY(update.getLabelY());
                    label.setLabelZ(update.getLabelZ());
                    label.setMarkX(update.getMarkX());
                    label.setMarkY(update.getMarkY());
                    label.setMarkZ(update.getMarkZ());
                    label.setModel(model);
                    // add id and persist
                    label.setId(0); // will be generated
                    save(label);
                    // populate content for all languages
                    for(Language anyLang:languageService.findAll()) {
                        LabelContent lc = new LabelContent();
                        lc.setLabelContentPK(
                                new LabelContentPK(label.getId(), anyLang.getId()));
                        lc.setLabel1(label);
                        lc.setLanguage1(anyLang);
                        if(lang.equals(anyLang)) {
                            lc.setTitle(update.getTitle());
                            lc.setText(update.getText());
                        } else {
                            lc.setTitle("[" + lang.getShort1() + "] " + update.getTitle());
                            lc.setText("[" + lang.getShort1() + "] " + update.getText());
                        }
                        labelContentService.save(lc);
                    }
                    // update label to make sure children were added
                    update(label);
                    break;
                case "update":
                    // populate label
                    label = find(update.getId());
                    label.setLabelX(update.getLabelX());
                    label.setLabelY(update.getLabelY());
                    label.setLabelZ(update.getLabelZ());
                    label.setMarkX(update.getMarkX());
                    label.setMarkY(update.getMarkY());
                    label.setMarkZ(update.getMarkZ());
                    // populate content for current language
                    LabelContent lc = labelContentService.find(
                            new LabelContentPK(label.getId(), lang.getId()));
                    lc.setTitle(update.getTitle());
                    lc.setText(update.getText());
                    // persist
                    labelContentService.update(lc);
                    update(label);
                    break;
                case "delete":
                    delete(find(update.getId()));
            }
        }
    }
}
