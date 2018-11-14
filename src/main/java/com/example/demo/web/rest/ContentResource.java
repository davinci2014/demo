package com.example.demo.web.rest;

import com.example.demo.domain.Content;
import com.example.demo.security.AuthoritiesConstants;
import com.example.demo.service.ContentService;
import com.example.demo.web.rest.util.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ContentResource {
    private ContentService contentService;

    public ContentResource(ContentService contentService) {
        this.contentService = contentService;
    }

    @GetMapping("/contents")
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<List<Content>> getAllContents(Pageable pageable) {
        final Page<Content> page = contentService.getAllContents(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/contents");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
