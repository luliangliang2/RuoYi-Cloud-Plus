package com.example.hmsystem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.hmsystem.domain.YearTable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Name ：YearTableService
 * @Description ：
 * @Author ：ZhiYu Ren
 * @Date ：2023/4/7 15:26
 * @Version ：$
 * @History ：
 */
public interface YearTableService {
    //查询所有年表
    List<YearTable> getAll(YearTable yearTable);

    int inset(YearTable yearTable);

    int del(String yearId);

    int put(YearTable yearTable);

}
