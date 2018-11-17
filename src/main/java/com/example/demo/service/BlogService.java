package com.example.demo.service;

import com.example.demo.domain.Blog;
import com.example.demo.repository.BlogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by lin on 2018/11/12.
 */
@Service
@Transactional
public class BlogService {
    private final BlogRepository blogRepository;

    public BlogService(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    public Page<Blog> getAllBlogs(Pageable pageable) {
        return blogRepository.findAllByActivatedTrue(pageable);
    }
}
