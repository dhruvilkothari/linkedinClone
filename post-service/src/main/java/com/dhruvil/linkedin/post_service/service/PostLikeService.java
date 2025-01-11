package com.dhruvil.linkedin.post_service.service;

import com.dhruvil.linkedin.post_service.auth.UserContextHolder;
import com.dhruvil.linkedin.post_service.entity.Post;
import com.dhruvil.linkedin.post_service.entity.PostLike;
import com.dhruvil.linkedin.post_service.event.PostLikedEvent;
import com.dhruvil.linkedin.post_service.exceptions.BadRequestException;
import com.dhruvil.linkedin.post_service.exceptions.ResourceNotFoundException;
import com.dhruvil.linkedin.post_service.repository.PostLikeRepository;
import com.dhruvil.linkedin.post_service.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostsRepository postsRepository;
    private final KafkaTemplate<Long, PostLikedEvent> kafkaTemplate;

    public void likePost(Long postId) {
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("Attempting to like the post with id: {}", postId);
        Post post = postsRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post not found with id:"+postId));
        boolean alreadyLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);
        if(alreadyLiked) throw new BadRequestException("Cannot like the same post again.");

        PostLike postLike = new PostLike();
        postLike.setPostId(postId);
        postLike.setUserId(userId);
        postLikeRepository.save(postLike);
        PostLikedEvent postLikedEvent = PostLikedEvent.builder()
                        .likedByUserId(userId)
                                .postId(postId)
                                        .creatorId(post.getUserId()).build();
        kafkaTemplate.send("post-liked-topic", postId,postLikedEvent);
        log.info("Post with id: {} liked successfully", postId);
    }

    public void unlikePost(Long postId) {
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("Attempting to unlike the post with id: {}", postId);
        boolean exists = postsRepository.existsById(postId);
        if(!exists) throw new ResourceNotFoundException("Post not found with id: "+postId);

        boolean alreadyLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);
        if(!alreadyLiked) throw new BadRequestException("Cannot unlike the post which is not liked.");

        postLikeRepository.deleteByUserIdAndPostId(userId, postId);

        log.info("Post with id: {} unliked successfully", postId);
    }
}
