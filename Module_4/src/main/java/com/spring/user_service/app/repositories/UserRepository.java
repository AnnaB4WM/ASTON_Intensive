package com.spring.user_service.app.repositories;


import com.spring.user_service.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// взаимодействие с БД
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
