package com.example.UA.repository;

import com.example.UA.repository.entity.Group;
import com.example.UA.repository.entity.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {
}
