package com.ganacom.board_back.service;

import org.springframework.http.ResponseEntity;

import com.ganacom.board_back.dto.request.board.PostBoardRequestDto;
import com.ganacom.board_back.dto.response.board.GetBoardResponseDto;
import com.ganacom.board_back.dto.response.board.PostBoardResponseDto;

public interface BoardService {
    // 게시물 선택 시 해당 게시물 상세조회
    ResponseEntity<? super GetBoardResponseDto> getBoard(Integer boardNumber);

    // 게시물 등록
    ResponseEntity<? super PostBoardResponseDto> postBoard(PostBoardRequestDto dto, String email);

}
