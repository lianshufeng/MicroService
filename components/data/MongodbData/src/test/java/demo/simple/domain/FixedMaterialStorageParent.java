package demo.simple.domain;

import com.github.microservice.components.data.mongo.orm.tree.domain.TreeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

/**
 * 固定的材料仓库
 */
@Document
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FixedMaterialStorageParent extends TreeEntity {

    //固定仓库的名称
    @Indexed
    private String name;

    //固定材料仓库的备注
    private String remark;


    private Map<String,Long> store;


}
