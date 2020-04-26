package cn.zilanxuan.modules.system.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.zilanxuan.base.BaseController;
import cn.zilanxuan.modules.system.entity.Log;
import cn.zilanxuan.modules.system.entity.User;
import cn.zilanxuan.util.LayerData;
import cn.zilanxuan.util.RestResponse;



/**
 * 日志管理
 * @author zilanxuan
 * @website http://www.zilanxuan.cn
 * @version 1.0
 * @date 2020年4月17日 下午8:46:10
 */
@RequestMapping(value = "api/system/log")
@RestController
public class LogController extends BaseController{

	private Logger log = LoggerFactory.getLogger(LogController.class);
	
	@RequiresPermissions("sys:log:list")
	@PostMapping("list")
	@ResponseBody
	public LayerData<Log> list(@RequestParam(value="page",defaultValue="1")Integer page,
			@RequestParam(value="limit",defaultValue="10")Integer limit,
			ServletRequest request){
		LayerData<Log> layer = new LayerData<Log>();
		Map map = WebUtils.getParametersStartingWith(request, "s_");
		QueryWrapper<Log> wrapper = new QueryWrapper<Log>();
		if(!map.isEmpty()) {
			String keys = (String)map.get("type");
			if(StringUtils.isNotBlank(keys)) {
				wrapper.eq("type", keys);
			}
			String title = (String)map.get("title");
			if(StringUtils.isNotBlank(title)) {
				wrapper.like("title", title);
			}
			String username = (String)map.get("username");
			if(StringUtils.isNotBlank(username)) {
				wrapper.eq("username", username);
			}
			String method = (String)map.get("method");
			if(StringUtils.isNotBlank(method)) {
				wrapper.eq("http_method", method);
			}
			
		}
		
		IPage<Log> p = logService.page(new Page<Log>(page,limit),wrapper);
		layer.setCount((int)p.getTotal());
		layer.setData(p.getRecords());
		
		
		return layer;
	}
	
	/**
	 * 日志删除
	 * @param ids
	 * @return
	 */
	@RequiresPermissions("sys:log:delete")
	@PostMapping("delete")
	@ResponseBody
	public RestResponse delete(@RequestParam("ids[]") List<Long> ids) {
		if(null == ids  || ids.size() == 0) {
			return RestResponse.failure("请选择要删除的记录");
		}
		logService.removeByIds(ids);
		return RestResponse.success();
	}
	
}
