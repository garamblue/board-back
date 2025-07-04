package com.ganacom.board_back.service.implement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ganacom.board_back.dto.request.board.PostBoardRequestDto;
import com.ganacom.board_back.dto.response.ResponseDto;
import com.ganacom.board_back.dto.response.board.PostBoardResponseDto;
import com.ganacom.board_back.entity.BoardEntity;
import com.ganacom.board_back.entity.ImageEntity;
import com.ganacom.board_back.repository.BoardRepository;
import com.ganacom.board_back.repository.ImageRepository;
import com.ganacom.board_back.repository.UserRepository;
import com.ganacom.board_back.service.BoardService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final ImageRepository imgRepository;

    @Override
    public ResponseEntity<? super PostBoardResponseDto> postBoard(PostBoardRequestDto dto, String email) {
        try {
            boolean existedEmail = userRepository.existsByEmail(email);
            if (!existedEmail)
                return PostBoardResponseDto.notExistedUser();

            BoardEntity boardEntity = new BoardEntity(dto, email);
            // 1.게시물 내용 저장
            boardRepository.save(boardEntity);

            // 2.저장 후 auto 생성된 Board number 를 가져옴
            int boardNumber = boardEntity.getBoardNumber();

            List<String> boardImageList = dto.getBoardImageList();
            List<ImageEntity> imageEntities = new ArrayList<>();

            for (String image : boardImageList) {
                ImageEntity imageEntity = new ImageEntity(boardNumber, image);
                imageEntities.add(imageEntity);
            }
            // 3.게시물과 연결된 이미지들 저장
            imgRepository.saveAll(imageEntities);

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseDto.databaseError();
        }
        return PostBoardResponseDto.success();
    }

}