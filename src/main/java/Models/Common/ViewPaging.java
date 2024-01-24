package Models.Common;

import Models.UserEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ViewPaging<T> {
    public List<T> items;
    public Pagination paging;

    public ViewPaging(List<T> items, Pagination paging) {
        this.items = items;
        this.paging = paging;
    }
}
