package org.api_app_with_patterns.userservice.repositories;

import org.api_app_with_patterns.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// взаимодействие с БД
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
