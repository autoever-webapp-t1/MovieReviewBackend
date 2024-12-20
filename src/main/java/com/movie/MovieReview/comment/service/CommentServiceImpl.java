package com.movie.MovieReview.comment.service;
import com.movie.MovieReview.comment.dao.CommentRepository;
import com.movie.MovieReview.comment.dto.CommentReqDto;
import com.movie.MovieReview.comment.dto.CommentResDto;
import com.movie.MovieReview.comment.entity.Comment;
import com.movie.MovieReview.comment.exception.CommentNotFoundException;
import com.movie.MovieReview.member.entity.MemberEntity;
import com.movie.MovieReview.member.repository.MemberRepository;
import com.movie.MovieReview.member.service.JwtTokenService;
import com.movie.MovieReview.post.entity.Post;
import com.movie.MovieReview.post.repository.PostRepository;
import com.movie.MovieReview.post.service.PostServiceImpl;
import com.movie.MovieReview.sse.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final PostServiceImpl postService;
    private final SseService sseService;
    private final JwtTokenService jwtTokenService;

    private Long extractMemberId(String authorizationHeader) throws Exception {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("클라이언트에서 헤더 토큰 오류!!!!!");
        }

        String token = authorizationHeader.substring(7);
        if (!jwtTokenService.validateToken(token)) {
            throw new IllegalArgumentException("유효하지 않은 토큰!!!!");
        }

        return Long.valueOf(jwtTokenService.getPayload(token));
    }

    private MemberEntity getLoginMember(String authorizationHeader) throws Exception {
        Long memberId = extractMemberId(authorizationHeader);
        return memberRepository.findById(memberId)
                .orElseThrow(()->new RuntimeException("member not found"));
    }

    @Override
    @Transactional
    public void deleteComment(String authorizationHeader, Long commentId) throws Exception {
        // 댓글 존재 여부 확인
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("comment not found"));

        // 로그인된 회원 정보 가져오기
        MemberEntity member = getLoginMember(authorizationHeader);

        // 댓글 작성자와 로그인한 회원이 동일한지 확인
        if (!comment.getWriter().equals(member)) {
            throw new RuntimeException("no permission");
        }

        // 게시물에서 댓글 삭제
        Post post = comment.getPost();
        post.deleteComment(comment);

        // 댓글 삭제
        commentRepository.delete(comment);
        postRepository.save(post);  // 수정된 post 객체 저장
    }


    @Override
    @Transactional
    public CommentResDto addComment(String authorizationHeader, Long postId, CommentReqDto commentReqDto) throws Exception {
        MemberEntity member = getLoginMember(authorizationHeader);
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("post not found"));
        Comment comment = Comment.builder()
                .writer(member)
                .content(commentReqDto.getContent())
                .post(post)
                .build();
        commentRepository.save(comment);
        post.addComment(comment);
        postRepository.save(post);
        Long postOwnerId = post.getMember().getMemberId();
        if (!postOwnerId.equals(member.getMemberId())) {
            sseService.sendNotification(member.getMemberId(),"새 댓글이 달렸습니다.");
        }
        return CommentResDto.entityToResDto(comment);
    }

    @Override
    @Transactional
    public CommentResDto updateComment(Long commentId, CommentReqDto commentReqDto) {
        Comment target = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("대상 댓글이 없습니다."));
        target.updateContent(commentReqDto.getContent());
        //commentRepository.save(target);
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
    public CommentResDto findOne(Long commentId) {
        return null;
    }

    @Override
    public Integer countComment(Long postId){
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