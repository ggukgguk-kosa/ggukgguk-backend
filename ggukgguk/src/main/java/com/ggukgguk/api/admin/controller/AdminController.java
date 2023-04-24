package com.ggukgguk.api.admin.controller;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ggukgguk.api.admin.service.AdminService;
import com.ggukgguk.api.admin.vo.Notice;
import com.ggukgguk.api.common.vo.PageHandler;

@Controller
@RequestMapping("/board")
public class AdminController {
	@Autowired
	AdminService adminService;

	@PostMapping("/modify")
	public String modify(Notice notice, RedirectAttributes rattr, Model model, HttpSession session) {
		String writer = (String) session.getAttribute("id");
		notice.setWriter(writer);

		try {
			if (adminService.modify(notice) != 1)
				throw new Exception("수정 완료");

			rattr.addFlashAttribute("msg", "MOD_OK");
			return "redirect:/board/list";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute(notice);
			model.addAttribute("msg", "MOD_ERR");
			return "board";
		}
	}

	@GetMapping("/write")
	public String write(Model model) {
		model.addAttribute("mode", "new");

		return "board";
	}

	@PostMapping("/write")
	public String write(Notice notice, RedirectAttributes rattr, Model model, HttpSession session) {
		String writer = (String) session.getAttribute("id");
		notice.setWriter(writer);

		try {
			if (adminService.write(notice) != 1)
				throw new Exception("Write failed.");

			rattr.addFlashAttribute("msg", "WRT_OK");
			return "redirect:/board/list";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute(notice);
			model.addAttribute("mode", "new");
			model.addAttribute("msg", "WRT_ERR");
			return "board";
		}
	}

	@GetMapping("/read")
	public String read(int noticeId, RedirectAttributes rattr, Model model) {
		try {
			Notice notice = adminService.read(noticeId);
			model.addAttribute(notice);
		} catch (Exception e) {
			e.printStackTrace();
			rattr.addFlashAttribute("msg", "READ_ERR");
			return "redirect:/board/list";
		}

		return "board";
	}

	@PostMapping("/remove")
	public String remove(int noticeId, RedirectAttributes rattr, HttpSession session) {
		String writer = (String) session.getAttribute("id");
		String msg = "삭제�";

		try {
			if (adminService.remove(noticeId, writer) != 1)
				throw new Exception("삭제 완료");
		} catch (Exception e) {
			e.printStackTrace();
			msg = "삭제 실패";
		}

		rattr.addFlashAttribute("msg", msg);
		return "redirect:/board/list";
	}

	@GetMapping("/list")
	public String list(Model model, HttpServletRequest request) {
		if (!loginCheck(request))
			return "redirect:/login/login?toURL=" + request.getRequestURL(); // 濡쒓렇�씤�쓣 �븞�뻽�쑝硫� 濡쒓렇�씤 �솕硫댁쑝濡� �씠�룞

		try {
			PageHandler pageHandler = new PageHandler();
			model.addAttribute("ph", pageHandler);

			Instant startOfToday = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant();
			model.addAttribute("startOfToday", startOfToday.toEpochMilli());
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("msg", "LIST_ERR");
			model.addAttribute("totalCnt", 0);
		}

		return "boardList";
	}

	private boolean loginCheck(HttpServletRequest request) {

		HttpSession session = request.getSession(false);
		return session != null && session.getAttribute("id") != null;
	}
}
