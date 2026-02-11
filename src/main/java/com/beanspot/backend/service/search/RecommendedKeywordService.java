package com.beanspot.backend.service.search;

import java.util.List;

public interface RecommendedKeywordService {
    List<String> getRecommendedKeywords(int size);
}
