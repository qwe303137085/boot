package cn.zilanxuan.modules.system.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import cn.zilanxuan.modules.system.entity.Menu;
import cn.zilanxuan.modules.system.entity.vo.ShowMenu;

public interface MenuMapper extends BaseMapper<Menu>{

	List<ShowMenu> selectShowMenuByUser(Map<String,Object> map);
	
	List<Menu> getMenus(Map<String,Object> map);
}
