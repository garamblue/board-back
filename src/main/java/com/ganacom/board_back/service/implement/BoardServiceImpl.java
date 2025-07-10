package com.ganacom.board_back.service.implement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ganacom.board_back.dto.request.board.PostBoardRequestDto;
import com.ganacom.board_back.dto.response.ResponseDto;
import com.ganacom.board_back.dto.response.board.GetBoardResponseDto;
import com.ganacom.board_back.dto.response.board.GetFavoriteListResponseDto;
import com.ganacom.board_back.dto.response.board.PostBoardResponseDto;
import com.ganacom.board_back.dto.response.board.PutFavoriteResponseDto;
import com.ganacom.board_back.entity.BoardEntity;
import com.ganacom.board_back.entity.FavoriteEntity;
import com.ganacom.board_back.entity.ImageEntity;
import com.ganacom.board_back.repository.BoardRepository;
import com.ganacom.board_back.repository.FavoriteRepository;
import com.ganacom.board_back.repository.ImageRepository;
import com.ganacom.board_back.repository.UserRepository;
import com.ganacom.board_back.repository.resultSet.GetBoardResultSet;
import com.ganacom.board_back.repository.resultSet.GetFavoriteListResultSet;
import com.ganacom.board_back.service.BoardService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final ImageRepository imgRepository;
    private final FavoriteRepository favoriteRepository;

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

    @Override
    public ResponseEntity<? super GetBoardResponseDto> getBoard(Integer boardNumber) {
        GetBoardResultSet resultSet = null;
        List<ImageEntity> imageEntities = new ArrayList<>();

        try {
            resultSet = boardRepository.getBoard(boardNumber);// BoardRepository interface sql 호출
            if (resultSet == null)
                return GetBoardResponseDto.noExistBoard();

            imageEntities = imgRepository.findByBoardNumber(boardNumber);

            BoardEntity boardEntity = boardRepository.findByBoardNumber(boardNumber);
            boardEntity.increaseViewCount();
            boardRepository.save(boardEntity);

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetBoardResponseDto.success(resultSet, imageEntities);
    }

    @Override
    public ResponseEntity<? super PutFavoriteResponseDto> putFavorite(Integer boardNumber, String email) {
        try {
            boolean existedUser = userRepository.existsByEmail(email);
            if (!existedUser)
                return PutFavoriteResponseDto.notExistedUser();

            BoardEntity boardEntity = boardRepository.findByBoardNumber(boardNumber);
            if (boardEntity == null)
                return PutFavoriteResponseDto.notExistedBoard();

            FavoriteEntity favoriteEntity = favoriteRepository.findByBoardNumberAndUserEmail(boardNumber, email);
            if (favoriteEntity == null) {
                favoriteEntity = new FavoriteEntity(email, boardNumber);
                favoriteRepository.save(favoriteEntity);
                boardEntity.increaseFavoriteCount();// 좋아요 증가
            } else {
                favoriteRepository.delete(favoriteEntity);
                boardEntity.decreaseFavoriteCount();// 좋아요 감소
            }

            boardRepository.save(boardEntity);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return PutFavoriteResponseDto.success();
    }

    @Override
    public ResponseEntity<? super GetFavoriteListResponseDto> getFavoriteList(Integer boardNumber) {
        // interface 객체 List
        List<GetFavoriteListResultSet> resultSets = new ArrayList<>();

        try {
            boolean existedBoard = boardRepository.existsByBoardNumber(boardNumber);
            if (!existedBoard)
                return GetFavoriteListResponseDto.noExistBoard();

            resultSets = favoriteRepository.getFavoriteList(boardNumber);
            if (resultSets == null)
                return GetFavoriteListResponseDto.noExistBoard();

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseDto.databaseError();
        }
        return GetFavoriteListResponseDto.success(resultSets);
    }

}