package com.example.demo.service;

import com.example.demo.domain.Content;
import com.example.demo.repository.ContentRepository;
import com.example.demo.service.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by lin on 2018/11/12.
 */
@Service
@Transactional
public class ContentService {
    private final ContentRepository contentRepository;

    public ContentService(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    public Page<Content> getAllContents(Pageable pageable) {
        return contentRepository.findAllByActivatedTrue(pageable);
    }
}
