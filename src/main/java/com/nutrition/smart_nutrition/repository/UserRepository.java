package com.nutrition.smart_nutrition.repository;

import com.nutrition.smart_nutrition.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}