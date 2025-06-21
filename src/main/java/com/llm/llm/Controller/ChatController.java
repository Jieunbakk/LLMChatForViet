package com.llm.llm.Controller;

import com.llm.llm.Dto.ChatDto.ChatResponseDto;
import com.llm.llm.Dto.ChatDto.ConversationRequestDto;
import com.llm.llm.Entity.Conversation;
import com.llm.llm.Service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/chats")
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/{conversationId}")
    public List<ChatResponseDto> generateResponse(@PathVariable int conversationId) {
        return chatService.getChatHistory(conversationId);
    }

    @PostMapping("")
    public int createChat(@RequestBody ConversationRequestDto requestDto) {
        return chatService.createConversation(requestDto);
    }

    @GetMapping("list")
    public List<Conversation> getChatHistory(@RequestBody Map<String, Object> body) {
        int userId = (int) body.get("userId");
        return chatService.getConversations(userId);
    }
}
