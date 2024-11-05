package com.dziekanat.springApp.repository;

import com.dziekanat.springApp.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassRepository extends JpaRepository<Admin, Integer> {

    @Query("SELECT a FROM Admin a WHERE a.user.username = :username")
    Optional<Admin> findByUserUsername(String username);
}
