package com.mapper;

import com.bean.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface MembersMapper {
    void deleteAllByIdIn(@Param("memberIds") List<Long> memberIds);
    List<Member> findAllUnChecked(@Param("offset") int offset, @Param("limit") int limit);
    List<Member> findAll(@Param("offset") int offset, @Param("limit") int limit);
    List<Member> findAllChecked(@Param("offset") int offset, @Param("limit") int limit);
    List<Member> searchMembers(@Param("query") String query, @Param("select") String select);

    int countAll();
    int countAllChecked();
    int countAllUnChecked();
    // 个人会员操作
    int add(Member member);

    // 根据 ID 查询个人会员
    Member findById(@Param("id") Integer id);

    // 查询所有个人会员
    List<Member> listIndividuals();

    // 根据名称查询个人会员
    Member selectByName(@Param("name") String name);

    // 根据名称和密码查询个人会员
    Member findByNameAndPassword(@Param("name") String name, @Param("password") String password);

    // 更新个人会员信息
    int updateIndividual(Member member);

    // 修改密码
    int changePwdByName(Member member);

    // 删除个人会员
    int deleteIndividual(@Param("id") Integer id);
    List<Member> selectMembersByStatus(@Param("status") String status);

    void approveMember(@Param("id") int id, @Param("status") String status, @Param("updated_at") LocalDateTime updated_at);

    // 团体会员操作
    // 根据 ID 查询团体会员
    Member findGroupById(@Param("id") Integer id);

    // 查询所有团体会员
    List<Member> listGroups();
    // 根据名称查询团体会员
    Member selectGroupByName(@Param("name") String name);

    // 更新团体会员信息
    int updateGroup(Member member);
    // 删除团体会员
    int deleteGroup(@Param("id") Integer id);

    List<Member> getMembersByType(@Param("type") String type);
    int addTeamMember(Member member);
}
