package com.himansh.seamosamigos.service;

import com.himansh.seamosamigos.entity.LikeOnFeed;
import com.himansh.seamosamigos.exception.UserException;
import com.himansh.seamosamigos.repository.LikeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    private final Logger log = LoggerFactory.getLogger(LikeService.class);
    private final LikeRepository likeRepository;

    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    public Integer addLike(Integer feedId, Integer userId, String feedType) throws UserException {
        Integer isLiked = likeRepository.isLikedAlready(feedId, userId);
        if (isLiked>0) {
            log.info("User has already liked the feed: {}", feedId);
            throw new UserException("User has already liked the same post.");
        }
        var like = new LikeOnFeed();
        like.setFeedId(feedId);
        like.setUserId(userId);
        like.setFeedType(feedType);
        like  = likeRepository.saveAndFlush(like);
        return like.getLikeId();
    }

    public Long disLike(Integer feedId) {
        likeRepository.deleteByFeedId(feedId);
        return likeRepository.getTotalLikes(feedId);
    }

    public Boolean haveUserLiked(Integer feedId, Integer userId) {
        Integer isLiked = likeRepository.isLikedAlready(feedId, userId);
        return isLiked>0;
    }
}
