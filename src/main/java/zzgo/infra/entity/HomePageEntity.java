package zzgo.infra.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import zzgo.domain.HomePageInfo;

@Data
@Entity
@Table(name = "homepage")
public class HomePageEntity {
    @Id
    private int id;
    @Column
    private String title;
    @Column
    private String description;

    public HomePageInfo tran(){
        return new HomePageInfo(id, title, description);
    }
}
