package atlas.entity;

import atlas.entity.Model;
import atlas.entity.PageContent;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2014-11-23T19:34:08")
@StaticMetamodel(ModelComponent.class)
public class ModelComponent_ { 

    public static volatile SingularAttribute<ModelComponent, Integer> compOrder;
    public static volatile SingularAttribute<ModelComponent, String> description;
    public static volatile SingularAttribute<ModelComponent, Model> model;
    public static volatile SingularAttribute<ModelComponent, Integer> id;
    public static volatile SingularAttribute<ModelComponent, PageContent> pageContent;

}