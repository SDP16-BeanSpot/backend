package com.beanspot.backend.service.search;

import com.beanspot.backend.common.filter.BadWordFilter;
import com.beanspot.backend.repository.es.SearchLogQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendedKeywordServiceImpl implements RecommendedKeywordService {

    private final SearchLogQueryRepository searchLogQueryRepository;
    private final BadWordFilter badWordFilter;

    private static final List<String> FALLBACK_KEYWORDS = List.of(
            "봉사활동", "대외활동", "공모전", "서포터즈", "교육 프로그램", "환경 캠페인"
    );

    @Override
    public List<String> getRecommendedKeywords(int size) {

        List<String> raw = searchLogQueryRepository.findPopularKeywords(7, size);
        List<String> filtered = raw.stream().filter(k -> !badWordFilter.containsBadword(k))
                .limit(size).toList();

        List<String> fallback = null;
        if (filtered.size() < size) {
            fallback = FALLBACK_KEYWORDS.stream()
                    .filter(k -> !filtered.contains(k))
                    .limit(size - filtered.size())
                    .toList();
        }

        List<String> result = new ArrayList<>(filtered);
        result.addAll(fallback);
        return result;
    }
}
