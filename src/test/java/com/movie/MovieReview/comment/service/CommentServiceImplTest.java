//package com.movie.MovieReview.comment.service;
//
//import com.movie.MovieReview.comment.dao.CommentRepository;
//import com.movie.MovieReview.comment.dto.CommentReqDto;
//import com.movie.MovieReview.comment.dto.CommentResDto;
//import com.movie.MovieReview.comment.entity.Comment;
//import com.movie.MovieReview.member.dto.KakaoInfoDto;
//import com.movie.MovieReview.member.entity.MemberEntity;
//import com.movie.MovieReview.member.entity.UserPrincipal;
//import com.movie.MovieReview.member.repository.MemberRepository;
//import com.movie.MovieReview.member.service.JwtTokenService;
//import com.movie.MovieReview.post.entity.Post;
//import com.movie.MovieReview.post.repository.PostRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.boot.test.context.SpringBootTest;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//@ExtendWith(MockitoExtension.class)
//public class CommentServiceImplTest {
//    @Mock
//    private PostRepository postRepository;
//
//    @Mock
//    private MemberRepository memberRepository;
//
//    @Mock
//    private CommentRepository commentRepository;
//
//    @Mock
//    private JwtTokenService jwtTokenService;
//    @InjectMocks
//    private CommentServiceImpl commentService;
//    @Mock
//    private MemberEntity mockMember;
//    @Mock
//    private Post mockPost;
//
//    @Mock
//    private Comment mockComment;
//
//    @BeforeEach
//    void set() {
//        mockMember = new MemberEntity();
//        mockPost = new Post(mockMember,"테스트 제목입니다","테스트 내용임");
//        mockMember.setEmail("user@example.com");
//        mockMember.setNickname("churu");
//        mockComment = new Comment(mockMember, mockPost, "테스트 댓글");
//    }
//
//    @Test
//    void addComment() throws Exception {
//        Long postId = 1L;
//        CommentReqDto commentReqDto = new CommentReqDto("테스트 댓글입니다.");
//        doReturn(String.valueOf(mockMember.getMemberId())).when(jwtTokenService).getPayload(anyString());
//        when(memberRepository.findByEmail("user@example.com")).thenReturn(Optional.of(mockMember));
//        when(postRepository.findById(postId)).thenReturn(Optional.of(mockPost));
//
//        CommentResDto result = commentService.addComment("Bearer token", postId,commentReqDto);
//
//        assertNotNull(result);
//        assertEquals("테스트 댓글입니다.",result.getContent());
//        assertEquals("churu",result.getNickname());
//        verify(commentRepository).save(any(Comment.class));
//        verify(commentRepository, times(1)).save(any(Comment.class));
//    }
//
//    @Test
//    void deleteComment() throws Exception {
//        Long commentId = 1L;
//        MemberEntity mockMember = new MemberEntity();
//        mockMember.setEmail("user@example.com");
//
//        Post mockPost = new Post(mockMember,"테스트 제목", "테스트 내용");
//        Comment mockComment = new Comment(mockMember, mockPost, "테스트 댓글");
//
//        Mockito.when(commentRepository.findById(commentId)).thenReturn(Optional.of(mockComment));
//        Mockito.when(memberRepository.findByEmail("user@example.com")).thenReturn(Optional.of(mockMember));
//
//        Mockito.doNothing().when(commentRepository).delete(Mockito.any(Comment.class));
//
//        commentService.deleteComment("Bearer token", commentId);
//
//        Mockito.verify(commentRepository).delete(Mockito.any(Comment.class));
//        verify(commentRepository, times(1)).delete(any(Comment.class));
//    }
//
//    @Test
//    void updateComment() throws Exception {
//        Long commentId = 1L;
//        Long postId = 2L;
//        when(memberRepository.findByEmail("user@example.com")).thenReturn(Optional.of(mockMember));
//        when(postRepository.findById(postId)).thenReturn(Optional.of(mockPost));
//        CommentReqDto commentReqDto = new CommentReqDto("테스트 댓글입니다.");
//        CommentResDto commentResDto = commentService.addComment("Bearer token", postId,commentReqDto);
//        commentResDto.setCommentId(12L);
//        System.out.println("before update : " + commentResDto.getContent());
//        commentResDto.setContent("변경한 내용");
//        Comment comment = new Comment(mockMember, mockPost, "변경 전");
//        Long num = comment.getCommentId();
//
//        System.out.println("num: "+ num);
//        commentResDto.setCommentId(num);
//        comment.update(commentResDto);
//        System.out.println("after update: " + comment.getContent());
//    }
//}