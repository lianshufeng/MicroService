package demo.simple.domain;

import com.github.microservice.components.data.jpa.domain.SuperEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Data
@Table(name = "grouptable")
public class Group extends SuperEntity {

    @ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
    private User user;


}
