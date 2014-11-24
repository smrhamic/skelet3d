package atlas.service;

import atlas.entity.Image;
import javax.ejb.Stateless;

/**
 *
 * @author Michal
 */
@Stateless
public class ImageService extends BasicService<Image, Integer> {

    public ImageService() {
        super(Image.class);
    }

}