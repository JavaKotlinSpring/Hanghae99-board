package com.example.board.controller;

import com.example.board.dto.BoardRequestDto;
import com.example.board.dto.BoardResponseDto;
import com.example.board.entity.Board;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final Map<Long, Board> boardList = new HashMap<>();

    @PostMapping("/create")
    public BoardResponseDto boardCreate(@RequestBody BoardRequestDto requestDto) {
        // 1. 사용자의 데이터 requestDto를 entity로 보내준다.
        Board board = new Board(requestDto);

        // 게시판의 최대값 구해주기(MAX ID를 찾기)(ID 값으로 구분을 해주고, 중복이 있으면 안됨)
        Long maxId = boardList.size() > 0 ? Collections.max(boardList.keySet()) + 1 : 1;
        board.setId(maxId);

        // 2. entity에 있는 데이터를 DB로 보내준다.
        // DB가 없으므로 맵 컬렉션으로 보내준다.
        boardList.put(board.getId(), board);

        // 3. entity에 있는 데이터를 responseDto로 보내준다.
        BoardResponseDto responseDto = new BoardResponseDto(board);

        // 4. 사용자에게 responseDto를 보내준다.
        return responseDto;
    }

    @GetMapping("/list")
    public List<BoardResponseDto> boardList() {
        // 1. DB에서 entity로 보내준다.
        // DB가 없으므로 맵 컬렉션에서 가져온다.
        List<BoardResponseDto> responseList = boardList.values().stream()
                .map(BoardResponseDto::new).toList();

        // 2. entity를 responseList에 보내준다.

        // 3. 사용자에게 responseList를 보내준다.
        return responseList;
    }

    @PutMapping("/update/{id}")
    public Long boardUpdate(@PathVariable Long id,
                            @RequestBody BoardRequestDto requestDto) {

        // 1. 해당 게시물이 DB에 존재하는지 확인하기 없다면 예외를 던져주기
        if (boardList.containsKey(id)) {
            // 2. DB에 존재하는 게시물 가져오기
            // DB가 없으므로 맵 컬렉션에서 가져온다.
            Board board = boardList.get(id);

            // 3. 해당 게시물 수정하기
            board.update(requestDto);

            // 4. 해당 게시물 id를 사용자에게 보내준다.
            return board.getId();
        } else {
            throw new IllegalArgumentException("선택한 게시물은 존재하지 않습니다.");
        }
    }

    @DeleteMapping("/delete/{id}")
    public Long boardDelete(@PathVariable Long id) {
        // 1. 해당 게시물이 DB에 존재하는지 확인하기 없다면 예외를 던져주기
        if (boardList.containsKey(id)) {
            // 2. 해당 게시물 삭제하기
            boardList.remove(id);
            // 3. 해당 게시물 id를 사용자에게 보내준다.
            return id;
        } else {
            throw new IllegalArgumentException("선택한 게시물은 존재하지 않습니다.");
        }
    }
}