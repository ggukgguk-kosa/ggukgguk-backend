package com.ggukgguk.api.record.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ggukgguk.api.record.dao.ReplyDao;
import com.ggukgguk.api.record.vo.Reply;
import com.ggukgguk.api.record.vo.ReplyNickname;

@Service
public class ReplyServiceImpl implements ReplyService {

	@Autowired
	private ReplyDao dao;
	
	@Override
	public List<ReplyNickname> addReply(Reply reply) {
		
		try {
			dao.insertReply(reply);
			return dao.selectReplies(reply.getRecordId());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public List<ReplyNickname> editReply(Reply reply) {
		
		try {
			dao.updateReply(reply);
			return dao.selectReplies(reply.getRecordId());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public List<ReplyNickname> removeReply(Reply reply) {
		
		try {
			dao.removeReply(reply.getReplyId());
			return dao.selectReplies(reply.getRecordId());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
}
