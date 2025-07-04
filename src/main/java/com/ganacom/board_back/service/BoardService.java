package com.ganacom.board_back.service;

import org.springframework.http.ResponseEntity;

import com.ganacom.board_back.dto.request.board.PostBoardRequestDto;
import com.ganacom.board_back.dto.response.board.PostBoardResponseDto;

public interface BoardService {
    ResponseEntity<? super PostBoardResponseDto> postBoard(PostBoardRequestDto dto, String email);

}
