package com.beanspot.backend.repository.es;

import com.beanspot.backend.entity.announcement.AnnouncementDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface AnnouncementSearchRepository extends ElasticsearchRepository<AnnouncementDocument, Long>, AnnouncementSearchRepositoryCustom {

}
