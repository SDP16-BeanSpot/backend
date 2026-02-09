package com.beanspot.backend.service.search;

import com.beanspot.backend.repository.es.AnnouncementSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AutoCompleteServiceImpl implements AutoCompleteService {
    private final AnnouncementSearchRepository repository;

    @Override
    public List<String> suggest(String keyword) {

        if(keyword == null || keyword.isEmpty()){
            return List.of();
        }

        return repository.autocomplete(keyword, 10);
    }
}
