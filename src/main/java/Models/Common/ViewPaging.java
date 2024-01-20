package Models.Common;

import java.util.List;

public class ViewPaging<T> {
    public List<T> items;
    public Pagination paging;

    public ViewPaging(List<T> items, Pagination paging) {
        this.items = items;
        this.paging = paging;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public Pagination getPaging() {
        return paging;
    }

    public void setPaging(Pagination paging) {
        this.paging = paging;
    }
}
