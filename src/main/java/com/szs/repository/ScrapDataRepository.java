package com.szs.repository;

import com.szs.entity.ScrapData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScrapDataRepository extends JpaRepository<ScrapData, String> {
    void deleteByUserId(String userId);

    List<ScrapData> getByUserId(String userId);
}
