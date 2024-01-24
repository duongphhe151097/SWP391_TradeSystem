package Models.Common;

import Utils.Settings.AppSettings;
import Utils.Validation.NumberValidator;
import lombok.Getter;

@Getter
public class Pagination {
    private final long totalItem;
    private final int currentPage;
    private final int pageSize;
    private final int totalPage;
    private final int startPage;
    private final int endPage;
    private final int pageRangeOutput;

    public Pagination(long totalItemInput, int pageInput, int pageRange, int pageSize) {
        if (pageRange <= 0) pageRange = 10;
        if (pageSize <= 0) pageSize = 10;
        if (pageInput < 0) pageInput = 0;

        this.totalItem = totalItemInput;
        this.pageSize = pageSize;
        int totalPage = (int) Math.ceil(totalItem / (double) pageSize);

        if (pageInput > totalPage) pageInput = totalPage;

        int currentPage = pageInput > 0 ? pageInput : 1;
        int startPage = currentPage - pageRange / 2;
        int endPage = currentPage + ((int) Math.ceil((double) pageRange / 2) - 1);

        if (startPage <= 0) {
            endPage -= startPage - 1;
            startPage = 1;
        }

        if (endPage > totalPage) {
            endPage = totalPage;
            if (endPage > pageRange) startPage = endPage - (pageRange - 1);
        }

        this.currentPage = currentPage;
        this.totalPage = totalPage;
        this.startPage = startPage;
        this.endPage = endPage;
        this.pageRangeOutput = pageRange;
    }
}
