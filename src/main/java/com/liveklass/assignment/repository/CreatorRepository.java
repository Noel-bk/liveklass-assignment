package com.liveklass.assignment.repository;

import com.liveklass.assignment.entity.Creator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreatorRepository extends JpaRepository<Creator, Long> {
}
