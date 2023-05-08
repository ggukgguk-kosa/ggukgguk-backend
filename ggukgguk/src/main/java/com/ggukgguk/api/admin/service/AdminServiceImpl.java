package com.ggukgguk.api.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ggukgguk.api.admin.dao.AdminDao;
import com.ggukgguk.api.admin.vo.Content;
import com.ggukgguk.api.admin.vo.ContentDetail;
import com.ggukgguk.api.admin.vo.Main;
import com.ggukgguk.api.admin.vo.Member;
import com.ggukgguk.api.admin.vo.Notice;
import com.ggukgguk.api.common.vo.PageOption;
import com.ggukgguk.api.common.vo.TotalAndListPayload;

@Service
public class AdminServiceImpl implements AdminService {
	@Autowired
	AdminDao dao;

	@Override
	public List<Notice> noticeSelectPage(PageOption option) {
		return dao.noticeSelectPaging(option);
	}

	public boolean noticeWrite(Notice notice) {
		try {
			dao.noticeInsert(notice);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean noticeRead(int noticeId) {
		try {
			dao.noticeSelect(noticeId);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean noticeUpdate(int noticeId) {
		try {
			dao.updateNotice(noticeId);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean noticeDelete(int noticeId) {
		try {
			dao.deleteNotice(noticeId);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<Content> contentSelectPage(PageOption option) {
		return dao.contentSelectPaging(option);
	}

	@Override
	public List<Member> memberSelectPage(PageOption option) {
		return dao.memberSelectPaging(option);

	}

	// 회원 삭제
	@Override
	public boolean memberDelete(String memberId) {
		try {
			dao.memberDelete(memberId);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public Main mainAdmin() {
		Main main = new Main();
		try {
			int totalMember = dao.totalMember();
			main.setTotalMember(totalMember);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			int todayMember = dao.todayMember();
			main.setTodayMember(todayMember);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			int totalContent = dao.totalContent();
			main.setTotalContent(totalContent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			int todayContent = dao.todayContent();
			main.setTodayContent(todayContent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return main;
	}

	@Override
	public List<ContentDetail> recordRead(int recordId) {
		return dao.recordSelectList(recordId);
	}

//	// 페이징 처리를 위한 전체 게시글 리스트 조회
//	@Override
//	public TotalAndListPayload getMemberList(PageOption option) {
//		TotalAndListPayload payload = new TotalAndListPayload();
//		payload.setList(dao.selectMemberList(option)); // 전체 회원 리스트 조회
//		payload.setTotal(dao.selectMemberListTotal(option)); // 페이징 처리를 위한 전체회원 수 구하기
//		return payload;
//	}
}
