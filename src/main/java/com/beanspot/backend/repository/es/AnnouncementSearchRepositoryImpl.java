package com.beanspot.backend.repository.es;

import co.elastic.clients.elasticsearch._types.TopLeftBottomRightGeoBounds;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.GeoBoundingBoxQuery;
import com.beanspot.backend.entity.announcement.AnnouncementDocument;
import com.beanspot.backend.entity.announcement.AnnouncementType;
import com.beanspot.backend.entity.search.SortType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AnnouncementSearchRepositoryImpl implements AnnouncementSearchRepositoryCustom {

    private final ElasticsearchOperations operations;

    @Override
    public Page<AnnouncementDocument> search(
            String keyword,
            AnnouncementType type,
            String region,
            YearMonth recruitmentMonth,
            YearMonth activityMonth,
            Double minLat,
            Double maxLat,
            Double minLng,
            Double maxLng,
            boolean onlyRecruiting,
            SortType sort,
            Pageable pageable
    ) {
        BoolQuery.Builder bool = new BoolQuery.Builder();

        // 키워드 검색
        if(keyword != null && !keyword.isBlank()) {
            bool.must(m -> m.multiMatch(mm -> mm
                    .query(keyword)
                    .fields("title", "content")
            ));
        }
        // 공고 유형 필터
        if(type != null ) {
            bool.filter(f -> f.term(t -> t.field("type").value(type.name())));
        }
        // 공고 지역(행정구) 필터
        if(region != null && !region.isEmpty()) {
            bool.filter(f -> f.term(t -> t.field("region").value(region)));
        }
        // 모집중 필터
        if(onlyRecruiting){
            String now = LocalDate.now().toString();
            bool.filter(f -> f.range(r -> r
                    .date(d -> d
                            .field("recruitmentStart")
                            .lte(now)
                    )

            ));
            bool.filter(f -> f.range(r -> r
                    .date(d -> d
                            .field("recruitmentEnd")
                            .gte(now)
                    )
            ));
        }
        // 모집 마감 월 필터
        if (recruitmentMonth != null) {
            LocalDate start = recruitmentMonth.atDay(1);
            LocalDate end = recruitmentMonth.atEndOfMonth();

            bool.filter(f -> f.range(r -> r
                    .date(d -> d
                            .field("recruitmentEnd")
                            .gte(start.toString())
                            .lte(end.toString())
                    )
            ));
        }

        // 활동 기간 월 필터
        if (activityMonth != null) {
            LocalDate start = activityMonth.atDay(1);
            LocalDate end = activityMonth.atEndOfMonth();

            bool.filter(f -> f.range(r -> r
                    .date(d -> d
                            .field("startDate")
                            .lte(end.toString())
                    )
            ));

            bool.filter(f -> f.range(r -> r
                    .date(d -> d
                            .field("endDate")
                            .gte(start.toString())
                    )
            ));
        }

        // 지도 범위 필터
        if (minLat != null && maxLat != null && minLng != null && maxLng != null) {

            GeoBoundingBoxQuery geoQuery = GeoBoundingBoxQuery.of(g -> g
                    .field("geoLocation")
                    .boundingBox(b -> b.tlbr(
                            TopLeftBottomRightGeoBounds.of(t -> t
                                    .topLeft(t1 -> t1
                                            .latlon(ll -> ll
                                                    .lat(maxLat)
                                                    .lon(minLng)
                                            )
                                    ).bottomRight(br -> br
                                            .latlon(ll -> ll
                                                    .lat(minLat)
                                                    .lon(maxLng)
                                            )
                                    )
                            )
                    ))

            );

            bool.filter(f -> f.geoBoundingBox(geoQuery));
        }

        var builder = NativeQuery.builder()
                .withQuery(q -> q.bool(bool.build()))
                .withPageable(pageable);

        if(sort == SortType.DISTANCE && minLat != null && minLng != null){
            builder.withSort(s -> s.geoDistance(g -> g
                            .field("geoLocation")
                            .location(l -> l.latlon(ll -> ll
                                    .lat(minLat)
                                    .lon(minLng)
                            ))
                            .order(co.elastic.clients.elasticsearch._types.SortOrder.Asc)
                    ));
        }

        NativeQuery query = builder.build();


        SearchHits<AnnouncementDocument> hits =
                operations.search(query, AnnouncementDocument.class);

        List<AnnouncementDocument> content = hits.getSearchHits()
                .stream()
                .map(SearchHit::getContent)
                .toList();
        return new PageImpl<>(content, pageable, hits.getTotalHits());
    }

    @Override
    public List<String> autocomplete(String keyword, int limit) {

        if (keyword == null || keyword.isBlank()) {
            return List.of();
        }

        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.match(m -> m
                        .field("title.autocomplete")
                        .query(keyword)
                ))
                .withPageable(PageRequest.of(0, limit))
                .build();

        SearchHits<AnnouncementDocument> hits = operations.search(query, AnnouncementDocument.class);

        return hits.getSearchHits()
                .stream()
                .map(hit -> hit.getContent().getTitle())
                .distinct()
                .toList();
    }
}
