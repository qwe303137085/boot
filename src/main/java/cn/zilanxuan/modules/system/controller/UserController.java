package cn.zilanxuan.modules.system.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;

import cn.zilanxuan.base.BaseController;
import cn.zilanxuan.base.MySysUser;
import cn.zilanxuan.modules.system.entity.Role;
import cn.zilanxuan.modules.system.entity.User;
import cn.zilanxuan.modules.system.entity.vo.ShowMenu;
import cn.zilanxuan.modules.system.service.UserService;
import cn.zilanxuan.redis.CacheUtils;
import cn.zilanxuan.util.Constants;
import cn.zilanxuan.util.LayerData;
import cn.zilanxuan.util.PasswordUtil;
import cn.zilanxuan.util.RestResponse;

/**
 * 功能描述：前端控制器
 * @author zilanxuan
 * @website http://www.zilanxuan.cn
 * @version 1.0
 * @date 2020年4月12日 上午10:34:09
 */
@Controller
@RequestMapping("/api/system/user")
public class UserController extends BaseController{

	private Logger log = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/getUserMenu")
	@ResponseBody
	public RestResponse getUserMenu() {
		Long userId = MySysUser.id();
		List<ShowMenu> list = menuService.getShowMenuByUser(userId);
		return RestResponse.success().setData(list);
	}
	
	/**
	 * 获取用户列表
	 * @param page
	 * @param limit
	 * @param request
	 * @return
	 */
	@RequiresPermissions("sys:user:list")
	@RequestMapping("list")
	@ResponseBody
	public LayerData<User> list(@RequestParam(value="page",defaultValue="1")Integer page,
								@RequestParam(value="limit",defaultValue="10")Integer limit,
								ServletRequest request) {
		Map map =WebUtils.getParametersStartingWith(request, "s_");
		LayerData<User> layerData = new LayerData<User>();
		QueryWrapper<User> wrapper = new QueryWrapper<User>();
		if(!map.isEmpty()) {
			String keys = (String)map.get("key");
			if(StringUtils.isNotBlank(keys)) {
				wrapper.like("login_name", keys).or().like("tel", keys).or().like("email", keys);
			}
		}
		
		IPage<User> p = userService.page(new Page<User>(page,limit),wrapper);
		layerData.setCount((int)p.getTotal());
		layerData.setData(p.getRecords());
		
		
		return layerData;
	}
	
	/**
	 * 获取所有角色列表
	 * @return
	 */
	@PostMapping("getAllRoles")
	@ResponseBody
	public RestResponse getAllRoles() {
		List<Role> roles = roleService.selectAll();
		return RestResponse.success().setData(roles);
	}
	
	@RequiresPermissions("sys:user:add")
	@PostMapping("add")
	@ResponseBody
	public RestResponse add(@RequestBody User user) {
		System.out.println(user.getLoginName()+"-----------");
		if(StringUtils.isBlank(user.getLoginName())) {
			return RestResponse.failure("用户登陆名不能为空");
		}
		if(null == user.getRoleLists() || user.getRoleLists().size() == 0) {
			return RestResponse.failure("用户角色至少需要分配一个");
		}
		if(userService.userCount(user.getLoginName()) > 0) {
			return RestResponse.failure("用户登陆名已存在");
		}
		if(StringUtils.isNotBlank(user.getEmail())) {
			if(userService.userCount(user.getEmail()) > 0) {
				return RestResponse.failure("该邮箱已被使用");
			}
		}
		if(StringUtils.isNotBlank(user.getTel())) {
			if(userService.userCount(user.getTel()) > 0) {
				return RestResponse.failure("该手机号已被绑定");
			}
		}
		//当用户未输入密码，系统就使用默认密码
		if(StringUtils.isBlank(user.getPassword())) {
			user.setPassword(Constants.DEFAULT_PASSWORD);
		}
		//保存用户信息
		userService.saveUser(user);
		if(user.getId() == null || user.getId() == 0) {
			return RestResponse.failure("保存用户信息失败");
		}
		//保存用户和角色的关系
		userService.saveUserRoles(user.getId(),user.getRoleLists());
		return RestResponse.success();
	}
	
	
	/**
	 * 根据用户id获取角色列表
	 * @param id
	 * @return
	 */
	@RequestMapping("/getRolesByUser")
	@ResponseBody
	public RestResponse getRolesByUser(Long id) {
		User user = userService.findUserById(id);
		StringBuilder sb = new StringBuilder();
		if(null != user) {
			Set<Role> roleSet = user.getRoleLists(); 
			if(null != roleSet && roleSet.size() > 0) {
				for(Role r :roleSet) {
					sb.append(r.getId().toString()).append(",");
				}
			}
		}
		List<Role> roleList = roleService.selectAll();
		Map<String,Object> resultMap = Maps.newHashMap();
		resultMap.put("roleIds", sb);
		resultMap.put("roleLists", roleList);
		return RestResponse.success().setData(resultMap);
	}
	
	
	
	
	
	
	@RequiresPermissions("sys:user:edit")
	@PostMapping("edit")
	@ResponseBody
	public RestResponse edit(@RequestBody User user) {
		if(null == user.getId() || user.getId() == 0) {
			return RestResponse.failure("用户ID不能为空");
		}
		if(StringUtils.isBlank(user.getLoginName())) {
			return RestResponse.failure("登陆名不能为空");
		}
		if(null == user.getRoleLists() || user.getRoleLists().size() == 0) {
			return RestResponse.failure("用户角色至少选择一个");
		}
		User oldUser = userService.findUserById(user.getId());
		System.out.println(oldUser.toString()+"-------");
		if(StringUtils.isNotBlank(user.getEmail())) {
			if(!oldUser.getEmail().equals(user.getEmail())) {
				if(userService.userCount(user.getEmail()) > 0) {
					return RestResponse.failure("该邮箱已被使用");
				}
			}
		}
		if(StringUtils.isNotBlank(user.getLoginName())) {
			if(!oldUser.getLoginName().equals(user.getLoginName())) {
				if(userService.userCount(user.getLoginName()) > 0) {
					return RestResponse.failure("该登陆名已存在");
				}
			}
		}
		if(StringUtils.isNotBlank(user.getTel())) {
			if(!oldUser.getTel().equals(user.getTel())) {
				if(userService.userCount(user.getTel()) > 0) {
					return RestResponse.failure("该手机号已被绑定");
				}
			}
		}
		//更新用户信息
		userService.updateUser(user);
		//先解除用户和角色的关系
		userService.dropUserRolesByUserId(user.getId());
		if(user.getId() == null  || user.getId() == 0) {
			return RestResponse.failure("保存用户信息出错");
		}
		//保存用户和角色的关系
		userService.saveUserRoles(user.getId(),user.getRoleLists());
		return RestResponse.success();
	}
	
	/**
	 * 删除单条记录
	 * @param id
	 * @return
	 */
	@RequiresPermissions("sys:user:delete")
	@PostMapping("delete")
	@ResponseBody
	public RestResponse delete(@RequestParam(value="id",required=false)Long id) {
		if(null == id || id == 0) {
			return RestResponse.failure("id不能为空");
		}
		if(id == 1) {
			return RestResponse.failure("超级管理员不能删除");
		}
		User user = userService.findUserById(id);
		if(null == user) {
			return RestResponse.failure("用户不存在");
		}
		
		userService.deleteUser(user);
		return RestResponse.success();
	}
	
	@RequiresPermissions("sys:user:delete")
	@PostMapping("deleteSome")
	@ResponseBody
	public RestResponse deleteSome(@RequestBody List<User> users) {
		if(null == users || users.size() == 0) {
			return RestResponse.failure("请选择需要删除的用户");
		}
		for(User u : users) {
			if(u.getId() == 1) {
				return RestResponse.failure("超级管理员不能删除");
			}else {
				userService.deleteUser(u);
			}
		}
		return RestResponse.success();
	}
	
	/**
	 * 修改用户密码 首页
	 * @param oldPwd
	 * @param newPwd
	 * @param confirmPwd
	 * @return
	 */
	@RequiresPermissions("sys:user:changePassword")
	@PostMapping("changePassword")
	@ResponseBody
	public RestResponse changePassword(@RequestParam(value="oldPwd",required=false)String oldPwd,
							@RequestParam(value="newPwd",required=false)String newPwd,
							@RequestParam(value="confirmPwd",required=false)String confirmPwd) {
		
		if(StringUtils.isBlank(oldPwd)) {
			return RestResponse.failure("旧密码不能为空");
		}
		if(StringUtils.isBlank(newPwd)) {
			return RestResponse.failure("新密码不能为空");
		}
		if(StringUtils.isBlank(confirmPwd)) {
			return RestResponse.failure("确认密码不能为空");
		}
		if(!newPwd.equals(confirmPwd)) {
			return RestResponse.failure("两次密码不一致");
		}
		User user = userService.findUserById(MySysUser.id());
		
		//判断输入的旧密码是否正确
		try {
			//通过明文旧密码重新加密为密文
			String reOldPwd = PasswordUtil.encrypt(oldPwd,user.getLoginName());
			if(!reOldPwd.equals(user.getPassword())) { //判断旧密码是否正确
				return RestResponse.failure("旧密码错误");
			}
			//通过明文新密码密码为密文
			String reNewPwd = PasswordUtil.encrypt(newPwd,user.getLoginName());
			user.setPassword(reNewPwd);
			userService.updateUser(user);
		} catch (Exception e) {
			log.error("密码匹配失败",e);
			return RestResponse.failure("系统异常,稍后重试");
		}
		
		
		return RestResponse.success();
	}
	
	
	/**
	 * 
	 * @param user
	 * @return
	 */
	@PostMapping("saveUserInfo")
	@ResponseBody
	public RestResponse saveUserInfo(User user) {
		if(null == user.getId() || user.getId() == 0) {
			return RestResponse.failure("用户ID不能为空");
		}
		if(StringUtils.isBlank(user.getLoginName())) {
			return RestResponse.failure("登陆名不能为空");
		}
		
		User oldUser = userService.findUserById(user.getId());
		if(StringUtils.isNotBlank(user.getEmail())) {
			if(!user.getEmail().equals(oldUser.getEmail())) {
				if(userService.userCount(user.getEmail()) > 0) {
					return RestResponse.failure("该邮箱已经使用");
				}
			}
		}
		if(StringUtils.isNotBlank(user.getTel())) {
			if(!user.getTel().equals(oldUser.getTel())) {
				if(userService.userCount(user.getTel()) > 0) {
					return RestResponse.failure("该手机号已经使用");
				}
			}
		}
		
		userService.updateUser(user);
		return RestResponse.success();
	}
	
	/**
	 * 清理用户缓存
	 * @return
	 */
	@GetMapping("clearUserCache")
	@ResponseBody
	public RestResponse clearUserCache() {
		CacheUtils cacheUtils = new CacheUtils();
		cacheUtils.clearUserCache();
		return RestResponse.success("清理缓存成功").setCode(0);
	}
	
	
}
