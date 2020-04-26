package cn.zilanxuan.modules.system.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;

import cn.zilanxuan.modules.system.entity.Menu;
import cn.zilanxuan.modules.system.entity.vo.ShowMenu;
import cn.zilanxuan.modules.system.mapper.MenuMapper;
import cn.zilanxuan.modules.system.service.MenuService;

@Service
@Transactional(readOnly = true ,rollbackFor = Exception.class)
public class MenuServiceImpl extends ServiceImpl<MenuMapper,Menu> implements MenuService{

	
	@Override
	public List<ShowMenu> getShowMenuByUser(Long id) {
		Map<String,Object> map = Maps.newHashMap();
		map.put("userId", id);
		map.put("parentId ", null);
		return baseMapper.selectShowMenuByUser(map);
	}
	
	@Override
	public List<Menu> selectAllMenus(Map<String,Object> map){
		return baseMapper.getMenus(map);
	}

	@Override
	public int getCountByName(String username) {
		QueryWrapper<Menu> wrapper = new  QueryWrapper<Menu>();
		wrapper.eq("del_flag", false);
		wrapper.eq("name",username);
		return baseMapper.selectCount(wrapper);
	}

	@Override
	public int getCountByPermission(String permission) {
		QueryWrapper<Menu> wrapper = new  QueryWrapper<Menu>();
		wrapper.eq("del_flag", false);
		wrapper.eq("permission",permission);
		return baseMapper.selectCount(wrapper);
	}

	@Transactional(readOnly = false ,rollbackFor = Exception.class)
	@Override
	public void saveOrUpdateMenu(Menu menu) {
		System.out.print("service------------"+menu.getName());
		menu.setIsShow(true);
		menu.insertOrUpdate();
	}

	

	
	
}
