package Models;

import Models.Common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.math.BigDecimal;
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

    @Column(name = "`description`", nullable = false)
    private String description;

    @Column(name = "`secret`", nullable = false, length = 500)
    private String secret;

    @Column(name = "`price`", columnDefinition = "money", nullable = false)
    private BigDecimal price;

    @Column(name = "`contact`", length = 500)
    private String contact;

    @Column(name = "`is_public`", nullable = false)
    private boolean isPublic;

    @Column(name = "`updatable`", nullable = false)
    private boolean updatable;

    @Column(name = "`quantity`", nullable = false)
    private int quantity;

    @Column(name = "`status`", nullable = false)
    private short status;
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(name = "`user_id`", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID user_id;
}
