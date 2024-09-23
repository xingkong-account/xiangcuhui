package com.service;

import com.bean.Member;
import com.bean.PageResult;
import com.mapper.MembersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class MembersService {
    @Autowired
    private MembersMapper memberMapper;
    @Autowired
    JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")  // 从配置文件获取发件人地址
    private String fromEmail;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public boolean updatePassword(String email, String password){
        String encodedPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        // 查找用户并更新密码
        Optional<Member> memberOpt = memberMapper.findByEmail(email);
        if (memberOpt.isPresent()) {
            Member member = memberOpt.get();
            member.setPassword(encodedPassword);
            memberMapper.update(member);
            return true;
        }
        return false;
    }

    public boolean validateUsernameEmail(String username, String email) {
        // 查询用户是否存在
        Optional<Member> user = memberMapper.findByUsername(username);
        return user.map(value -> value.getEmail().equals(email)).orElse(false);
    }
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

    // 单个删除个人会员
    public int deleteIndividual(int id) {
        return memberMapper.deleteIndividual(id);
    }

    // 多选删除个人会员
    public void deleteMembersByIds(List<Long> memberIds) {
        memberMapper.deleteAllByIdIn(memberIds);
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
        return hashedPassword.equals(encodedPassword);
    }


    public boolean changePassword(String username, String oldPassword, String newPassword) {
        // 使用MD5加密旧密码进行验证
        String encodedOldPassword = DigestUtils.md5DigestAsHex(oldPassword.getBytes());
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

    // 团体会员部分
    public PageResult<Member> getAllTeamMembers(int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<Member> members = memberMapper.findAllTeams(offset, pageSize);
        int total = memberMapper.countAllTeams();
        return new PageResult<>(members, total, pageNum, pageSize);
    }

    public PageResult<Member> getAllUnCheckedTeamMembers(int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<Member> members = memberMapper.findAllUnCheckedTeams(offset, pageSize);
        int total = memberMapper.countAllUnCheckedTeams();
        return new PageResult<>(members, total, pageNum, pageSize);
    }

    public PageResult<Member> getAllCheckedTeamMembers(int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<Member> members = memberMapper.findAllCheckedTeams(offset, pageSize);
        int total = memberMapper.countAllCheckedTeams();
        return new PageResult<>(members, total, pageNum, pageSize);
    }

    // 多选删除团本会员
    public void deleteTeamMembersByIds(List<Long> memberIds) {
        memberMapper.deleteAllTeamByIdIn(memberIds);
    }

    // 单个删除团体会员
    public int deleteTeam(int id){
        return memberMapper.deleteGroup(id);
    }

    public List<Member> searchMembers(String query, String select) {
        return memberMapper.searchMembers(query, select);
    }

    public List<Member> searchTeamMembers(String query, String select) {
        return memberMapper.searchTeamMembers(query, select);
    }

    // 更新团体会员信息
    public int updateTeam(Member member){
        member.setUpdated_at(LocalDateTime.now());
        return memberMapper.updateGroup(member);
    }

    // 发送QQ邮件
    public Member sendEmail(Member member, String verificationCode) {
        String email = member.getEmail();
        if (Objects.isNull(email) || email.isEmpty()) {
            System.out.println("用户邮箱地址信息为空");
            return null;
        }
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(5); // 有效期5分钟
        String formattedExpirationTime = expirationTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("验证码");  // 邮件标题
        // 邮件内容
        message.setText("您的验证码是: " + verificationCode + "\n有效期至: " + formattedExpirationTime + "，请在有效期内输入。");

        try {
            javaMailSender.send(message);
        } catch (Exception e) {
            System.out.println("邮件发送失败: " + e.getMessage());
        }
        return member;
    }

    public String generateVerificationCode() {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000);
        return String.valueOf(code);
    }

    // 将验证码存储到Redis，5分钟有效期
    public void saveCodeToRedis(String email, String code) {
        String key = "verification_code:" + email;
        redisTemplate.opsForValue().set(key, code, 50, TimeUnit.MINUTES);
    }

    // 从Redis中获取验证码
    public String getCodeFromRedis(String email) {
        String key = "verification_code:" + email;
        return (String) redisTemplate.opsForValue().get(key);
    }

    // 校验验证码
    public boolean verifyCode(String email, String inputCode) {
        String storedCode = getCodeFromRedis(email);
        return storedCode != null && storedCode.equals(inputCode);
    }
}

