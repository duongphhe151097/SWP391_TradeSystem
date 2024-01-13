package Models.Common;

import Utils.Settings.AppSettings;
import Utils.Validation.NumberValidator;

public class Pagination {
    private final int totalItem;
    private final int currentPage;
    private final int pageSize;
    private final int totalPage;
    private final int startPage;
    private final int endPage;

    public Pagination(int totalItem, String page_input) {
        this.totalItem = totalItem;
        this.pageSize = AppSettings.getPageSize();

        int totalPage_internal = (int) Math.ceil((double) totalItem / (double) pageSize);
        int currentPage_internal;
        if (page_input != null) {
            boolean isValid = NumberValidator.isInteger(page_input);
            if (isValid) {
                currentPage_internal = Integer.parseInt(page_input);
            } else {
                currentPage_internal = 1;
            }
        } else {
            currentPage_internal = 1;
        }
        //Start page
        int startPage_internal = currentPage_internal - 5;
        int endPage_internal = currentPage_internal + 4;
        if (startPage_internal <= 0) {
            endPage_internal -= (startPage_internal - 1);
            startPage_internal = 1;
        }
        //End page
        if (endPage_internal > totalPage_internal) {
            endPage_internal = totalPage_internal;
            if (endPage_internal > 10) {
                startPage_internal = endPage_internal - 9;
            }
        }

        this.currentPage = currentPage_internal;
        this.totalPage = totalPage_internal;
        this.startPage = startPage_internal;
        this.endPage = endPage_internal;
    }

    public int getTotalItem() {
        return totalItem;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }
    public int getStartPage() {
        return startPage;
    }

    public int getEndPage() {
        return endPage;
    }
}
