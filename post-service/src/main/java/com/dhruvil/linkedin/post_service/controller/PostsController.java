package com.dhruvil.linkedin.post_service.controller;

import com.dhruvil.linkedin.post_service.auth.UserContextHolder;
import com.dhruvil.linkedin.post_service.dto.PostCreateRequestDto;
import com.dhruvil.linkedin.post_service.dto.PostDto;
import com.dhruvil.linkedin.post_service.service.PostsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
public class PostsController {

    private final PostsService postsService;

    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostCreateRequestDto postDto) {
        PostDto createdPost = postsService.createPost(postDto);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPost(@PathVariable Long postId
//                                           @RequestHeader("X-User-Id") String userId
                                           ) {
//        String userId = httpServletRequest.getHeader("X-User-Id");
        Long userId = UserContextHolder.getCurrentUserId();
        PostDto postDto = postsService.getPostById(postId);
        return ResponseEntity.ok(postDto);
    }

    @GetMapping("/users/{userId}/allPosts")
    public ResponseEntity<List<PostDto>> getAllPostsOfUser(@PathVariable Long userId) {
        List<PostDto> posts = postsService.getAllPostsOfUser(userId);
        return ResponseEntity.ok(posts);
    }

}
