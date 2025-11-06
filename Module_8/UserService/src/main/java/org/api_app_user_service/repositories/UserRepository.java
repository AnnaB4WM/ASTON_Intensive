package org.api_app_user_service.repositories;

import org.api_app_user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// взаимодействие с БД
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
