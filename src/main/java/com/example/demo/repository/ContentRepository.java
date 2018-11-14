package com.example.demo.repository;

import com.example.demo.domain.Content;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long>{
    Page<Content> findAllByActivatedTrue(Pageable pageable);
}
