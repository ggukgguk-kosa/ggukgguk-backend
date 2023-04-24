package com.ggukgguk.api.admin.vo;

import lombok.Data;

@Data
public class NoticeOption {

	private int page;
	private int size;
	private int skip; // �뒪�궢 �븷 寃뚯떆臾� �닔( (pageNum-1) * amount )
	
	public NoticeOption() {
		this(1,  10);
	}
	
	public NoticeOption(int page, int size) {
		this.page = page;
		this.size = size;
		computeSkip();
	}
	
	public void setPage(int page) {
		this.page = page;
		computeSkip();
	}
	
	public void setSize(int size) {
		this.size = size;
		computeSkip();
	}
	
	private void computeSkip() {
		this.skip = (page - 1) * size;
	}
}
