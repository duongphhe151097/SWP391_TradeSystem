package models;

import jakarta.persistence.*;
import models.common.BaseEntity;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.UUID;

@Entity(name = "product")
@Table(name = "product")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductEntity extends BaseEntity implements Serializable {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(name = "`id`", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "`title`", nullable = false)
    private String title;

    @Column(name = "`category_id`", nullable = false)
    private int categoryId;

    @Column(name = "`user_id`", updatable = false)
    private UUID userId;

    @Column(name = "`description`", nullable = false)
    private String description;

    @Column(name = "`secret`", nullable = false)
    private String secret;

    @Column(name = "`price`", nullable = false)
    private BigInteger price;

    @Column(name = "`contact`", length = 500)
    private String contact;

    @Basic
    @Column(name = "`is_public`", nullable = false)
    private boolean isPublic;

    @Basic
    @Column(name = "`is_seller`", nullable = false)
    private boolean isSeller;

    @Column(name = "`updatable`", nullable = false)
    private boolean updatable;

    @Column(name = "`quantity`", nullable = false)
    private int quantity;

    @Column(name = "`status`", nullable = false)
    private short status;
}
