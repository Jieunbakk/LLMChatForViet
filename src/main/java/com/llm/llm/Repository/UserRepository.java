package com.llm.llm.Repository;


import com.llm.llm.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByName(String name);

    boolean existsByUserId(String userId);

    User findByUserId(String username);
}
