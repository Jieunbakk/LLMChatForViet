package com.llm.llm.Repository;

import com.llm.llm.Entity.Correction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface CorrectionRepository extends JpaRepository<Correction, Integer> {
    Correction findByChathistoryId(int Chathistoryid);
}
