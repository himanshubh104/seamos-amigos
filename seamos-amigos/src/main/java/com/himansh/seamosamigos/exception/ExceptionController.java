package com.himansh.seamosamigos.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {
	
	private Logger log=LoggerFactory.getLogger(this.getClass());
	
	@ExceptionHandler(value = InAppException.class)
	public ResponseEntity<Map<String, String>> inAppException(InAppException ex){
		log.error(ex.getMessage(), ex);
		HashMap<String, String> resp=new HashMap<String, String>();
		resp.put("message", ex.getMessage());
		return ResponseEntity.status(200).body(resp);
	}
	
	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<Map<String, String>> generalException(Exception ex){
		log.error(ex.getMessage(), ex);
		HashMap<String, String> resp=new HashMap<String, String>();
		resp.put("message", ex.getMessage());
		return ResponseEntity.status(500).body(resp);
	}

}
