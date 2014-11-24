package atlas.entity;

import atlas.entity.ImageComponent;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2014-11-24T16:59:41")
@StaticMetamodel(Image.class)
public class Image_ { 

    public static volatile SingularAttribute<Image, String> filename;
    public static volatile SingularAttribute<Image, String> name;
    public static volatile ListAttribute<Image, ImageComponent> imageComponentList;
    public static volatile SingularAttribute<Image, Integer> id;

}