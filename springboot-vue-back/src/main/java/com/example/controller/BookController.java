package com.example.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.utils.Result;
import com.example.entity.Book;
import com.example.mapper.BookMapper;
import com.example.service.IBookService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author author
 * @since 2023-07-05
 */
@RestController
@RequestMapping("/book")
public class BookController {

    @Resource
    private IBookService iBookService;

    @Resource
    private BookMapper bookMapper;
    

    //分页模糊查询
    @GetMapping("/findPage")
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search){

        LambdaQueryWrapper<Book> wrapper = Wrappers.<Book>lambdaQuery();
        if(StrUtil.isNotBlank(search)){
            wrapper.like(Book::getName, search);
        }

        Page<Book> userPage = bookMapper.selectPage(new Page<>(pageNum,pageSize),wrapper);

        return Result.success(userPage);
    }


    //新增
    @PostMapping("/save")
    public Result<?> save(@RequestBody Book book){
        if(book.getAuthor() == null){
            book.setAuthor("author");
        }
        iBookService.save(book);
        return Result.success();
    }

    //编辑
    @PutMapping("/updata")
    public Result<?> updata(@RequestBody Book book){
        iBookService.updateById(book);
        return Result.success();
    }

    //删除
    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id){
        iBookService.removeById(id);
        return Result.success();
    }

    //批量删除
    @PostMapping("/deleteBatch")
    public Result<?> deleteList(@RequestBody List<Integer> ids) {
        bookMapper.deleteBatchIds(ids);
        return Result.success();
    }

}
