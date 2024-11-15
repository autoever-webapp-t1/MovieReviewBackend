package com.movie.MovieReview.post.service;

import com.movie.MovieReview.member.entity.MemberEntity;
import com.movie.MovieReview.member.entity.UserPrincipal;
import com.movie.MovieReview.member.repository.MemberRepository;
import com.movie.MovieReview.post.dto.PostDto;
import com.movie.MovieReview.post.dto.PostResDto;
import com.movie.MovieReview.post.entitiy.Post;
import com.movie.MovieReview.post.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private PostServiceImpl postService;

    @Mock
    private UserPrincipal userPrincipal;

    @Mock
    private MemberEntity mockMember;

    private String title = "탈주";
    private String content = "휴전선 인근 북한 최전방 군부대. 10년 만기 제대를 앞둔 중사 규남은 미래를 선택할 수 없는 북을 벗어나 원하는 것을 해 볼 수 있는 철책 너머로의 탈주를 준비한다. 그러나, 규남의 계획을 알아챈 하급 병사 동혁이 먼저 탈주를 시도하고, 말리려던 규남까지 졸지에 탈주병으로 체포된다. 탈주병 조사를 위해 부대로 온 보위부 소좌 현상은 어린 시절 알고 지내던 규남을 탈주병을 체포한 노력 영웅으로 둔갑시키고 사단장 직속보좌 자리까지 마련해주며 실적을 올리려 한다. 하지만 규남이 본격적인 탈출을 감행하자 현상은 물러설 길 없는 추격을 시작한다.";
    @Mock
    private Post mockPost;

    @BeforeEach
    void set() {
        mockMember = new MemberEntity();
        mockMember.setEmail("user@example.com");
        mockMember.setNickname("churu");
    }
    @Test
    void findByPostId() {
    }

    @Test
    void createPost() throws Exception {
        when(userPrincipal.getEmail()).thenReturn("user@example.com");
        when(memberRepository.findByEmail("user@example.com")).thenReturn(Optional.of(mockMember));
        mockPost = new Post(mockMember, title,content);
        PostDto postDto = new PostDto(mockPost.getPostId(),mockPost.getTitle(),mockPost.getContent());
        System.out.println("postId"+mockPost.getPostId());
        PostResDto result = postService.createPost(postDto);
        assertNotNull(result);
        assertEquals(title,result.title());
        assertEquals(content,result.content());
        verify(postRepository).save(any(Post.class));
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void deletePost() throws Exception {
        Long postId = 1L;
        mockPost = new Post(mockMember,title,content);

        when(postRepository.findById(postId)).thenReturn(Optional.of(mockPost));
        when(userPrincipal.getEmail()).thenReturn("user@example.com");
        when(memberRepository.findByEmail("user@example.com")).thenReturn(Optional.of(mockMember));
        doNothing().when(postRepository).delete(any(Post.class));
        postRepository.save(mockPost);
        postService.deletePost(postId);
        verify(postRepository).delete(any(Post.class));
        verify(postRepository, times(1)).delete(any(Post.class));
    }

    @Test
    void updatePost() throws Exception {
        Long postId = 1L;
        when(userPrincipal.getEmail()).thenReturn("user@example.com");
        when(memberRepository.findByEmail("user@example.com")).thenReturn(Optional.of(mockMember));

        PostDto postDto = new PostDto(postId, "탈주","구교환 짱");
        PostResDto postResDto = postService.createPost(postDto);
        Post post = new Post(mockMember, postResDto.title(),postResDto.content());
        postResDto.setPostId(postId);
        System.out.println("before update : " + post.getContent());
        postResDto.setContent("이제훈 짱");

        Long num = post.getPostId();
        postResDto.setPostId(num);
        post.update(postResDto);
        System.out.println("after update: "+post.getContent());
    }
}