package hibernate.domain.namestrategy.domain;


import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class UUIDGenerator {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(
        name = "uuid",
        strategy = "org.hibernate.id.UUIDGenerator",
        parameters = {
            @Parameter(
                    name = "uid_gen_strategy_class",
                    value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
            )
        }
    )
    private UUID id;

    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "product_generator"
    )
    @GenericGenerator(
        name = "product_generator",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = {
            @Parameter(name = "sequence_name", value = "product_sequence"),
                @Parameter(name = "initial_value", value = "1"),
                @Parameter(name = "increment)size", value = "3"),
                @Parameter(name = "optimizer", value = "pooled-lo")
        }
    )
    private Long id1;
}
