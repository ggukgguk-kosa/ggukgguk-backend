package com.ggukgguk.api.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BasicResp <T> {

	public String status;
	public String message;
	public T data;
}
