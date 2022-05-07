package demo.simple.domain;

import com.github.microservice.components.data.mongo.timer.domain.SimpleTaskTimerTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class TestTaskTimerTable2 extends SimpleTaskTimerTable {

    //企业id
    private String uid;

}
