package com.example.demo.service;

import com.example.demo.repository.BlogRepository;
import com.example.demo.service.dto.BlogDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
    public Page<BlogDTO> getAllBlogs(Pageable pageable) {
        return blogRepository.findAll(pageable).map(BlogDTO::new);
    }

    public void deleteBlogById(Long id) {
        blogRepository.findById(id).ifPresent(blog -> {
            blogRepository.delete(blog);
            System.out.println("delete succeed");
        });
    }

    public void saveBlog(Blog blog) {
        blogRepository.save(blog);
    }
}
