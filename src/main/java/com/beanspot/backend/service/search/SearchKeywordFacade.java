package com.beanspot.backend.service.search;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchKeywordFacade {

    private final RecentSearchService recentSearchService;
    private final AutoCompleteService autoCompleteService;
    private final RecommendedKeywordService recommendedKeywordService;

    public List<String> getRecent(Long userId) {
        return recentSearchService.getRecentSearch(userId);
    }

    public void saveRecent(Long userId, String keywords) {
        recentSearchService.saveRecentSearch(userId, keywords);
    }

    public void deleteRecent(Long userId, String keyword) {
        recentSearchService.deleteRecentSearch(userId, keyword);
    }

    public void deleteAllRecent(Long userId) {
        recentSearchService.deleteAllRecentSearch(userId);
    }

    public List<String> getAutoComplete(String keyword) {
        return autoCompleteService.suggest(keyword);
    }

    public List<String> getRecommended(int size) {
        return recommendedKeywordService.getRecommendedKeywords(size);
    }
}
