package com.beanspot.backend.repository.es;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsAggregate;
import com.beanspot.backend.entity.search.SearchLogDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SearchLogQueryRepositoryImpl implements SearchLogQueryRepository {

    private final ElasticsearchOperations operations;

    public List<String> findPopularKeywords(int days, int size){
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.range(r -> r
                        .date(d -> d
                                .field("searchedAt")
                                .gte("now-" + days + "d/d")
                        )
                )).withAggregation("popular_keywords",
                        Aggregation.of(a -> a.terms(t -> t
                                .field("keyword")
                                .size(size*3))))
                .build();

        SearchHits<SearchLogDocument> hits = operations.search(query, SearchLogDocument.class);

        ElasticsearchAggregations aggregations = (ElasticsearchAggregations) hits.getAggregations();

        StringTermsAggregate terms = aggregations.get("popular_keywords")
                .aggregation().getAggregate().sterms();

        return terms.buckets().array()
                .stream().map(bucket -> bucket.key().stringValue())
                .toList();

    }
}
