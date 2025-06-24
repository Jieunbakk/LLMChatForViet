package com.llm.llm.Controller;

import com.llm.llm.Dto.ChatDto.ChatRequestDto;
import com.llm.llm.Dto.ChatDto.ChatResponseDto;
import com.llm.llm.Dto.ChatDto.LlmResponse;
import com.llm.llm.Dto.UserDto.JoinDto;
import com.llm.llm.Service.LlmService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/ai")
@RequiredArgsConstructor
public class LlmController {

    private final LlmService llmService;

    @PostMapping("/correction")
    public LlmResponse generateResponse(@RequestHeader(HttpHeaders.AUTHORIZATION) String accesstoken,@RequestBody ChatRequestDto chatRequestDto) {
        return llmService.generateResponse(accesstoken, chatRequestDto);
    }

}
