package com.ggukgguk.api.admin.vo;

import java.util.Date;

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
    private Date noticeCreatedAt;
}
