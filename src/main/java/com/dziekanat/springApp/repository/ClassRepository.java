package com.dziekanat.springApp.repository;

import com.dziekanat.springApp.model.Class;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassRepository extends JpaRepository<Class, Integer> {

    @Query("SELECT c FROM Class c WHERE c.employee.id = :employeeId")
    List<Class> findByEmployeeId(Integer employeeId);

    @Query("SELECT c FROM Class c WHERE c.name =:name")
    Optional<Class> findByName(String name);
}
