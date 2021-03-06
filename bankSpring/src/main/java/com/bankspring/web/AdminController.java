package com.bankspring.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bankspring.factory.CommandFactory;
import com.bankspring.serviceImpl.MemberServiceImpl;

@Controller
@RequestMapping(value="/admin")
public class AdminController {
	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
	@Autowired MemberServiceImpl memberService;
	
	@RequestMapping("/member.do")
	// command , pageNo 필수값, 나머지는 선택값
	public ModelAndView member(@RequestParam("command")String command,
			@RequestParam(value="pageNo",defaultValue="1")String pageNo,
			@RequestParam(value="searchKey",required=false)String searchKey,
			@RequestParam(value="searchVal",required=false)String searchVal,
			ModelAndView mav
			){
		logger.info("관리자 메인페이지 에서 넘어온 명령어 = {}, 페이지넘버={}", command,pageNo);
		switch (command) {
		case "list":
			mav.addObject("memberList", memberService.memberList(CommandFactory.list(pageNo)));
			mav.addObject("count", memberService.memberCountAll());
			mav.setViewName("admin/main");
			break;
		case "search":
			switch (searchKey) {
			case "list":
				mav.addObject("memberList", memberService.memberList(CommandFactory.list(pageNo)));
				mav.addObject("count", memberService.memberCountAll());
				mav.setViewName("admin/main");
				break;
			case "userid":
				mav.addObject("member", memberService.memberDetail(CommandFactory.search(searchKey, searchVal)));
				mav.setViewName("admin/search");
				break;
			case "name":
				mav.addObject("memberList", memberService.searchByKeyword(CommandFactory.search(pageNo,searchKey, searchVal)));
				mav.addObject("count", memberService.memberCountSome(CommandFactory.countSome(searchKey, searchVal)));
				mav.setViewName("admin/main");
				break;

			default:
				break;
			}
			break;
		default:
			break;
		}
		
		return mav;
	}
}
