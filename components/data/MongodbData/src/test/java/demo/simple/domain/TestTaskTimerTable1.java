package demo.simple.domain;

import com.github.microservice.components.data.mongo.mongo.domain.SuperEntity;
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
public class TestTaskTimerTable1 extends SuperEntity {

    //企业id
    private String epId;


    //表达式
    private String cron;

}
