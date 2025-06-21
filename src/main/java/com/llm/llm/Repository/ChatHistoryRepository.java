package com.llm.llm.Repository;

import com.llm.llm.Entity.ChatHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long> {
    List<ChatHistory> findByConversationId(int conversationId);
}
