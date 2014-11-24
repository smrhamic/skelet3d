package atlas.service;

import atlas.entity.Model;
import javax.ejb.Stateless;

/**
 *
 * @author Michal
 */
@Stateless
public class ModelService extends BasicService<Model, Integer> {

    public ModelService() {
        super(Model.class);
    }

}