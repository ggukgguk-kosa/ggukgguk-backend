package com.ggukgguk.api.notification.vo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
	
	private int notificationId;
	private String notificationTypeId;
	private Date notificationCreatedAt;
	private int referenceId;
	private String receiverId;
	private int notificationIsRead;
	private String notificationMessage;
}
