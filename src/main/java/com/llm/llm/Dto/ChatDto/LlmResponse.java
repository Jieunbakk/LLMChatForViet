package com.llm.llm.Dto.ChatDto;

import com.llm.llm.Entity.ChatHistory;
import com.llm.llm.Entity.Correction;
import com.llm.llm.Enum.Sender;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class LlmResponse {
    String message;
    String correction;
    String response;
    String timestamp;

}
