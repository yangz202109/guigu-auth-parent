package com.study.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.domain.system.WebLog;
import com.study.service.WebLogService;
import com.study.mapper.WebLogMapper;
import org.springframework.stereotype.Service;

@Service
public class WebLogServiceImpl extends ServiceImpl<WebLogMapper, WebLog>
    implements WebLogService{

}




