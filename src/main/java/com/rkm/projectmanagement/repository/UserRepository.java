package com.rkm.projectmanagement.repository;

import com.rkm.projectmanagement.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
