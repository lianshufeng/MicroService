package demo.simple.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FixedMaterialStorageModel {


    //id
    private String id;

    private String parent;

    // String.join("/", paths), paths 为字符串数组，依次为路径的节点id
    private String path;

    //排序字段
    private int order;

    private String name;

    //固定材料仓库的备注
    private String remark;


}
