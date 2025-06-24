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
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LlmService {

    private final ChatHistoryRepository chatHistoryRepository;
    private final ConversationRepository conversationRepository;
    private final CorrectionRepository correctionRepository;
    private final UserRepository userRepository;
    private final WebClient webClient;
    private final JWTUtil jwtUtil;

    String url = "v1/ai/generate";

    public LlmResponse generateResponse(String accessToken,ChatRequestDto chatRequestDto) {
        String userId= jwtUtil.getUserId(accessToken);
        User user=userRepository.findByUserId(userId);
        int userid = user.getId();

        System.out.println(chatRequestDto);
        List<ChatHistory> histories = chatHistoryRepository.findByConversationId(chatRequestDto.getConversationId());
        List<MessageDto> historyDtoList = convertToMessageDto(histories);
        ChatHistory newchatHistory = this.save(chatRequestDto);
        newchatHistory.setUserId(userid);
        Conversation conversation = conversationRepository.findById(chatRequestDto.getConversationId()).get();

        LlmRequest llmRequest = new LlmRequest(conversation.getSituation(), historyDtoList, chatRequestDto.getInput());
        System.out.println(llmRequest);
        LlmResponse llmResponse = webClient
                .post()
                .uri(url)
                .bodyValue(llmRequest)
                .retrieve()
                .bodyToMono(LlmResponse.class)
                .block();

        System.out.println(llmResponse);

        ChatHistory aiChat= new ChatHistory();
        Correction aiCorrection = new Correction();
        aiChat.setConversationId(chatRequestDto.getConversationId());
        aiChat.setSender(Sender.assistant);
        aiChat.setMessage(llmResponse.getResponse());
        Timestamp ts = Timestamp.valueOf(llmResponse.getTimestamp().replace("T", " "));
        aiChat.setCreatedAt(ts);
        conversation.setEndedAt(ts);
        conversationRepository.save(conversation);
        chatHistoryRepository.save(aiChat);

        aiCorrection.setCorrection(llmResponse.getCorrection());
        aiCorrection.setChathistoryId(newchatHistory.getId());
        aiCorrection.setCreatedAt(ts);
        correctionRepository.save(aiCorrection);

        return llmResponse;
    }

    public ChatHistory save(ChatRequestDto chatRequestDto) {
        ChatHistory chatHistory = new ChatHistory(chatRequestDto);
        chatHistory.setSender(Sender.user);
        chatHistoryRepository.save(chatHistory);
        return chatHistory;
    }

    public List<MessageDto> convertToMessageDto(List<ChatHistory> chatHistoryList) {
        return chatHistoryList.stream()
                .map(history -> {
                    MessageDto dto = new MessageDto();
                    dto.setRole(history.getSender().name().toLowerCase()); // "USER" → "user"
                    dto.setContent(history.getMessage());
                    return dto;
                })
                .collect(Collectors.toList());
    }


}
