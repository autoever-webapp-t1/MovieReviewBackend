package com.movie.MovieReview.comment.service;

import com.movie.MovieReview.comment.dao.CommentRepository;
import com.movie.MovieReview.comment.dto.CommentReqDto;
import com.movie.MovieReview.comment.dto.CommentResDto;
import com.movie.MovieReview.comment.entity.Comment;
import com.movie.MovieReview.comment.exception.CommentNotFoundException;
import com.movie.MovieReview.member.entity.MemberEntity;
import com.movie.MovieReview.member.entity.UserPrincipal;
import com.movie.MovieReview.member.repository.MemberRepository;
import com.movie.MovieReview.post.entitiy.Post;
import com.movie.MovieReview.post.repository.PostRepository;
import com.movie.MovieReview.post.service.PostServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService{
    private CommentRepository commentRepository;
    private MemberRepository memberRepository;
    private PostRepository postRepository;
    private UserPrincipal userPrincipal;
    private PostServiceImpl postService;

    private MemberEntity getLoginMember() {
        String loginMemberEmail = userPrincipal.getEmail();
        return memberRepository.findByEmail(loginMemberEmail)
                .orElseThrow(()->new RuntimeException("member not found"));
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()->new CommentNotFoundException("comment not found"));

        MemberEntity member = getLoginMember();
        if(!comment.getWriter().equals(member)) {
            throw new RuntimeException("no permisson");
        }
        Post post = comment.getPost();
        post.deleteComment(comment);
        commentRepository.delete(comment);
        postRepository.save(post);
    }

    @Override
    @Transactional
    public CommentResDto addComment(Long postId, CommentReqDto commentReqDto) {
        MemberEntity member = getLoginMember();
        Post post = postRepository.findById(postId).orElseThrow(()->new RuntimeException("post not found"));
        Comment comment = Comment.builder()
                .writer(member)
                .content(commentReqDto.getContent())
                .post(post)
                .build();

        commentRepository.save(comment);
        post.addComment(comment);
        postRepository.save(post);
        return CommentResDto.entityToResDto(comment);
    }

    @Override
    @Transactional
    public CommentResDto updateComment(Long commentId, CommentResDto commentResDto) {
       Comment target = commentRepository.findById(commentId)
               .orElseThrow(()->new IllegalArgumentException("대상 댓글이 없습니다."));
       target.update(commentResDto);
       Comment updated = commentRepository.save(target);

       return CommentResDto.entityToResDto(target);
    }

    @Override
    public List<CommentResDto> findCommentByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPost_PostId(postId);

        return comments.stream()
                .map(CommentResDto::entityToResDto)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Integer countComment(Long postId) {
        try {
            return commentRepository.countComment(postId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("값이 나오지 않았습니다.");
        }
    }

    @Transactional
    private Post setComment(Long postId) {
        Integer countComment = countComment(postId);
        Post byPostId = postService.findByPostId(postId);
        byPostId.setCommentCnt(countComment);
        return postRepository.save(byPostId);
    }
}
