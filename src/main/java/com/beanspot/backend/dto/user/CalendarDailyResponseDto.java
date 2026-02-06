package com.beanspot.backend.dto.user;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class CalendarDailyResponseDto {
    private LocalDate date;
    private List<TodoDetail> todos;
    private DiaryDetail diary; // 일기는 없을 수도 있으니 단건

    @Getter @Builder @NoArgsConstructor @AllArgsConstructor
    public static class TodoDetail {
        private Long id;
        private String content;
        private boolean isCompleted;
    }

    @Getter @Builder @NoArgsConstructor @AllArgsConstructor
    public static class DiaryDetail {
        private String content;
        private String emotionType;
    }
}
