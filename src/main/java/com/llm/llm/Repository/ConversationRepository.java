package com.llm.llm.Repository;

import com.llm.llm.Entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation, Integer> {
    List<Conversation> findAllByUserId(int userId);
}
