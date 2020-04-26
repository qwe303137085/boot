package cn.zilanxuan.modules.system.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.zilanxuan.modules.system.entity.Log;
import cn.zilanxuan.modules.system.mapper.LogMapper;
import cn.zilanxuan.modules.system.service.LogService;

@Service
@Transactional(readOnly=true,rollbackFor=Exception.class)
public class LogServiceImpl extends ServiceImpl<LogMapper,Log> implements LogService{

}
