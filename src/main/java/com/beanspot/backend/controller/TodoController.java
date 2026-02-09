package com.beanspot.backend.controller;

import com.beanspot.backend.dto.user.TodoRequestDto;
import com.beanspot.backend.dto.user.TodoResponseDto;
import com.beanspot.backend.security.CurrentUser;
import com.beanspot.backend.security.UserPrincipal;
import com.beanspot.backend.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Todo", description = "투두리스트 관련 API")
@RestController
@RequestMapping("/api/v1/todos") // 명세서 규격 경로
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @Operation(summary = "일별 투두 조회", description = "특정 날짜의 투두 리스트를 가져옵니다.")
    @GetMapping
    public ResponseEntity<List<TodoResponseDto>> getDailyTodos(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        // 명세서: GET /api/v1/todos?date=2025-12-19
        List<TodoResponseDto> todos = todoService.getDailyTodos(userPrincipal.getId(), date);
        return ResponseEntity.ok(todos);
    }

    @Operation(summary = "투두 추가", description = "새로운 할 일을 추가합니다.")
    @PostMapping
    public ResponseEntity<Long> addTodo(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody TodoRequestDto dto) {
        // 명세서: POST /api/v1/todos
        Long todoId = todoService.addTodo(userPrincipal.getId(), dto);
        return ResponseEntity.ok(todoId);
    }

    @Operation(summary = "투두 상태 변경", description = "할 일의 완료 여부를 토글합니다.")
    @PatchMapping("/{todoId}/status")
    public ResponseEntity<Void> toggleStatus(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable Long todoId) {
        // 명세서: PATCH /api/v1/todos/{todoId}/status
        todoService.toggleStatus(userPrincipal.getId(), todoId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "투두 삭제", description = "할 일을 삭제합니다.")
    @DeleteMapping("/{todoId}")
    public ResponseEntity<Void> deleteTodo(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable Long todoId) {
        // 명세서: DELETE /api/v1/todos/{todoId}
        todoService.deleteTodo(userPrincipal.getId(), todoId);
        return ResponseEntity.noContent().build();
    }
}