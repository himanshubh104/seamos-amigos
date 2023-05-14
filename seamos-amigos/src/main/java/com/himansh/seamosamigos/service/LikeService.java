package com.himansh.seamosamigos.service;

import com.himansh.seamosamigos.entity.Comment;
import com.himansh.seamosamigos.entity.LikeOnFeed;
import com.himansh.seamosamigos.entity.Photo;
import com.himansh.seamosamigos.exception.UserException;
import com.himansh.seamosamigos.repository.CommentRepository;
import com.himansh.seamosamigos.repository.LikeRepository;
import com.himansh.seamosamigos.repository.PhotoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.himansh.seamosamigos.utility.AmigosConstants.COMMENT;
import static com.himansh.seamosamigos.utility.AmigosConstants.IMAGE;

@Service
public class LikeService {
    private final Logger log = LoggerFactory.getLogger(LikeService.class);
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final PhotoRepository photoRepository;

    public LikeService(LikeRepository likeRepository, CommentRepository commentRepository, PhotoRepository photoRepository) {
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
        this.photoRepository = photoRepository;
    }

    @Transactional
    public Integer addLike(Integer feedId, Integer userId, String feedType) throws UserException {
        Integer isLiked = likeRepository.isLikedAlready(feedId, userId);
        if (isLiked>0) {
            log.info("User has already liked the feed: {}", feedId);
            throw new UserException("User has already liked the same post.");
        }
        int totalLikes = 0;
        switch (feedType) {
            case IMAGE:
                Photo photo = photoRepository.findById(feedId).orElseThrow(()->new UserException("Image not found"));
                totalLikes = photo.getLikes()+1;
                photo.setLikes(totalLikes);
                photoRepository.save(photo);
                break;
            case COMMENT:
                Comment comment = commentRepository.findById(feedId).orElseThrow(()->new UserException("Comment not found"));
                totalLikes = comment.getLikes()+1;
                comment.setLikes(totalLikes);
                commentRepository.save(comment);
                break;
            default:
                log.error("Invalid Feed Type: {}", feedType);
                throw new UserException("Invalid Feed Type");
        }
        var like = new LikeOnFeed();
        like.setFeedId(feedId);
        like.setUserId(userId);
        like.setFeedType(feedType);
        likeRepository.save(like);
        return totalLikes;
    }

    @Transactional
    public Long disLike(Integer feedId, String feedType) throws UserException {
        Integer rowsDeleted = likeRepository.deleteByFeedId(feedId);
        if (rowsDeleted == 0) {
            log.info("User has not liked the post: {}", feedId);
            throw new UserException("User has not liked the post.");
        }
        int totalLikes = 0;
        switch (feedType) {
            case IMAGE:
                Photo photo = photoRepository.findById(feedId).orElseGet(Photo::new);
                totalLikes = photo.getLikes()-1;
                photo.setLikes(totalLikes);
                photoRepository.save(photo);
                break;
            case COMMENT:
                Comment comment = commentRepository.findById(feedId).orElseGet(Comment::new);
                totalLikes = comment.getLikes()-1;
                comment.setLikes(totalLikes);
                commentRepository.save(comment);
                break;
            default:
                log.error("Invalid Feed Type: {}", feedType);
                throw new UserException("Invalid Feed Type");
        }
        return (long) totalLikes;
    }

    public Boolean haveUserLiked(Integer feedId, Integer userId) {
        Integer isLiked = likeRepository.isLikedAlready(feedId, userId);
        return isLiked>0;
    }
}
