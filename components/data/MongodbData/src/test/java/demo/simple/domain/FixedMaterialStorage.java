package demo.simple.domain;

import com.github.microservice.components.data.mongo.orm.tree.domain.TreeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 固定的材料仓库
 */
@Document
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FixedMaterialStorage extends TreeEntity {

    //固定仓库的名称
    @Indexed
    private String name;

    //固定材料仓库的备注
    private String remark;


    //待出库的数量
    @Indexed
    private long pendingOutboundCount;


    //待入库的数量
    @Indexed
    private long pendingInboundCount;


    //现有库存数量
    @Indexed
    private long currentInventoryCount;


    @Indexed
    private long childrenCount = 0;

    @Indexed
    private BigDecimal value;


    private Map<String, Long> store;


}
