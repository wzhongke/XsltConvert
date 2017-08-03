package hibernate.domain.any;

import org.hibernate.annotations.Any;

import javax.persistence.*;

public class PropertyHolder {

    @Id
    private Long id;

    @Any(
            metaDef = "PropertyMetaDef",
            metaColumn = @Column( name = "property_type" )
    )
    @JoinColumn( name = "property_id" )
    private Property property;

    //Getters and setters are omitted for brevity


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }
}

/*
CREATE TABLE property_holder (
    id BIGINT NOT NULL,
    property_type VARCHAR(255),
    property_id BIGINT,
    PRIMARY KEY ( id )
)

 */