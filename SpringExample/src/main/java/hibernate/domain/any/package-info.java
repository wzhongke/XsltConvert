@AnyMetaDef( name= "PropertyMetaDef", metaType = "string", idType = "long",
        metaValues = {
                @MetaValue(value = "S", targetEntity = StringProperty.class),
                @MetaValue(value = "I", targetEntity = IntegerProperty.class)
        }
)
package hibernate.domain.any;

import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.MetaValue;