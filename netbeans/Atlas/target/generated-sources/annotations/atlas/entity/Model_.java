package atlas.entity;

import atlas.entity.Label;
import atlas.entity.ModelComponent;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2014-11-17T17:53:28")
@StaticMetamodel(Model.class)
public class Model_ { 

    public static volatile ListAttribute<Model, ModelComponent> modelComponentList;
    public static volatile ListAttribute<Model, Label> labelList;
    public static volatile SingularAttribute<Model, String> filename;
    public static volatile SingularAttribute<Model, String> name;
    public static volatile SingularAttribute<Model, Integer> id;

}