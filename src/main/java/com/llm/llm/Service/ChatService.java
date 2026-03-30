package com.llm.llm.Service;

import com.llm.llm.Dto.ChatDto.*;
import com.llm.llm.Entity.ChatHistory;
import com.llm.llm.Entity.Conversation;
import com.llm.llm.Entity.Correction;
import com.llm.llm.Entity.User;
import com.llm.llm.Enum.Sender;
import com.llm.llm.Jwt.JWTUtil;
import com.llm.llm.Repository.ChatHistoryRepository;
import com.llm.llm.Repository.ConversationRepository;
import com.llm.llm.Repository.CorrectionRepository;
import com.llm.llm.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatHistoryRepository chatHistoryRepository;
    private final ConversationRepository conversationRepository;
    private final CorrectionRepository correctionRepository;
    private final UserRepository userRepository;
    final JWTUtil jwtUtil;

    public List<ChatResponseDto> getChatHistory(int conversationId) {
        List<ChatHistory> chatHistoryList= chatHistoryRepository.findByConversationId(conversationId);
        List<ChatResponseDto> llmResponseList = new ArrayList<>();
        for(ChatHistory chatHistory: chatHistoryList){
            llmResponseList.add(new ChatResponseDto(chatHistory, correctionRepository.findByChathistoryId(chatHistory.getId())));
        }
        return llmResponseList;
    }

    public List<Conversation> getConversations(String accessToken) {
        String token = accessToken.split(" ")[1];
        String userId = jwtUtil.getUserId(token);
        User user = userRepository.findByUserId(userId);
        return conversationRepository.findAllByUserId(user.getId());
    }

    @Transactional
    public int createConversation(String accessToken, ConversationRequestDto conversationRequestDto) {
        String token = accessToken.split(" ")[1];
        String userId = jwtUtil.getUserId(token);
        User user = userRepository.findByUserId(userId);
        Conversation conversation = new Conversation(conversationRequestDto);
        conversation.setUserId(user.getId());
        return conversationRepository.save(conversation).getId();
    }

    @Transactional
    public ChatHistory saveUserChat(ChatRequestDto chatRequestDto) {
        ChatHistory chatHistory = new ChatHistory(chatRequestDto);
        chatHistory.setSender(Sender.user);
        return chatHistoryRepository.save(chatHistory);
    }

    @Transactional
    public void saveAiResponse(ChatHistory newchatHistory, ChatRequestDto chatRequestDto,Conversation conversation, LlmResponse llmResponse,int userId) {

        // 데이터를 Entity로 정제시킨다.
        Timestamp ts = Timestamp.valueOf(llmResponse.getTimestamp().replace("T", " "));
        chatHistoryRepository.save(convertToChatHistory(chatRequestDto,llmResponse,ts));

        // 마지막 채팅 시간 업데이트
        conversation.setEndedAt(ts);
        conversationRepository.save(conversation);

        //교정 채팅 데이터를 저장
        newchatHistory.setUserId(userId);
        correctionRepository.save(convertToCorrection(chatRequestDto, llmResponse, newchatHistory, ts));
    }

    public ChatHistory convertToChatHistory(ChatRequestDto chatRequestDto,LlmResponse llmResponse,Timestamp ts) {
        ChatHistory aiChat= new ChatHistory();
        aiChat.setConversationId(chatRequestDto.getConversationId());
        aiChat.setSender(Sender.assistant);
        aiChat.setMessage(llmResponse.getResponse());
        aiChat.setCreatedAt(ts);
        return aiChat;
    }

    public Correction convertToCorrection(ChatRequestDto chatRequestDto, LlmResponse llmResponse,ChatHistory newchatHistory,Timestamp ts) {
        Correction aiCorrection = new Correction();
        aiCorrection.setCorrection(llmResponse.getCorrection());
        aiCorrection.setChathistoryId(newchatHistory.getId());
        aiCorrection.setCreatedAt(ts);
        return aiCorrection;
    }

}
