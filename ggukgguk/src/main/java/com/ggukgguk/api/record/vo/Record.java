package com.ggukgguk.api.record.vo;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ggukgguk.api.reply.vo.ReplyNickname;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Record {

	private int recordId;
	private String memberId;
	private String recordComment;
	@JsonFormat(pattern = "yyyy-MM-dd'T'hh:mm:ss.SSSZ", timezone = "Asia/Seoul")
	private Date recordCreatedAt;
	private String mediaFileId;
	private double recordLocationY;
	private double recordLocationX;
	private boolean recordIsOpen;
	private String recordShareTo;
	private boolean recordShareAccepted;
	private List<ReplyNickname> replyList;
}
