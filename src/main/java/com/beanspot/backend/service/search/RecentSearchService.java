package com.beanspot.backend.service.search;

import java.util.List;

public interface RecentSearchService {
    void saveRecentSearch(Long userId, String keyword);
    List<String> getRecentSearch(Long userId);
    void deleteRecentSearch(Long userId, String keyword);
    void deleteAllRecentSearch(Long userId);
}
