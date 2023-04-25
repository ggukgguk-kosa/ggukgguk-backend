package com.ggukgguk.api.admin.vo;

import lombok.Data;

@Data
public class NoticeOption {

	int page;
	int size;
	int skip; 
	
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
