package com.ggukgguk.api.admin.vo;

import java.util.Date;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Notice {
	private int noticeId;
	private String noticeTitle;
	private String noticeContent;
	private String writer;
	private Date noticeCreatedAt;

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object == null || getClass() != object.getClass())
			return false;
		Notice notice = (Notice) object;
		return Objects.equals(noticeId, notice.noticeId) && Objects.equals(noticeTitle, notice.noticeTitle)
				&& Objects.equals(noticeContent, notice.noticeContent) && Objects.equals(writer, notice.writer);
	}

	@Override
	public int hashCode() {
		return Objects.hash(noticeId, noticeTitle, noticeContent, writer);
	}

	public Notice(String noticeTitle, String noticeContent, String writer){
	        this.noticeTitle = noticeTitle;
	        this.noticeContent = noticeContent;
	        this.writer = writer;
	    }
}
