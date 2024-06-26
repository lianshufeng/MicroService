package demo.simple.domain;

import com.github.microservice.components.data.jpa.domain.SuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;


/**
 * 数据库实体
 */
@Data
@Entity
public class User extends  SuperEntity  {

    @Column(unique = true, nullable = true)
    private String userName;

    @Column(unique = false, nullable = true)
    private int age;

    @Column(unique = false, nullable = true)
    private boolean sex;

}
