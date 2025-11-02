package org.api_app_with_doc.module_6.repositories;

import org.api_app_with_doc.module_6.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// взаимодействие с БД
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
