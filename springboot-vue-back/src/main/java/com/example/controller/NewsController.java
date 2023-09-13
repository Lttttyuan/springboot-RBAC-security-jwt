package com.example.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.utils.Result;
import com.example.entity.News;
import com.example.mapper.NewsMapper;
import com.example.service.INewsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author author
 * @since 2023-07-05
 */
@RestController
@RequestMapping("/news")
public class NewsController {
    @Resource
    private INewsService iNewsService;

    @Resource
    private NewsMapper newsMapper;

    //分页模糊查询
    @GetMapping("/findPage")
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search){

        LambdaQueryWrapper<News> wrapper = Wrappers.<News>lambdaQuery();
        if(StrUtil.isNotBlank(search)){
            wrapper.like(News::getTitle, search);
        }

        Page<News> newsPage = newsMapper.selectPage(new Page<>(pageNum,pageSize),wrapper);

        return Result.success(newsPage);
    }

    //新增
    @PostMapping("/save")
    public Result<?> save(@RequestBody News news){
        if(news.getAuthor() == null){
            news.setAuthor("author");
        }
        LocalDateTime localDateTime = LocalDateTime.now();

        news.setTime(localDateTime);

        iNewsService.save(news);
        return Result.success();
    }

    //编辑
    @PutMapping("/updata")
    public Result<?> updata(@RequestBody News news){
        iNewsService.updateById(news);
        return Result.success();
    }

    //删除
    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id){
        iNewsService.removeById(id);
        return Result.success();
    }

}
