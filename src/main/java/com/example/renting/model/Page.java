package com.example.renting.model;

public class Page {

    public boolean firstPage;
    public boolean lastPage;
    public int number;
    public int totalPages;
    public int size;
    public int totalItems;

    public static Page of(int totalCount, int pageNo, int limit) {

        Page page = new Page();
        page.totalItems = totalCount;
        page.firstPage = (pageNo == 1 || totalCount < limit);
        page.totalPages = (int) Math.ceil((float) totalCount / (float)limit);
        page.lastPage = (page.totalPages < 2 || pageNo >= page.totalPages);
        page.number = pageNo;
        page.size = limit;

        return page;
    }

    @Override
    public String toString() {
        return "Page{" +
                "firstPage=" + firstPage +
                ", lastPage=" + lastPage +
                ", number=" + number +
                ", totalPages=" + totalPages +
                ", size=" + size +
                ", totalItems=" + totalItems +
                '}';
    }
}
