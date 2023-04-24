package com.ggukgguk.api.admin.dao;

import java.util.List;
import java.util.Map;

import com.ggukgguk.api.admin.vo.Notice;

public interface AdminDao {
    Notice select(int noticeId) throws Exception;
    int delete(int noticeId, String writer) throws Exception;
    int insert(Notice notice) throws Exception;
    int update(Notice notice) throws Exception;
    int increaseViewCnt(int noticeId) throws Exception;

    List<Notice> selectPage(Map map) throws Exception;
    List<Notice> selectAll() throws Exception;
    int count() throws Exception;
    
//    int deleteAll() throws Exception;
//    int searchResultCnt(SearchCondition sc) throws Exception;
//    List<Notice> searchSelectPage(SearchCondition sc) throws Exception;
}
