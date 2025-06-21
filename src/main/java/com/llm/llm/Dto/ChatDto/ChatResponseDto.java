package com.llm.llm.Dto.ChatDto;

import com.llm.llm.Entity.ChatHistory;
import com.llm.llm.Entity.Correction;
import com.llm.llm.Enum.Sender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponseDto {
    Sender sender;
    String message;
    String correction;
    Timestamp timestamp;

    public ChatResponseDto(ChatHistory chatHistory, Correction correction) {
        this.sender = chatHistory.getSender();
        this.message = chatHistory.getMessage();
        this.correction = correction != null ? correction.getCorrection() : null;
        this.timestamp = Timestamp.valueOf(LocalDateTime.now());
    }
}
