package cn.zilanxuan.modules.system.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

import cn.zilanxuan.modules.system.entity.Menu;
import cn.zilanxuan.modules.system.entity.vo.ShowMenu;

/**
 * 功能描述：菜单 服务接口
 * @author zilanxuan
 * @website http://www.zilanxuan.cn
 * @version 1.0
 * @date 2020年4月12日 上午11:03:22
 */
public interface MenuService extends IService<Menu>{

	List<ShowMenu> getShowMenuByUser(Long id);
	
	List<Menu> selectAllMenus(Map<String,Object> map);
	
	int getCountByName(String username);
	
	int getCountByPermission(String permission);
	
	void saveOrUpdateMenu(Menu menu);
	
	
}
