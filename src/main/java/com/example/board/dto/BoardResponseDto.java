package com.example.board.dto;

import com.example.board.entity.Board;
import lombok.Getter;

@Getter
public class BoardResponseDto {

    private Long id;
    private String title;
    private String username;
    private String password;
    private String contents;
    private String contents_date;

    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.username = board.getUsername();
        this.password = board.getPassword();
        this.contents = board.getContents();
        this.contents_date = board.getContents_date();
    }
}
