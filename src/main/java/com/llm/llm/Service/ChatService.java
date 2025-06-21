package com.llm.llm.Service;

import com.llm.llm.Dto.ChatDto.ChatResponseDto;
import com.llm.llm.Dto.ChatDto.ConversationRequestDto;
import com.llm.llm.Entity.ChatHistory;
import com.llm.llm.Entity.Conversation;
import com.llm.llm.Entity.Correction;
import com.llm.llm.Repository.ChatHistoryRepository;
import com.llm.llm.Repository.ConversationRepository;
import com.llm.llm.Repository.CorrectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatHistoryRepository chatHistoryRepository;
    private final ConversationRepository conversationRepository;
    private final CorrectionRepository correctionRepository;

    public List<ChatResponseDto> getChatHistory(int conversationId) {
        List<ChatHistory> chatHistoryList= chatHistoryRepository.findByConversationId(conversationId);
        List<ChatResponseDto> llmResponseList = new ArrayList<>();
        for(ChatHistory chatHistory: chatHistoryList){
            llmResponseList.add(new ChatResponseDto(chatHistory, correctionRepository.findByChathistoryId(chatHistory.getId())));
        }
        return llmResponseList;
    }

    public List<Conversation> getConversations(int userId) {
        return conversationRepository.findAllByUserId(userId);
    }

    public int createConversation(ConversationRequestDto conversationRequestDto) {
        Conversation conversation = new Conversation(conversationRequestDto);
        return conversationRepository.save(conversation).getId();
    }
}
