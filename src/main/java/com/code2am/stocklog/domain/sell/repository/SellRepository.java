package com.code2am.stocklog.domain.sell.repository;

import com.code2am.stocklog.domain.journals.models.entity.Journals;
import com.code2am.stocklog.domain.notes.models.entity.Notes;
import com.code2am.stocklog.domain.sell.models.entity.Sell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SellRepository extends JpaRepository<Sell, Integer> {

    @Query ("SELECT s FROM Sell s WHERE s.journal = ?1 AND s.status = 'Y'")
    List<Sell> findAllByJournal(Journals journal);

}
