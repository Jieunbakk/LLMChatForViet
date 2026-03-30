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
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LlmService {

    private final ChatHistoryRepository chatHistoryRepository;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final ChatService chatService;
    private final WebClient LlmwebClient;
    private final JWTUtil jwtUtil;

    String url = "/v1/ai/generate";

    @Timed(value = "service.generateResponse",percentiles = {0.5,0.95,0.99},histogram = true)
    public LlmResponse generateResponse(String accessToken,ChatRequestDto chatRequestDto) {

        //토큰 검증
        String token = accessToken.split(" ")[1];
        String userId = jwtUtil.getUserId(token);

        //과거의 대화내용 등 필요한 데이터를 DB로부터 긁어모아 LlmRequest에 담는다
        User user=userRepository.findByUserId(userId);
        int userid = user.getId();
        System.out.println(chatRequestDto);
        List<ChatHistory> histories = chatHistoryRepository.findByConversationId(chatRequestDto.getConversationId());
        List<MessageDto> historyDtoList = convertToMessageDto(histories);

        ChatHistory newchatHistory = chatService.saveUserChat(chatRequestDto);

        Conversation conversation = conversationRepository.findById(chatRequestDto.getConversationId()).get();
        LlmRequest llmRequest = new LlmRequest(conversation.getSituation(), historyDtoList, chatRequestDto.getInput());
        //2. 서버에 요청을 보낸다
        LlmResponse llmResponse = TestRequestLlm(llmRequest);

        chatService.saveAiResponse(newchatHistory,chatRequestDto,conversation,llmResponse,userid);

        return llmResponse;
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

    LlmResponse RequestLlm(LlmRequest llmRequest) {
        System.out.println("hello ✅"+ LlmwebClient.toString());

        return  LlmwebClient
                .post()
                .uri(url)
                .bodyValue(llmRequest)
                .retrieve()
                .bodyToMono(LlmResponse.class)
                .block(); //sync
    }

    LlmResponse TestRequestLlm(LlmRequest llmRequest) {
        LlmResponse llmresponse = new LlmResponse("hello","안녕하세요", "반가워요", "2025-09-30 01:20:17.038066");
        return llmresponse;
    }


}
