package demo.simple.domain;

import com.github.microservice.components.data.base.data.annotations.sync.DataSync;
import com.github.microservice.components.data.mongo.mongo.domain.SuperEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@NoArgsConstructor
@ToString(callSuper = true)
public class UserInfo extends SuperEntity {

    @Indexed
    private String uid;

    @DataSync(targetEntity = UserGroup.class, targetField = "firstName", targetQuery = "{ 'master' : { '\\$ref':'user' , '\\$id': ObjectId('$uid') } }", targetExpression = "name.substring(0,4)")
    private String name;


    @Indexed
    private String other;


}
