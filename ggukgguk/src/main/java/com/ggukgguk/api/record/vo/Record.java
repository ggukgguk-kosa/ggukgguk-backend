package com.ggukgguk.api.record.vo;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
<<<<<<< HEAD
=======
import com.ggukgguk.api.record.vo.ReplyNickname;
>>>>>>> 50eba2ab7301fdf647dc0a28790cdf2a0cc0d8c2

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
