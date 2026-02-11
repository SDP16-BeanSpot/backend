package com.beanspot.backend.repository.es;

import com.beanspot.backend.entity.search.SearchLogDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SearchLogRepository extends ElasticsearchRepository<SearchLogDocument, String> {
}
