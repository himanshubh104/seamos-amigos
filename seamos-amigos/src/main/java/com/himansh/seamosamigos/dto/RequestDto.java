package com.himansh.seamosamigos.dto;

import com.himansh.seamosamigos.entity.FollowRequests;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestDto {
	private int requestId;
	private int reqesterId;
	private int requestedId;
	
	public static RequestDto genrateDto(FollowRequests request) {
		RequestDto dto=new RequestDto();
		dto.setRequestId(request.getRequestId());
		dto.setReqesterId(request.getRequestingUser().getUserId());
		return dto;
	}
}
