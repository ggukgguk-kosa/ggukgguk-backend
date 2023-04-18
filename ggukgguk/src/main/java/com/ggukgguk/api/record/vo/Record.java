package com.ggukgguk.api.record.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

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
	private Date recordCreateAt;
	private String mediaFileId;
	private double recordLocationY;
	private double recordLocationX;
	private boolean recordIsOpen;
	private String recordShareTo;
	private boolean recordShareAccepted;
}
