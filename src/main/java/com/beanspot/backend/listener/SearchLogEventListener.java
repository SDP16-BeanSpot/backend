package com.beanspot.backend.listener;

import com.beanspot.backend.entity.search.SearchLogDocument;
import com.beanspot.backend.repository.es.SearchLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SearchLogEventListener {
    private final SearchLogRepository repository;

    @EventListener
    public void handle(SearchExcutedEvent event) {
        if (event.keyword() == null || event.keyword().isBlank()) return;

        repository.save(SearchLogDocument.builder()
                        .id(UUID.randomUUID().toString())
                        .keyword(event.keyword())
                        .userId(event.userId())
                        .searchedAt(LocalDate.now())
                        .build()
        );
    }


}
