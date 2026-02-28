package com.beanspot.backend.dto.announcement;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScheduleResponseDTO {
    private int seqNo;
    private String scheduleDate;
    private String content;
}