package models.common;

import filters.UsernameHolder;
import utils.constants.CommonConstants;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class AuditListener {
    @PreUpdate
    private void beforeAnyUpdate(BaseEntity entity){
        String username = UsernameHolder.getUserName();

        if (username == null || username.isBlank()) username = CommonConstants.DEFAULT_USER;
        entity.setUpdateBy(username);
    }

    @PrePersist
    private void beforeAnyPersist(BaseEntity entity){
        String username = UsernameHolder.getUserName();

        if (username == null || username.isBlank()) username = CommonConstants.DEFAULT_USER;
        entity.setCreateBy(username);
    }
}
