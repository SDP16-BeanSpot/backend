package com.beanspot.backend.dto.report;

import com.beanspot.backend.entity.ReportType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReportRequest {

    @NotNull(message = "신고 유형은 필수입니다.")
    private ReportType reportType;

    @Size(min = 30, max = 500, message = "신고 내용은 30자 이상 500자 이하로 입력해주세요.")
    private String content;
}
