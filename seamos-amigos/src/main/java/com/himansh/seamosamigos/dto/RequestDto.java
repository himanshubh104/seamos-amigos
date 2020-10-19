package com.himansh.seamosamigos.dto;

import com.himansh.seamosamigos.entity.FollowRequests;

public class RequestDto {
	private int requestId;
	private int reqesterId;
	private int requestedId;
	public int getRequestId() {
		return requestId;
	}
	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}
	public int getReqesterId() {
		return reqesterId;
	}
	public void setReqesterId(int reqesterId) {
		this.reqesterId = reqesterId;
	}
	public int getRequestedId() {
		return requestedId;
	}
	public void setRequestedId(int requestedId) {
		this.requestedId = requestedId;
	}
	
	public static RequestDto genrateDto(FollowRequests request) {
		RequestDto dto=new RequestDto();
		dto.setRequestId(request.getRequestId());
		dto.setReqesterId(request.getRequestingUser().getUserId());
		return dto;
	}
}
