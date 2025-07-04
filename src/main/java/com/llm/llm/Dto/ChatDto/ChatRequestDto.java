package com.llm.llm.Dto.ChatDto;

import com.llm.llm.Enum.Sender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRequestDto {
    String input;
    int conversationId;
}
