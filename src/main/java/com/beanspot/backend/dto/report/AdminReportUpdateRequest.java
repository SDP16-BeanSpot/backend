package com.beanspot.backend.dto.report;

import com.beanspot.backend.entity.ReportStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminReportUpdateRequest {

    @NotNull(message = "처리 상태는 필수입니다.")
    private ReportStatus status;
}
