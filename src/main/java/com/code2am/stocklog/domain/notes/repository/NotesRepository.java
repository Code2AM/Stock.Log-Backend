package com.code2am.stocklog.domain.notes.repository;

import com.code2am.stocklog.domain.notes.models.entity.Notes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotesRepository extends JpaRepository<Notes, Integer> {

    @Query("SELECT n FROM Notes n WHERE n.userId = ?1")
    List<Notes> findAllByUserId(Integer userId);
}
