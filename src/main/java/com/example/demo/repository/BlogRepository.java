package com.example.demo.repository;

import com.example.demo.domain.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long>{
    @EntityGraph(attributePaths = "user")
    Page<Blog> findAllByActivatedTrue(Pageable pageable);
}
