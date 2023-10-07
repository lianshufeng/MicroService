package demo.simple.domain;

import com.github.microservice.components.data.mongo.orm.tree.domain.TrackRecordEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FixedMaterialStorageTrackRecord extends TrackRecordEntity<FixedMaterialStorage> {

    @Indexed
    private String name;


    @Indexed
    private State state;

    @Indexed
    private BigDecimal value;


    public static enum State{
        Add,
        Sub
    }
}
