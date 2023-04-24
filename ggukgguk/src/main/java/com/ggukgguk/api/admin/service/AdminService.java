package com.ggukgguk.api.admin.service;

import java.util.List;
import java.util.Map;

import com.ggukgguk.api.admin.vo.Notice;

public interface AdminService {
    int getCount() throws Exception;
    int remove(int noticeId, String writer) throws Exception;
    int write(Notice notice) throws Exception;
    List<Notice> getList() throws Exception;
    Notice read(int noticeId) throws Exception;
    List<Notice> getPage(Map map) throws Exception;
    int modify(Notice notice) throws Exception;

//    int getSearchResultCnt(SearchCondition sc) throws Exception;
//    List<Notice> getSearchResultPage(SearchCondition sc) throws Exception;
}
