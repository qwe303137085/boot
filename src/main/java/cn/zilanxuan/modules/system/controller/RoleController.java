package cn.zilanxuan.modules.system.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;

import cn.zilanxuan.base.BaseController;
import cn.zilanxuan.modules.system.entity.Menu;
import cn.zilanxuan.modules.system.entity.Role;
import cn.zilanxuan.modules.system.entity.User;
import cn.zilanxuan.util.LayerData;
import cn.zilanxuan.util.RestResponse;

/**
 * 角色控制层
 * @author zilanxuan
 * @website http://www.zilanxuan.cn
 * @version 1.0
 * @date 2020年4月16日 下午10:58:45
 */
@RestController
@RequestMapping("api/system/role")
public class RoleController extends BaseController{

	private Logger log = LoggerFactory.getLogger(RoleController.class);
	
	/**
	 * 获取角色列表
	 * @param page
	 * @param limit
	 * @param request
	 * @return
	 */
	@RequiresPermissions("sys:role:list")
	@PostMapping("/list")
	public LayerData<Role> list(@RequestParam(value = "page", defaultValue = "1")Integer page ,
								@RequestParam(value = "limit", defaultValue="10")Integer limit,
								ServletRequest request){
		Map map = WebUtils.getParametersStartingWith(request, "s_");
		LayerData<Role> roleLayerData = new LayerData<Role>();
		QueryWrapper<Role> roleEntityWrapper = new QueryWrapper<Role>();
		roleEntityWrapper.eq("del_flag", false);
		//查询条件
		if(!map.isEmpty()) {
			String keys = (String) map.get("key");
			if(StringUtils.isNotBlank(keys)) {
				roleEntityWrapper.like("name", keys);
			}
		}
		
		IPage<Role> p= roleService.page(new Page<Role>(page,limit),roleEntityWrapper);
		roleLayerData.setCount((int)p.getTotal());
		roleLayerData.setData(setUserToRole(p.getRecords()));
		for(Role r : p.getRecords()) {
			System.out.println("-----"+r.getId());
			System.out.println("-----"+r.getName());
			System.out.println("-----"+r.getCreateUser());
			System.out.println("-----");
			System.out.println("-----");
			System.out.println("-----");
		}
		return roleLayerData;
	}
	
	//根据创建者id或者更新者id设置用户
	private List<Role> setUserToRole(List<Role> roles) {
		for(Role r : roles) {
			if(r.getCreateId() != null && r.getCreateId() != 0) { //用户id,创建者id
				User u = userService.findUserById(r.getCreateId());
				if(StringUtils.isBlank(u.getNickName())) {
					u.setNickName(u.getLoginName());
				}
				r.setCreateUser(u);
			}
			
			if(r.getUpdateId() != null && r.getUpdateId() != 0) { //用户id,修改者id
				User u = userService.findUserById(r.getUpdateId());
				if(StringUtils.isBlank(u.getNickName())) {
					u.setNickName(u.getLoginName());
				}
				r.setUpdateUser(u);
			}
		}
		return roles;
	}
	
	/**
	 * 获取所有菜单
	 * @return
	 */
	@GetMapping("getAllMenusList")
	public RestResponse getAllMenusList() {
		Map<String,Object> map = Maps.newHashMap();
		map.put("parentId", null);
		map.put("isShow", false);
		List<Menu> menuList = menuService.selectAllMenus(map);
		return RestResponse.success().setData(menuList);
	}
	
	
	/**
	 * 新增用户角色
	 * @param role
	 * @return
	 */
	@RequiresPermissions("sys:role:add")
	@PostMapping("add")
	public RestResponse add(@RequestBody Role role) {
		if(StringUtils.isBlank(role.getName())) {
			return RestResponse.failure("角色名称不能为空");
		}
		if(roleService.getRoleNameCount(role.getName()) > 0) {
			return RestResponse.failure("角色名称已存在");
		}
		roleService.saveRole(role);
		return RestResponse.success();
	}
	
	
	
	
	
	
	
	/**
	 * 功能描述：根据角色获取对应权限
	 * @param id
	 * @return
	 */
	@GetMapping("getMenuByRole")
	public RestResponse getMenuByRole(Long id) {
		Role role = roleService.getRoleById(id);
		StringBuilder menuIds = new StringBuilder();
		if(null != role) {
			Set<Menu> menuSet = role.getMenuSet();
			if(null != menuSet && menuSet.size() > 0) {
				for(Menu m : menuSet) {
					menuIds.append(m.getId().toString()).append(",");
				}
			}
		}
		//查询所有菜单
		Map<String,Object> map = Maps.newHashMap();
		map.put("parentId", null);
		map.put("isShow", false);
		List<Menu> menuList = menuService.selectAllMenus(map);
		Map<String,Object> result = Maps.newHashMap();
		result.put("menuList", menuList);
		result.put("menuIds", menuIds);
		
		return RestResponse.success().setData(result);
		
	}
	
	
	/**
	 * 编辑用户角色
	 * @param role
	 * @return
	 */
	@RequiresPermissions("sys:role:edit")
	@PostMapping("edit")
	public RestResponse edit(@RequestBody Role role) {
		if(role.getId() == null || role.getId() == 0) {
			return RestResponse.failure("角色ID不能为空");
		}
		if(StringUtils.isBlank(role.getName())) {
			return RestResponse.failure("角色名称不能为空");
		}
		Role oldRole = roleService.getById(role.getId());
		if(!role.getName().equals(oldRole.getName())) {
			if(roleService.getRoleNameCount(role.getName()) > 0) {
				return RestResponse.failure("角色已存在");
			}
		}
		roleService.updateRole(role);
		return RestResponse.success();
	}
	
	/**
	 * 根据角色id删除角色信息 （单条记录删除）
	 * @param id
	 * @return
	 */
	@RequiresPermissions("sys:role:delete")
	@PostMapping("delete")
	public RestResponse delete(@RequestParam(value = "id",required = true) Long id) {
		if(null == id || id == 0) {
			return RestResponse.failure("角色ID不能为空");
		}
		Role role = roleService.getRoleById(id);
		roleService.deleteRole(role);
		return RestResponse.success();
	}
	
	/**
	 * 批量删除角色
	 * @param roles
	 * @return
	 */
	@RequiresPermissions("sys:role:delete")
	@PostMapping("deleteSome")
	public RestResponse deleteSome(@RequestBody List<Role> roles) {
		if(null == roles || roles.size() == 0) {
			return RestResponse.failure("请选择需要删除的角色");
		}
		for(Role r : roles) {
			roleService.deleteRole(r);
		}
		
		return RestResponse.success();
	}
	
}
