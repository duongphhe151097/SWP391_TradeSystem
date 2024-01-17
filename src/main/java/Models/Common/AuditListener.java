package Models.Common;

import Utils.Constants.CommonConstants;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class AuditListener {
    @PreUpdate
    private void beforeAnyUpdate(BaseEntity entity){
        entity.setUpdateBy(CommonConstants.DEFAULT_USER);
    }

    @PrePersist
    private void beforeAnyPersist(BaseEntity entity){
        entity.setCreateBy(CommonConstants.DEFAULT_USER);
    }
}
