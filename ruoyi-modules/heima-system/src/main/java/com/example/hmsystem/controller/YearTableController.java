package com.example.hmsystem.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.hmsystem.domain.YearTable;
import com.example.hmsystem.service.YearTableService;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Classname YearTableController
 * @Author: ZhiYu Ren
 * @Date: 2023年04月07日 15:25
 * @Description:
 */
@RestController
@RequestMapping("/yearTabl")
public class YearTableController {

    private final YearTableService yearTableService;

    @Autowired
    public YearTableController(YearTableService yearTableService) {
        this.yearTableService = yearTableService;
    }


    /**
     * 查询所有年表
     * @return
     */
    @GetMapping(value = "/getAll")
    @ResponseBody
    public R getAll(YearTable yearTable) {
        List<YearTable> list = yearTableService.getAll(yearTable);
        return R.ok(list);
    }

    /**
     * 新增年度表
     */

    @PostMapping("/add")
    public R add(YearTable yearTable) {
        int count = yearTableService.inset(yearTable);
        return R.ok("影响条数为："+count);
    }
    /**
     * 修改年度表
     */

    @PostMapping("/put")
    public R put(YearTable yearTable) {
        int count = yearTableService.put(yearTable);
        return R.ok("影响条数为："+count);
    }
    /**
     * 删除年度表
     */

    @GetMapping("/del")
    public R del(String yearId) {
        int count = yearTableService.del(yearId);
        return R.ok("影响条数为："+count);
    }







}
