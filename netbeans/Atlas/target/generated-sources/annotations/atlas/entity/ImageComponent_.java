package atlas.entity;

import atlas.entity.Image;
import atlas.entity.PageContent;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2014-11-17T17:53:28")
@StaticMetamodel(ImageComponent.class)
public class ImageComponent_ { 

    public static volatile SingularAttribute<ImageComponent, Image> image;
    public static volatile SingularAttribute<ImageComponent, Integer> compOrder;
    public static volatile SingularAttribute<ImageComponent, String> description;
    public static volatile SingularAttribute<ImageComponent, Integer> id;
    public static volatile SingularAttribute<ImageComponent, PageContent> pageContent;

}