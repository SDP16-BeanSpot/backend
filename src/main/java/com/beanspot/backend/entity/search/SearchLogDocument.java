package com.beanspot.backend.entity.search;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;

@Document(indexName = "search_log")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchLogDocument {

    @Id
    private String id; //UUID

    @Field(type = FieldType.Keyword)
    private String keyword;

    @Field(type = FieldType.Long)
    private Long userId;

    @Field(type = FieldType.Date)
    private LocalDate searchedAt;
}
