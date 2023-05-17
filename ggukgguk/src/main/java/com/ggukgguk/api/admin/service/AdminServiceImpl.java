package com.ggukgguk.api.admin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ggukgguk.api.admin.dao.AdminDao;
import com.ggukgguk.api.admin.vo.BatchJobExecution;
import com.ggukgguk.api.admin.vo.BatchPageOption;
import com.ggukgguk.api.admin.vo.Content;
import com.ggukgguk.api.admin.vo.ContentDetail;
import com.ggukgguk.api.admin.vo.Main;
import com.ggukgguk.api.admin.vo.MediaClaimPageOption;
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
	public TotalAndListPayload contentSelectPage(PageOption option) {
		TotalAndListPayload payload = new TotalAndListPayload(
			dao.contentSelectCount(option),
			dao.contentSelectPaging(option)
		);
		
		return payload;
	}

	@Override
	public TotalAndListPayload memberSelectPage(PageOption option) {
		TotalAndListPayload payload = new TotalAndListPayload(
				dao.memberSelectCount(option),
				dao.memberSelectPaging(option)
			);
			return payload;
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
	public Map<String, List<BatchJobExecution>> fetchBatchStatus() {
		Map<String, List<BatchJobExecution>> resultMap =
				new HashMap<String, List<BatchJobExecution>>();
		
		resultMap.put("extractKeywordJob", dao.selectRecentBatchJobExecution("extractKeywordJob"));
		resultMap.put("checkModContentJob", dao.selectRecentBatchJobExecution("checkModContentJob"));

		return resultMap;
	}

	@Override
	public TotalAndListPayload fetchBatchStatusByJobName(BatchPageOption option) {
		TotalAndListPayload payload = new TotalAndListPayload();
		
		payload.setTotal(dao.selectBatchJobExecutionCount(option));
		payload.setList(dao.selectBatchJobExecution(option));
		
		return payload;
	}

	@Override
	public Map<String, Object> getDailyReportAll(String startDate, String endDate) {
		Map<String, String> option = new HashMap<String, String>();
		option.put("startDate", startDate);
		option.put("endDate", endDate);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("newMember", dao.selectMemberDailyReport(option));
		result.put("newRecord", dao.selectRecordDailyReport(option));
		result.put("newReply", dao.selectReplyDailyReport(option));
		
		return result;
	}

	@Override
	public List<Map<String, Integer>> getDailyReport(String startDate, String endDate, String reportSubject) {
		Map<String, String> option = new HashMap<String, String>();
		option.put("startDate", startDate);
		option.put("endDate", endDate);
		
		List<Map<String, Integer>> result = null;
		
		switch (reportSubject) {
		case "member":
			result = dao.selectMemberDailyReport(option);
			break;

		case "record":
			result = dao.selectRecordDailyReport(option);
			break;
			
		case "reply":
			result = dao.selectReplyDailyReport(option);
			break;
		default:
			result = null;
			break;
		}
		
		return result;
	}

	@Override
	public TotalAndListPayload getMediaClaim(MediaClaimPageOption option) {
		TotalAndListPayload payload = new TotalAndListPayload(
			dao.selectMediaClaimCount(option),
			dao.selectMediaClaim(option)
		);
		
		return payload;
	}

}
