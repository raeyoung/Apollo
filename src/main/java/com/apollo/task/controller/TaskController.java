package com.apollo.task.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.View;

import com.apollo.task.service.TaskService;
import com.apollo.vo.CommentDTO;
import com.apollo.vo.StarredTaskDTO;
import com.apollo.vo.StepDTO;
import com.apollo.vo.TaskDTO;
import com.apollo.vo.TaskInStepDTO;
import com.apollo.vo.TstatusDTO;

@Controller
public class TaskController {
	
	@Autowired
	private TaskService service;
	
	@Autowired
	private View jsonview;
	
	
	/**
	 * 
	 날      짜 : 2018. 6. 15.
	 기      능 : 태스크 정보변경 >> 동적쿼리
	 작성자명 : 박 민 식
	 */
	@RequestMapping("/updateTask.htm")
	public View updateTask(TaskDTO taskdto, Model model) {
		int result = 0;
		try {
			result=service.updateTask(taskdto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		model.addAttribute("result", result);
		return jsonview;
	}
	
	public View callMemberComment(CommentDTO commentdto) {
		
		return null;
	}
	
	public View doComment(CommentDTO commentdto) {
		return null;
	}
	
	/**
	 * 
	 날      짜 : 2018. 6. 19.
	 기      능 : task modal에 필요한 정보 가져오기
	 작성자명 : 김 정 권
	 */
	@RequestMapping("/getTask.htm")
	public View getTask(int tid, HttpSession session, Model model) {

	System.out.println("컨트롤러는 탔음");
	int temp_pid = (Integer) session.getAttribute("pid");
	String pid = String.valueOf(temp_pid);
	String mid = (String) session.getAttribute("mid");

	System.out.println("pid : " + pid);
	System.out.println("mid : " + mid);
    
    ArrayList<StarredTaskDTO> starredtasklist = new ArrayList();
    starredtasklist = service.getStarredTaskList(mid);
    model.addAttribute("starredtasklist", starredtasklist);
    
	TaskDTO taskdto = new TaskDTO();
	taskdto = service.getTask(tid);
	model.addAttribute("task", taskdto);
	
	ArrayList<StepDTO> steplist = new ArrayList();
	steplist = service.getStepid(tid);
	model.addAttribute("steps", steplist);
	
	ArrayList<TstatusDTO> tstatuslist = new ArrayList();
	tstatuslist = service.gettstatuslist(pid);
	model.addAttribute("tstatuslist", tstatuslist);

	return jsonview;
	}
	
	/**
	 * 
	 날      짜 : 2018. 6. 20.
	 기      능 : 즐겨찾기 추가 혹은 삭제
	 작성자명 : 김 정 권
	 */
	@RequestMapping("/addordeletestar.htm")
	public View addordeletestar(int tid, int starAddOrDel, HttpSession session, Model model) {

		String mid = (String) session.getAttribute("mid");
		StarredTaskDTO dto = new StarredTaskDTO();
		dto.setMid(mid);
		dto.setTid(tid);
		
		int result;
		// 현재 즐겨찾기에 되어있지 않다 -> 따라서 즐겨찾기에 추가
		if(starAddOrDel == 1) {
			result = service.addstar(dto);
			model.addAttribute("result", "added");
		}
		
		// 현재 즐겨찾기에 되어있다 -> 따라서 즐겨찾기에서 삭제
		else {
			result = service.deletestar(dto);
			model.addAttribute("result", "deleted");
		}
		
	return jsonview;
	
	}
	
	/**
	 * 
	 날      짜 : 2018. 6. 20.
	 기      능 : task 삭제
	 작성자명 : 김 정 권
	 */
	@RequestMapping("/deletetask.htm")
	public String deleteTask(int tid, Model model, HttpServletRequest request) {
		
		String location = (String) request.getSession().getAttribute("location");
		
		System.out.println("tid : " + tid);
		int result = service.deleteTask(tid);
		
		System.out.println("결과는? : " + result);
		
		//pid는 지금은 그냥 가정
		String pid = "18";
	    if(location.equals("/information.htm")) {
	    	return "redirect:/information.htm?pid=" + pid;
	    } else if(location.equals("/board.htm")) {
	    	return "redirect:/board.htm";
	    } else {
	    	return null;
	    }
	    
	}
	
	/**
	 * 
	 날      짜 : 2018. 6. 21.
	 기      능 : step 삭제
	 작성자명 : 김 정 권
	 */
	@RequestMapping("/deletestepintaskmodal.htm")
	public View deleteStepInTaskModal(int tid, int sid, Model model) {
	
		TaskInStepDTO dto = new TaskInStepDTO();
		dto.setSid(sid);
		dto.setTid(tid);
		
		int result = service.deleteStepInTaskModal(dto);
		System.out.println("스텝이 지워졌니? : " + result);
		
		ArrayList<StepDTO> steplist = new ArrayList();
		steplist = service.getStepid(tid);
		model.addAttribute("steplist_after_delete_step", steplist);
		
		return jsonview;
	}
	
	
	
	/**
	 * 
	 날      짜 : 2018. 6. 20.
	 기      능 : 해당 task가 몇 개의 step에 속해 있는지 확인
	 작성자명 : 김 정 권
	 */
	@RequestMapping("/counttaskinstep.htm")
	public View countTaskInStep(int tid, Model model) {
	
		int result = service.countTaskInStep(tid);
		System.out.println("몇 개의 스텝이니? : " + result);
		model.addAttribute("countresult", result);
		
		return jsonview;
	}
	
	
	
	
	
}
