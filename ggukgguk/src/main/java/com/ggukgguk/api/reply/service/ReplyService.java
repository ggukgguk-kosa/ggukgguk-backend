package com.ggukgguk.api.reply.service;

import java.util.List;

import com.ggukgguk.api.reply.vo.Reply;
import com.ggukgguk.api.reply.vo.ReplyNickname;

public interface ReplyService {

	List<ReplyNickname> addReply(Reply reply);

	List<ReplyNickname> editReply(Reply reply);

	List<ReplyNickname> removeReply(Reply reply);

}
