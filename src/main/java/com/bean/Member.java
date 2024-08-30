package com.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    private Integer id;
    private String name;
    private String type; // "个人会员" or "团体会员"
    private String phone;
    private String image_url;
    private String description;
    private String website;
    private String password;
    private String status; // "待审核", "已审核", or "已拒绝"
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
