package com.llm.llm.Repository;

import com.llm.llm.Entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation, Integer> {
}
