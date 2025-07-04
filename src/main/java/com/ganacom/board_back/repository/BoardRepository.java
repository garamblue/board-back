package com.ganacom.board_back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ganacom.board_back.entity.BoardEntity;

//PK type is Integer
@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Integer> {

}
