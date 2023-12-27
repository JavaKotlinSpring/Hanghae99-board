package com.example.board.controller;

import com.example.board.dto.BoardRequestDto;
import com.example.board.dto.BoardResponseDto;
import com.example.board.entity.Board;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final JdbcTemplate jdbcTemplate;

    public BoardController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/create")
    public BoardResponseDto boardCreate(@RequestBody BoardRequestDto requestDto) {
        // 1. 사용자의 데이터 requestDto를 entity로 보내준다.
        Board board = new Board(requestDto);

        // 2. entity에서 DB로 보내준다.
        KeyHolder keyHolder = new GeneratedKeyHolder(); // 기본 키를 반환받기 위한 객체

        String sql = "INSERT INTO board (title, password, username, contents, date) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update( con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, board.getTitle());
                    preparedStatement.setString(2, board.getPassword());
                    preparedStatement.setString(3, board.getUsername());
                    preparedStatement.setString(4, board.getContents());
                    preparedStatement.setString(5, board.getContents_date());
                    return preparedStatement;
                },
                keyHolder);

        // 3. entity에 있는 데이터를 responseDto로 보내준다.
        BoardResponseDto responseDto = new BoardResponseDto(board);

        // 4. 사용자에게 responseDto를 보내준다.
        return responseDto;
    }

    @GetMapping("/list")
    public List<BoardResponseDto> boardList() {
        // 1. DB를 조회한다.
        String sql = "SELECT * FROM board";

        return jdbcTemplate.query(sql, new RowMapper<BoardResponseDto>() {
            @Override
            public BoardResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                // SQL 의 결과로 받아온 Memo 데이터들을 MemoResponseDto 타입으로 변환해줄 메서드
                Long id = rs.getLong("id");
                String title = rs.getString("title");
                String password = rs.getString("password");
                String username = rs.getString("username");
                String contents = rs.getString("contents");
                String date = rs.getString("date");

                // 2. 사용자에게 responseDto를 반환한다.
                return new BoardResponseDto(id, title, password, username, contents, date);
            }
        });
    }

    @PutMapping("/update/{id}")
    public Long boardUpdate(@PathVariable Long id,
                            @RequestBody BoardRequestDto requestDto) {

        // 1. 해당 게시물이 DB에 있는지 확인한다.
        Board board = findById(id);

        // 2. 해당 게시물이 있다면 수정한다.
        if(board != null) {
            // memo 내용 수정
            String sql = "UPDATE board SET title = ?, password = ?, username = ?, contents = ?, date = ? WHERE id = ?";
            jdbcTemplate.update(sql, requestDto.getTitle(), requestDto.getPassword() ,requestDto.getUsername(), requestDto.getContents(),requestDto.getContents_date() ,id);

            // 3. id를 반환한다.
            return id;

            // 4. 해당 게시물이 없다면 예외를 던진다.
        } else {
            throw new IllegalArgumentException("선택한 게시물은 존재하지 않습니다.");
        }
    }

    @DeleteMapping("/delete/{id}")
    public Long boardDelete(@PathVariable Long id) {
        // 1. 해당 게시물이 DB에 있는지 확인한다.
        Board board = findById(id);

        // 2. 해당 게시물이 있다면 삭제한다.
        if(board != null) {
            // 게시물 삭제
            String sql = "DELETE FROM board WHERE id = ?";
            jdbcTemplate.update(sql, id);

            // 3. id를 반환한다.
            return id;

            // 4. 해당 게시물이 없다면 예외를 던진다.
        } else {
            throw new IllegalArgumentException("선택한 게시물은 존재하지 않습니다.");
        }
    }
    private Board findById(Long id) {
        // DB 조회
        String sql = "SELECT * FROM board WHERE id = ?";

        return jdbcTemplate.query(sql, resultSet -> {
            if(resultSet.next()) {
                Board board = new Board();
                board.setTitle(resultSet.getString("title"));
                board.setPassword(resultSet.getString("password"));
                board.setUsername(resultSet.getString("username"));
                board.setContents(resultSet.getString("contents"));
                board.setContents_date(resultSet.getString("date"));
                return board;
            } else {
                return null;
            }
        }, id);
    }
}