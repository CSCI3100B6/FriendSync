package com.friendsync.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("")
	public String getUser(HttpServletRequest request) {
		HttpSession session = request.getSession();
		StringBuilder sb = new StringBuilder();
		sb.append("Id: " + session.getId());
		sb.append("<br>CreationTime: " + session.getCreationTime());
		sb.append("<br>LastAccessedTime: " + session.getLastAccessedTime());
		return sb.toString();
	}
}
