package com.sanfumall.admin.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sanfumall.admin.service.GradeService;
import com.sanfumall.admin.service.UserService;
import com.sanfumall.common.base.controller.BaseController;
import com.sanfumall.common.pojo.entity.Grade;
import com.sanfumall.common.pojo.entity.User;
import com.sanfumall.common.pojo.vo.Page;

@Controller("gradeController")
@RequestMapping("/grade")
public class GradeController extends BaseController {

	@Resource(name="userService")
	private UserService userService;
	@Resource(name="gradeService")
	private GradeService gradeService;
	
	/**
	 * 分页展示会员等级列表
	 * @param pageNum 页码
	 * @param pageSize 每页显示的条数
	 * @param keyword 模糊查询的参数，如果为null,则进行普通分页查询，否则进行模糊分页查询
	 * @return grade_index.html
	 * @throws Exception
	 */
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public ModelAndView getgradeIndex(Integer pageNum, Integer pageSize, String keyword) throws Exception {
		// 首先，创建分页对象
		Page<Grade> page = new Page<>(pageNum, pageSize, keyword);
		page = gradeService.getGradeListByPage(page);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("page", page);
		return new ModelAndView("grade/grade_index", resultMap);
	}
	
	/**
	 * 加载会员等级添加页面
	 * @return grade_add.html
	 * @throws Exception
	 */
	@RequestMapping(value="/add", method=RequestMethod.GET)
	public String getAddPage() throws Exception {
		return "grade/grade_add";
	}
	
	/**
	 * 添加会员等级
	 * @param grade
	 * @return Boolean
	 * @throws Exception
	 */
	@RequestMapping(value="/add", method=RequestMethod.POST)
	@ResponseBody
	public Boolean addGrade(Grade grade) throws Exception {
		// 校验grade对象是否合法（前端校验有可能失效）
		if (checkGrade(grade)) {
			// 校验成功，获取当前登录用户，记录创建者
			User user = userService.getUserByUserId(((User) session.getAttribute("user")).getUserId());
			grade.setCreateUser(user);
			grade.setCreateTime(new Date());
			// 调用业务层保存会员等级
			return gradeService.savaGrade(grade);
		}
		return false;
	}

	/**
	 * 加载修改页面
	 * @param gradeId
	 * @return grade_update.html
	 * @throws Exception
	 */
	@RequestMapping(value="/update", method=RequestMethod.GET)
	public ModelAndView getUpdatePage(Long gradeId) throws Exception {
		Grade grade = gradeService.getGradeByGradeId(gradeId);
		return new ModelAndView("grade/grade_update", "grade", grade);
	}
	
	/**
	 * 修改grade对象
	 * @param grade
	 * @return Boolean
	 * @throws Exception
	 */
	@RequestMapping(value="/update", method=RequestMethod.PUT)
	@ResponseBody
	public Boolean updateGrade(Grade grade) throws Exception {
		// 校验grade对象是否合法（前端校验有可能失效）
		if (checkGrade(grade)) {
			// 根据gradeId获得数据库中原有信息，并将未修改的信息进行回填
			Grade origGrade = gradeService.getGradeByGradeId(grade.getGradeId());
			// 将排序字段,创建人,创建时间回填
			grade.setCreateUser(origGrade.getCreateUser());
			grade.setCreateTime(origGrade.getCreateTime());
			// 记录修改者，修改时间
			User user = userService.getUserByUserId(((User) session.getAttribute("user")).getUserId());
			grade.setUpdateUser(user);
			grade.setUpdateTime(new Date());
			// 调用业务层保存grade对象
			return gradeService.savaGrade(grade);
			}
		return false;
	}
	
	/**
	 * 在添加或者修改过程中对会员等级名称进行唯一性校验
	 * @param gradeName 
	 * @param gradeId 如果是修改，则存在ID
	 * @return Boolean 如果违反了唯一性规则，则返回false,否则,返回true
	 * @throws Exception
	 */
	@RequestMapping(value="/remoteValidateGradeInfo", method=RequestMethod.GET)
	@ResponseBody
	public Boolean remoteValidateGradeInfo(Long gradeId, String gradeName) throws Exception {
		// 非空校验
		if (gradeName != null && !"".equals(gradeName.trim())) {
			// 根据gradeName获取grade对象
			Grade grade = gradeService.getGradeByGradeName(gradeName);
			if (grade == null || (gradeId != null && grade.getGradeId() == gradeId)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 校验grade对象
	 * @param grade
	 * @return Boolean
	 */
	private boolean checkGrade(Grade grade) {
		String gradeName = grade.getGradeName();
		if (gradeName != null && !"".equals(gradeName.trim())) {
			return true;
		}
		return false;
	}
	
}
