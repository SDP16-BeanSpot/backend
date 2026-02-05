package com.beanspot.backend.repository.announcement;

import com.beanspot.backend.entity.announcement.Announcement;
import com.beanspot.backend.entity.announcement.AnnouncementType;
import com.beanspot.backend.entity.announcement.QAnnouncement;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AnnouncementRdbSearchRepositoryImpl implements AnnouncementRdbSearchRepository {

    private final JPAQueryFactory queryFactory;

    QAnnouncement a = QAnnouncement.announcement;

    public AnnouncementRdbSearchRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }


    @Override
    public Page<Announcement> search(
            String keyword,
            AnnouncementType type,
            String region,
            String sort,
            Pageable pageable
    ) {
        List<Announcement> content = queryFactory
                .selectFrom(a)
                .where(
                        containsKeyword(keyword),
                        eqType(type),
                        eqRegion(region)
                )
                .orderBy(sortCondition(sort))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(a.count())
                .from(a)
                .where(
                        containsKeyword(keyword),
                        eqType(type),
                        eqRegion(region)
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }

    @Override
    public List<Announcement> findTop(
            String keyword,
            AnnouncementType type,
            String region,
            String sort,
            int limit
    ) {
        return queryFactory
                .selectFrom(a)
                .where(
                        containsKeyword(keyword),
                        eqType(type),
                        eqRegion(region)
                )
                .orderBy(sortCondition(sort))
                .limit(limit)
                .fetch();
    }

    private BooleanExpression containsKeyword(String keyword) {
        if(keyword == null || keyword.isEmpty()) return null;
        return a.title.containsIgnoreCase(keyword)
                .or(a.content.containsIgnoreCase(keyword));
    }

    private BooleanExpression eqType(AnnouncementType type) {
        return type == null ? null : a.type.eq(type);
    }

    private BooleanExpression eqRegion(String region) {
        return (region == null || region.isEmpty()) ?  null : a.region.eq(region);
    }

    private OrderSpecifier<?> sortCondition(String sort) {
        if(sort == null || sort.equals("latest"))
            return a.createdAt.desc();
        if(sort.equals("popular"))
            return a.viewCount.desc();
        if(sort.equals("endDate"))
            return a.endDate.asc();
        return a.createdAt.desc();
    }

}
