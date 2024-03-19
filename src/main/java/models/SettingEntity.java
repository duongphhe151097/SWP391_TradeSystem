package models;

import jakarta.persistence.*;
import models.common.BaseEntity;
import lombok.*;

import java.io.Serializable;

@Entity(name = "setting")
@Table(name = "setting")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SettingEntity extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`", updatable = false, nullable = false)
    private int id;

    @Column(name = "`key_name`", updatable = false, nullable = false, length = 20)
    private String keyName;

    @Column(name = "`value`", nullable = false, length = 50)
    private String value;
}
