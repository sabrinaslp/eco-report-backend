package com.api.ecoreport.repository;

import com.api.ecoreport.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
