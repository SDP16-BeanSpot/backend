package com.beanspot.backend.common.filter;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class BadWordFilter {
    private final Set<String> badWords = Set.of(
            "욕설", "비속어", "혐오"
    );

    public boolean containsBadword(String keyword){
        return badWords.stream().anyMatch(keyword::contains);
    }
}
