package com.ggukgguk.api.admin.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ggukgguk.api.admin.vo.BatchJobExecution;
import com.ggukgguk.api.admin.vo.BatchPageOption;
import com.ggukgguk.api.admin.vo.Content;
import com.ggukgguk.api.admin.vo.ContentDetail;
import com.ggukgguk.api.admin.vo.Member;
import com.ggukgguk.api.admin.vo.Notice;
import com.ggukgguk.api.common.vo.PageOption;

@Repository
public class AdminDaoImpl implements AdminDao {
    @Autowired
    SqlSession session;
   
    @Override
    public void noticeInsert(Notice notice) throws Exception {
        int affectedRow = session.insert("com.ggukgguk.api.Admin.noticeInsert", notice);
        
        if (affectedRow != 1) {
            throw new Exception();
        }
    }
    
    @Override
    public void noticeSelect(int noticeId) throws Exception {
        int affectedRow = session.selectOne("com.ggukgguk.api.Admin.noticeSelect", noticeId);
        
        if (affectedRow != 1) {
            throw new Exception();
        }
    }
    
    @Override
    public void updateNotice(int noticeId) throws Exception {
        int affectedRow = session.update("com.ggukgguk.api.Admin.noticeUpdate", noticeId);
        
        if (affectedRow != 1) {
            throw new Exception();
        }
    }
    @Override
    public void deleteNotice(int noticeId) throws Exception {
        int affectedRow = session.delete("com.ggukgguk.api.Admin.noticeDelete", noticeId);
        
        if (affectedRow != 1) {
            throw new Exception();
        }
    }
    public List<Notice> noticeSelectPaging(PageOption option) {
		
		return session.selectList("com.ggukgguk.api.Admin.noticeSelectPage",option);
	}
    

	@Override
	public List<Content> contentSelectPaging(PageOption option) {
		return session.selectList("com.ggukgguk.api.Admin.contentSelectList",option);
	}
	
	@Override
	public List<Member> memberSelectPaging(PageOption option) {
		return session.selectList("com.ggukgguk.api.Admin.memberSelectList",option);
	}

	// 회원 삭제
	@Override
	public void memberDelete(String memberId) throws Exception {
        int affectedRow = session.delete("com.ggukgguk.api.Admin.memberDelete", memberId);
        
        if (affectedRow != 1) {
            throw new Exception();
        }
	}
	
	@Override
	public int totalMember() {
		return session.selectOne("com.ggukgguk.api.Admin.totalMemberSelect");
	}
	@Override
	public int todayMember() {
		return session.selectOne("com.ggukgguk.api.Admin.todayMemberSelect");
	}
	@Override
	public int totalContent() {
		return session.selectOne("com.ggukgguk.api.Admin.totalContentSelect");
	}
	@Override
	public int todayContent() {
		return session.selectOne("com.ggukgguk.api.Admin.todayContentSelect");
	}

	@Override
	public List<ContentDetail> recordSelectList(int recordId) {
		return session.selectList("com.ggukgguk.api.Admin.recordSelectList", recordId);
	}

	@Override
	public int contentSelectCount(PageOption option) {
		return session.selectOne("com.ggukgguk.api.Admin.selectContentTotal",option);
	}
	
	
//	@Override // 전체 컨텐츠 리스트 
//	public List<?> selectMemberList(PageOption option) {
//		return session.selectList("com.ggukgguk.api.Member.totalMemberList",option);
//	}

//	@Override // 페이징 처리를 위한 전체 컨텐츠 수 구하기
//	public int selectMemberListTotal(PageOption option) {
//		return session.selectOne("com.ggukgguk.api.Member.selectMemberTotal", option);
//	}

	
	@Override
	public List<BatchJobExecution> selectRecentBatchJobExecution(String jobName) {
		return session.selectList("com.ggukgguk.api.Admin.selectRecentBatchJobExecution", jobName);
	}

	@Override
	public List<BatchJobExecution> selectBatchJobExecution(BatchPageOption option) {
		return session.selectList("com.ggukgguk.api.Admin.selectBatchJobExecution", option);
	}

	@Override
	public int selectBatchJobExecutionCount(BatchPageOption option) {
		return session.selectOne("com.ggukgguk.api.Admin.selectBatchJobExecutionCount", option);
	}
}

