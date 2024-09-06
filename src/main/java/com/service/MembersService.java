package com.service;

import com.bean.Member;
import com.bean.PageResult;
import com.mapper.MembersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MembersService {
    @Autowired
    private MembersMapper memberMapper;

    public int addPersonal(Member member){
        String encodedPassword = DigestUtils.md5DigestAsHex(member.getPassword().getBytes());
        member.setPassword(encodedPassword);
        member.setType("个人会员");
        try {
            return  memberMapper.add(member);
        } catch (Exception e) {
            return -1;
        }
    }
    public int add(Member member) {
        // 使用MD5加密密码
        String encodedPassword = DigestUtils.md5DigestAsHex(member.getPassword().getBytes());
        member.setPassword(encodedPassword);
        try {
            return memberMapper.add(member);
        } catch (Exception e) {
            return -1;
        }
    }

    public int updateIndividual(Member member){
        member.setUpdated_at(LocalDateTime.now());
        return memberMapper.updateIndividual(member);
    }

    public int changePwd(Member member){
        member.setUpdated_at(LocalDateTime.now());
        return memberMapper.changePwdByName(member);
    }
    public List<Member> listIndividuals() {
        return memberMapper.listIndividuals();
    }

    public Member findById(Integer id){
        return memberMapper.findById(id);
    }

    public int deleteIndividual(int id) {
        return memberMapper.deleteIndividual(id);
    }

    public Member selectByName(String name){
        return memberMapper.selectByName(name);
    }

    public List<Member> getMembersByType(String type){
        return memberMapper.getMembersByType(type);
    }

    public Member findByNameAndPassword(String name, String password){
        return memberMapper.findByNameAndPassword(name, password);
    }

    public List<Member> getMembersByStatus(String status) {
        return memberMapper.selectMembersByStatus(status);
    }

    public void approveMember(int id) {
        memberMapper.approveMember(id, "已审核", LocalDateTime.now());
    }

    public void rejectMember(int id){
        memberMapper.approveMember(id, "已拒绝", LocalDateTime.now());
    }

    public int addTeamMember(Member teamMember) {
        String encodedPassword = DigestUtils.md5DigestAsHex(teamMember.getPassword().getBytes());
        teamMember.setPassword(encodedPassword);
        return memberMapper.addTeamMember(teamMember);
    }

    public boolean validatePassword(String rawPassword, String encodedPassword) {
        // 对用户输入的密码进行MD5哈希
        String hashedPassword = DigestUtils.md5DigestAsHex(rawPassword.getBytes());
        // 比较哈希后的密码
        return hashedPassword.equals(encodedPassword);
    }


    public boolean changePassword(String username, String oldPassword, String newPassword) {
        // 使用MD5加密旧密码进行验证
        String encodedOldPassword = DigestUtils.md5DigestAsHex(oldPassword.getBytes());

        // 从数据库中获取用户信息
        Member member = memberMapper.selectByName(username);

        if (member != null && member.getPassword().equals(encodedOldPassword)) {
            // 如果旧密码正确，则更新密码
            String encodedNewPassword = DigestUtils.md5DigestAsHex(newPassword.getBytes());
            member.setPassword(encodedNewPassword);
            return memberMapper.updateIndividual(member) > 0;
        }
        return false;
    }

    public PageResult<Member> getAllMembers(int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<Member> members = memberMapper.findAll(offset, pageSize);
        int total = memberMapper.countAll();
        return new PageResult<>(members, total, pageNum, pageSize);
    }

    public PageResult<Member> getAllUnCheckedMembers(int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<Member> members = memberMapper.findAllUnChecked(offset, pageSize);
        int total = memberMapper.countAllUnChecked();
        return new PageResult<>(members, total, pageNum, pageSize);
    }

    public PageResult<Member> getAllCheckedMembers(int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<Member> members = memberMapper.findAllChecked(offset, pageSize);
        int total = memberMapper.countAllChecked();
        return new PageResult<>(members, total, pageNum, pageSize);
    }

    // 多选删除用户
    public void deleteMembersByIds(List<Long> memberIds) {
        memberMapper.deleteAllByIdIn(memberIds);
    }

    public List<Member> searchMembers(String query, String select) {
        return memberMapper.searchMembers(query, select);
    }
}

