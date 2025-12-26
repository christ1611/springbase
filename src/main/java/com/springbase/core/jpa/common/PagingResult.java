package com.springbase.core.jpa.common;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PagingResult<T> {

    private Long totalCount = 0L;
    private Long currentPage = 0L;
    private Long pageCount = 0L;
    private Long totalPage = 0L;

    List<T> queryResult;

    public PagingResult() {
        queryResult = new ArrayList<>();
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    public void setQueryResult(List<T> queryResult) {
        this.queryResult = queryResult;

        if(queryResult != null && ! queryResult.isEmpty() )
        {
            if (totalCount != null && pageCount != null) {
                if (pageCount > 0)
                    this.totalPage = (long) Math.ceil((float) totalCount / (float) pageCount);
            }
        }
    }


    public void setTotalCount(final Long totalCount) {
        if (totalCount != null && pageCount != null) {
            if (pageCount > 0)
                this.totalPage = (long) Math.ceil((float) totalCount / (float) pageCount);
        }
        this.totalCount = totalCount;
    }


    public void setCurrentPage(final Long currentPage) {
        this.currentPage = currentPage;
    }


    public void setPageCount(Long pageCount, Long pageNumber) {
        if (totalCount != null && pageCount != null) {
            if (pageCount > 0)
                this.totalPage = (long) Math.ceil((float) totalCount / (float) pageCount);
        }

        this.pageCount = (this.totalCount - ((pageNumber-1) * pageCount))  < pageCount ? (this.totalCount - ((pageNumber-1) * pageCount)) :  pageCount;
        if(this.pageCount.compareTo(0L) < 0) {
            this.pageCount = 0L;
        }
    }
}
