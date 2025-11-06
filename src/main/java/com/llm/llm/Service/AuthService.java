package com.llm.llm.Service;

import com.llm.llm.Dto.UserDto.DupliDto;
import com.llm.llm.Dto.UserDto.JoinDto;
import com.llm.llm.Entity.User;
import com.llm.llm.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 회원가입
    public void join(JoinDto requestJoinDto) throws CloneNotSupportedException {
        boolean isExist = userRepository.existsByUserId((requestJoinDto.getUserId()));

        if (isExist) {
            throw new CloneNotSupportedException("중복된 아이디가 존재합니다.");
        }
        User user = new User(requestJoinDto);
        System.out.println(requestJoinDto.getPassword());
        user.setPassword(bCryptPasswordEncoder.encode(requestJoinDto.getPassword()));

        userRepository.save(user);
    }

    // 중복 인증
    public void duplicateCheck(DupliDto requestDupliDto) throws CloneNotSupportedException{
        if(userRepository.existsByUserId((requestDupliDto.getUserId())))
        {
            throw new CloneNotSupportedException("중복된 아이디가 존재합니다.");
        }
    }

}

