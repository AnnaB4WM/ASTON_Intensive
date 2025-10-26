package org.springboot_kafka_app.module_5.user_service.repositories;


import org.springboot_kafka_app.module_5.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// взаимодействие с БД
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
