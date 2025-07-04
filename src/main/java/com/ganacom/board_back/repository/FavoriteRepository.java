package com.ganacom.board_back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ganacom.board_back.entity.FavoriteEntity;
import com.ganacom.board_back.entity.pKey.FavoritePk;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteEntity, FavoritePk> {
    // Custom query methods (if needed) can be defined here
}
