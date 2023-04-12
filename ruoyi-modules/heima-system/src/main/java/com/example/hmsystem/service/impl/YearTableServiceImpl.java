package com.example.hmsystem.service.impl;

import com.alibaba.dubbo.container.page.PageHandler;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.hmsystem.domain.YearTable;
import com.example.hmsystem.mapper.YearTableMapper;
import com.example.hmsystem.service.YearTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Classname YearTableServiceImpl
 * @Author: ZhiYu Ren
 * @Date: 2023年04月07日 15:27
 * @Description:
 */
@Service
public class YearTableServiceImpl implements YearTableService {
    private final YearTableMapper yearTableMapper;

    @Autowired
    public YearTableServiceImpl(YearTableMapper yearTableMapper) {
        this.yearTableMapper = yearTableMapper;
    }

    @Override
    public List<YearTable> getAll(YearTable yearTable) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.setEntity(yearTable);
        return yearTableMapper.selectList(queryWrapper);
    }

    @Override
    public int inset(YearTable yearTable) {
        return yearTableMapper.insert(yearTable);
    }

    @Override
    public int del(String yearId) {
        return yearTableMapper.deleteById(yearId);
    }

    @Override
    public int put(YearTable yearTable) {
        return yearTableMapper.updateById(yearTable);
    }


}
