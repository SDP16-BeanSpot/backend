package com.beanspot.backend.service;

import com.beanspot.backend.common.exception.CustomException;
import com.beanspot.backend.common.exception.ErrorCode;
import com.beanspot.backend.dto.report.ReportRequest;
import com.beanspot.backend.dto.report.ReportResponse;
import com.beanspot.backend.entity.Report;
import com.beanspot.backend.entity.User;
import com.beanspot.backend.entity.chat.ChatMessage;
import com.beanspot.backend.entity.chat.ChatMessageType;
import com.beanspot.backend.repository.ReportRepository;
import com.beanspot.backend.repository.UserRepository;
import com.beanspot.backend.repository.chat.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public ReportResponse report(Long reporterId, Long messageId, ReportRequest request) {
        ChatMessage message = chatMessageRepository.findByIdWithSender(messageId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_MESSAGE_NOT_FOUND));

        if (message.isDeleted()) {
            throw new CustomException(ErrorCode.CHAT_MESSAGE_NOT_FOUND);
        }

        if (message.getMsgType() != ChatMessageType.TALK) {
            throw new CustomException(ErrorCode.REPORT_TARGET_NOT_ALLOWED);
        }

        User reporter = userRepository.findById(reporterId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        User reportedUser = message.getSender();

        if (reportedUser.getId().equals(reporterId)) {
            throw new CustomException(ErrorCode.REPORT_SELF_NOT_ALLOWED);
        }

        if (reportRepository.existsByReporter_IdAndMessage_Id(reporterId, messageId)) {
            throw new CustomException(ErrorCode.REPORT_ALREADY_EXISTS);
        }

        Report saved = reportRepository.save(Report.builder()
                .reporter(reporter)
                .reportedUser(reportedUser)
                .message(message)
                .reportType(request.getReportType())
                .content(request.getContent())
                .build());

        return ReportResponse.from(saved);
    }
}
