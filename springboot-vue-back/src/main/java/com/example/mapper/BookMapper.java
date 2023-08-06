package com.example.mapper;
import org.apache.ibatis.annotations.Param;

import com.example.entity.Book;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author author
 * @since 2023-07-05
 */
@Mapper
public interface BookMapper extends BaseMapper<Book> {

    int updateCoverById(@Param("id") Integer id);
}
