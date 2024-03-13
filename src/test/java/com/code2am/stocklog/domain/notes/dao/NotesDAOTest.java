package com.code2am.stocklog.domain.notes.dao;

import com.code2am.stocklog.domain.notes.models.vo.NotesVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(NotesDAO.class) // MyBatisTest는 MyBatis 관련 설정만 로드하기 때문에, DAO를 수동으로 Import 해야 합니다.
public class NotesDAOTest {

    @Autowired
    private NotesDAO notesDAO;

    @Test
    public void readNotesByJournalId_저장된_노트를_정상적으로_불러온다() {
        // given
        Integer journalId = 1; // 테스트에 사용할 임의의 journalId

        // when
        List<NotesVo> result = notesDAO.readNotesByJournalId(journalId);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty(); // 실제 데이터가 있다고 가정할 때
        // 결과 검증이 추가적으로 필요할 수 있습니다. 예를 들어, 결과 리스트의 첫 번째 요소에 대한 특정 필드 검증 등
    }
}