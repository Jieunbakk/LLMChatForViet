package com.llm.llm.Dto.ChatDto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LlmRequest {
    String situation;
    List<MessageDto> history;
    String message;
}
