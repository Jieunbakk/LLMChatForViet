package com.llm.llm.Controller;

import com.llm.llm.Dto.UserDto.DupliDto;
import com.llm.llm.Dto.UserDto.JoinDto;
import com.llm.llm.Dto.UserDto.LoginDto;
import com.llm.llm.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/auth/signup")
public class AuthController {

    final AuthService authService;

    @PostMapping("")
    public ResponseEntity<Void> join(@RequestBody JoinDto JoinDto)
    {
        authService.join(JoinDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/duplicheck")
    public ResponseEntity<Void> duplicateCheck(@RequestBody DupliDto requestDupliDto) throws CloneNotSupportedException{
        authService.duplicateCheck(requestDupliDto);
        return ResponseEntity.ok().build();
    }
}
