package cn.zilanxuan.modules.system.controller;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Maps;

import cn.zilanxuan.base.BaseController;
import cn.zilanxuan.modules.system.entity.Menu;
import cn.zilanxuan.util.RestResponse;

@RequestMapping("/api/system/menu")
@RestController
public class MenuController extends BaseController{

	/**
	 * 获取全部菜单列表
	 * @return
	 */
	@RequestMapping("/treelist")
	public RestResponse treelist() {
		Map<String,Object> map = Maps.newHashMap();
		map.put("parentId",null);
		map.put("isShow",false);
		return RestResponse.success().setData(menuService.selectAllMenus(map));
	}
	
	/**
	 * 添加菜单，包括添加父级菜单和子菜单
	 * @return
	 */
	@RequiresPermissions("sys:menu:add")
	@RequestMapping("/add")
	public RestResponse add(Menu menu) {
		System.out.println("------add ----menu:"+menu.getName());
		if(StringUtils.isBlank(menu.getName())) {
			return RestResponse.failure("菜单名称不能为空");
		}
		if(menuService.getCountByName(menu.getName()) > 0) {
			return RestResponse.failure("菜单名称已存在");
		}
		if(StringUtils.isNotBlank(menu.getPermission())) {
			if(menuService.getCountByPermission(menu.getPermission()) > 0) {
				return RestResponse.failure("权限标识已存在");
			}
		}
		
		//处理菜单排序
		/*
		if(menu.getParentId() == null) {
			menu.setLevel(1L);
			QueryWrapper<Menu> wrapper = new QueryWrapper<Menu>();
			Object o = menuService.getObj(wrapper.select("max(sort)").isNull("parent_id"),null);
			System.out.println(o+"--------");
			
			int sort = 0;
			if(null != o) {
				sort = (Integer)o+10;
			}
			menu.setSort(sort);
		}else {
			Menu parentMenu = menuService.getById(menu.getParentId());
			if(null == parentMenu) {
				return RestResponse.failure("父菜单存在");
			}
			
			menu.setParentIds(parentMenu.getParentIds());
			menu.setLevel(parentMenu.getLevel()+1);

			QueryWrapper<Menu> wrapper = new QueryWrapper<Menu>();
			Object o = menuService.getObj(wrapper.select("max(sort)").eq("parent_id", menu.getParentId()), null);
			int sort = 0;
			if(null != o) {
				sort = (Integer)o+10;
			}
			menu.setSort(sort);
		}
		*/
		menuService.saveOrUpdateMenu(menu);
		return RestResponse.success();
	}
	
	/**
	 * 功能描述:编辑菜单
	 * @param menu
	 * @return
	 */
	@RequiresPermissions("sys:menu:edit")
	@RequestMapping("/edit")
	public RestResponse edit(Menu menu) {
		if(null == menu.getId()) {
			return RestResponse.failure("菜单id不能为空");
		}
		if(StringUtils.isBlank(menu.getName())) {
			return RestResponse.failure("菜单名称不能为空");
		}
		Menu oldMenu  = menuService.getById(menu.getId());
		if(!oldMenu.getName().equals(menu.getName())) {
			if(menuService.getCountByName(menu.getName()) > 0) {
				return RestResponse.failure("菜单名称已存在");
			}
		}
		if(StringUtils.isNotBlank(menu.getPermission())) {
			if(!oldMenu.getPermission().equals(menu.getPermission())) {
				if(menuService.getCountByPermission(menu.getPermission()) > 0) {
					return RestResponse.failure("权限标识已存在");
				}
			}
		} 
		
		if(null == menu.getSort()) {
			return RestResponse.failure("排序值不能为空");
		}
		menuService.saveOrUpdateMenu(menu);
		return RestResponse.success();
	}
	
	
	/**
	 * 功能描述:删除菜单
	 * @param id
	 * @return
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("sys:menu:delete")
	public RestResponse delete(@RequestParam(value="id", required=true) Long id) {
		if(null == id) {
			return RestResponse.failure("菜单id不能为空");
		}
		Menu menu  = menuService.getById(id);
		menu.setDelFlag(true);
		menuService.saveOrUpdateMenu(menu);
		return RestResponse.success();
	}
	
}
