package com.ggukgguk.api.reply.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyNickname {

	private int replyId;
	private String replyContent;
	@JsonFormat(pattern = "yyyy-MM-dd'T'hh:mm:ss.SSSZ", timezone = "Asia/Seoul")
	private Date replyDate;
	private int recordId;
	private String memberNickname;
	
}
