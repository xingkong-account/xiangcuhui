package com.project.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
    private List<T> data;    // 当前页数据
    private int total;       // 总记录数
    private int pageNum;     // 当前页码
    private int pageSize;    // 每页显示条数
}
