package com.ggukgguk.api.admin.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ggukgguk.api.admin.vo.Notice;
import com.ggukgguk.api.admin.vo.NoticeOption;

@Repository
public class AdminDaoImpl implements AdminDao {
    @Autowired
    SqlSession session;
   
    @Override
    public void insertNotice(Notice notice) throws Exception {
        int affectedRow = session.insert("com.ggukgguk.api.Notice.insert", notice);
        
        if (affectedRow != 1) {
            throw new Exception();
        }
    }
    
    @Override
    public void readNotice(int noticeId) throws Exception {
        int affectedRow = session.selectOne("com.ggukgguk.api.Notice.select", noticeId);
        
        if (affectedRow != 1) {
            throw new Exception();
        }
    }
    
    @Override
    public void updateNotice(int noticeId) throws Exception {
        int affectedRow = session.update("com.ggukgguk.api.Notice.update", noticeId);
        
        if (affectedRow != 1) {
            throw new Exception();
        }
    }
    @Override
    public void deleteNotice(int noticeId) throws Exception {
        int affectedRow = session.delete("com.ggukgguk.api.Notice.delete", noticeId);
        
        if (affectedRow != 1) {
            throw new Exception();
        }
    }
    public List<Notice> pagingInsertBoard(NoticeOption option) {
		
		return session.selectList("com.ggukgguk.api.Notice.selectPage",option);
	}
    
	@Override
	public int totalCount() {
		return session.selectOne("com.ggukgguk.api.Notice.increaseViewCnt");
	}


}

