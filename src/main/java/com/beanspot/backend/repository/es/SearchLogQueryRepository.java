package com.beanspot.backend.repository.es;

import java.util.List;

public interface SearchLogQueryRepository {
    List<String> findPopularKeywords(int days, int size);
}
