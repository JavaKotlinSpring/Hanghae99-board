package com.example.board.entity;

import com.example.board.dto.BoardRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
// DB와 소통하기 위한 클래스
public class Board {

    private Long id;
    private String title;
    private String username;
    private String password;
    private String contents;
    private String contents_date;

    public Board(BoardRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.username = requestDto.getUsername();
        this.password = requestDto.getPassword();
        this.contents = requestDto.getContents();
        this.contents_date = requestDto.getContents_date();
    }

    public void update(BoardRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.username = requestDto.getUsername();
        this.password = requestDto.getPassword();
        this.contents = requestDto.getContents();
        this.contents_date = requestDto.getContents_date();
    }
}
