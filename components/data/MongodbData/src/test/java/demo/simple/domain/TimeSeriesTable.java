package demo.simple.domain;


import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.TimeSeries;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;

@Document
@TimeSeries(timeField = "timestamp")
@Entity
public class TimeSeriesTable {

    @Id
    private String id;

    @Field("timestamp")
    private Instant timestamp;
    private String name;

}
