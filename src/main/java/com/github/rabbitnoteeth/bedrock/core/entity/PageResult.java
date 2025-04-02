package com.github.rabbitnoteeth.bedrock.core.entity;

import java.util.List;

public class PageResult<E> {

    private long total;

    private List<E> records;

    public PageResult() {
    }

    public PageResult(long total, List<E> records) {
        this.total = total;
        this.records = records;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<E> getRecords() {
        return records;
    }

    public void setRecords(List<E> records) {
        this.records = records;
    }
}
