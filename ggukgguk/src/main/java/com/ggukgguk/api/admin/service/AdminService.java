package com.ggukgguk.api.admin.service;

import java.util.List;

import com.ggukgguk.api.admin.vo.Content;
import com.ggukgguk.api.admin.vo.ContentDetail;
import com.ggukgguk.api.admin.vo.Main;
import com.ggukgguk.api.admin.vo.Member;
import com.ggukgguk.api.admin.vo.Notice;
import com.ggukgguk.api.common.vo.PageOption;
import com.ggukgguk.api.common.vo.TotalAndListPayload;

public interface AdminService {

	// 공지사항 리스트 출력
	public List<Notice> noticeSelectPage(PageOption option);

	// 게시글 작성
    public boolean noticeWrite(Notice notice);

    // 게시글 조회
	public boolean noticeRead(int noticeId);

	// 게시글 수정
	public boolean noticeUpdate(int noticeId);

	// 게시글 삭제
	public boolean noticeDelete(int noticeId);

	// 컨텐츠 리스트 출력
	public List<Content> contentSelectPage(PageOption option);

	public List<Member> memberSelectPage(PageOption option);

	// 회원삭제
	public boolean memberDelete(String memberId);

	Main mainAdmin();

	public List<ContentDetail> recordRead(int recordId);

//	public TotalAndListPayload getMemberList(PageOption option);
}
