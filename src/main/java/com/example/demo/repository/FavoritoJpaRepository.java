package com.example.demo.repository;

import com.example.demo.entity.FavoritoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoritoJpaRepository extends JpaRepository<FavoritoEntity, Long> {
}
