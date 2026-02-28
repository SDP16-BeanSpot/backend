package com.beanspot.backend.common.response;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PageResponse<T> {

    private long totalCount;
    private int page;
    private int size;
    private int totalPages;
    private List<T> items;

    public static <T> PageResponse<T> of(Page<?> pageResult, List<T> items){
        PageResponse<T> res = new PageResponse<>();
        res.totalCount = pageResult.getTotalElements();
        res.page = pageResult.getNumber();
        res.size = pageResult.getSize();
        res.totalPages = pageResult.getTotalPages();
        res.items = items;
        return res;
    }

    // limit 모드일 경우 (페이징 없음)
    public static <T> PageResponse<T> of(List<T> items){
        PageResponse<T> res = new PageResponse<>();
        res.totalCount = items.size();
        res.page = 0;
        res.size = items.size();
        res.totalPages = 1;
        res.items = items;
        return res;
    }
}
