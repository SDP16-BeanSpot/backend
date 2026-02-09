package com.beanspot.backend.service.search;

import java.util.List;

public interface AutoCompleteService {
    List<String> suggest(String text);
}
