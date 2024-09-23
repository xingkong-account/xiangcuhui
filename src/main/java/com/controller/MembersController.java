package com.controller;

import com.bean.Article;
import com.bean.Member;
import com.bean.PageResult;
import com.dto.ResetPasswordRequestDTO;
import com.service.FileService;
import com.service.MembersService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class MembersController{
    @Autowired
    private MembersService memberService;
    @Autowired
    private FileService fileService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // 注册
    @PostMapping("/add")
    public boolean register(@RequestBody Member member) {
        // 检查用户名是否已存在
        Member existingMember = memberService.selectByName(member.getName());
        if (existingMember != null) {
            return false;
        }
        try {
            int rowsAffected = memberService.add(member);
            return rowsAffected > 0;
        } catch (Exception e) {
            return false;
        }
    }

    // 验证用户名和邮箱是否匹配
    @PostMapping("/validate-username-email")
    public ResponseEntity<Map<String, Object>> validateUsernameEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String username = request.get("name");
        boolean isMatch = memberService.validateUsernameEmail(username, email);
        Map<String, Object> response = new HashMap<>();
        response.put("data", Map.of("isMatch", isMatch));
        return ResponseEntity.ok(response);
    }
    // 发送QQ邮箱验证码
    @PostMapping("/send-email-code")
    public ResponseEntity<String> sendEmailCode(@RequestBody Member member) {
        String email = member.getEmail();
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("邮箱不能为空");
        }
        String verificationCode = memberService.generateVerificationCode();
        memberService.saveCodeToRedis(email, verificationCode);
        // 发送邮件
        memberService.sendEmail(member, verificationCode);
        return ResponseEntity.ok("验证码已发送到您的邮箱，请注意查收。");
    }

    // 验证QQ邮箱验证码
    @PostMapping("/verify-email-code")
    public ResponseEntity<?> verifyEmailCode(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        System.out.println(email);
        String inputCode = payload.get("code");
        String storedCode = redisTemplate.opsForValue().get("verification_code:" + email);
        if (storedCode == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("验证码已过期或不存在");
        }
        if (storedCode.equals(inputCode)) {
            return ResponseEntity.ok(Collections.singletonMap("success", true));
        } else {
            return ResponseEntity.ok(Collections.singletonMap("success", false));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, Object>> resetPassword(@RequestBody ResetPasswordRequestDTO request) {
        Map<String, Object> response = new HashMap<>();
        boolean isUpdated = memberService.updatePassword(request.getEmail(), request.getPassword());
        if (isUpdated) {
            response.put("success", true);
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "密码重置失败，请稍后重试");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 多选删除个人会员
   @PostMapping("/delete-members")
   public ResponseEntity<String> deleteMembers(@RequestBody List<Long> memberIds) {
       try {
           memberService.deleteMembersByIds(memberIds);
           return ResponseEntity.ok("删除成功");
       } catch (Exception e) {
           e.printStackTrace();
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("删除失败");
       }
   }

   // 多选删除团体会员
   @PostMapping("/delete-teams")
   public ResponseEntity<String> deleteTeamMembers(@RequestBody List<Long> memberIds) {
       try {
           memberService.deleteTeamMembersByIds(memberIds);
           return ResponseEntity.ok("删除成功");
       } catch (Exception e) {
           e.printStackTrace();
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("删除失败");
       }
   }

    @GetMapping("/personal-list")
    public PageResult<Member> personalList(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize)
    {
        return memberService.getAllMembers(pageNum, pageSize);
    }

    // 添加个人会员
    @PostMapping("/add-personal")
    public int addPersonalMember(@RequestBody Member member) {
        // 设置会员类型为 '个人会员'
        member.setType("个人会员");
        return memberService.addPersonal(member);
    }

    @GetMapping("/personal/{id}")
    public Member findById(@PathVariable("id") Integer id) {
        return memberService.findById(id);
    }

    // 未审核的个人会员
    @GetMapping("/unchecked-individuals")
    public PageResult<Member> getAllMembers(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize)
    {
        return memberService.getAllUnCheckedMembers(pageNum, pageSize);
    }

    // 获取已审核的个人用户
    @GetMapping("/processed-members")
    public PageResult<Member> getAll1Members(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize)
    {
        return memberService.getAllCheckedMembers(pageNum, pageSize);
    }


    // 个人会员审核
    @PostMapping("/individual-members/{id}/approve")
    public ResponseEntity<String> approveMember(@PathVariable("id") int id) {
        try {
            memberService.approveMember(id);
            return ResponseEntity.ok("审核通过");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("审核失败");
        }
    }

    // 拒绝个人用户
    @PostMapping("/individual-members/{id}/reject")
    public ResponseEntity<String> rejectMember(@PathVariable("id") int id) {
        try {
            memberService.rejectMember(id);
            return ResponseEntity.ok("拒绝用户的会员申请");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("拒绝失败");
        }
    }

    @PostMapping("/team-members/{id}/approve")
    public ResponseEntity<String> approveTeamMember(@PathVariable("id") int id) {
        try {
            memberService.approveMember(id);
            return ResponseEntity.ok("审核通过");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("审核失败");
        }
    }

    // 拒绝团体用户
    @PostMapping("/team-members/{id}/reject")
    public ResponseEntity<String> rejectTeamMember(@PathVariable("id") int id) {
        try {
            memberService.rejectMember(id);
            return ResponseEntity.ok("拒绝用户的会员申请");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("拒绝失败");
        }
    }


    @PostMapping("/update-personal/{id}")
    public ResponseEntity<Void> updateIndividual(@PathVariable Integer id, @RequestBody Member member) {
        if (memberService.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        member.setId(id);
        memberService.updateIndividual(member);
        return ResponseEntity.noContent().build();
    }

    // 按条件查询个人用户
    @GetMapping("/members")
    public ResponseEntity<List<Member>> searchMembers(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "select", required = false) String select) {
        List<Member> members = memberService.searchMembers(query, select);
        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    // 按条件查询团体
    @GetMapping("/all-teams")
    public ResponseEntity<List<Member>> searchTeamMembers(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "select", required = false) String select) {
        List<Member> members = memberService.searchTeamMembers(query, select);
        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    // 单个删除个人会员
    @DeleteMapping("/delete-personal/{id}")
    public ResponseEntity<String> deletePersonal(@PathVariable("id") Integer id) {
        int result = memberService.deleteIndividual(id);
        if (result > 0) {
            return ResponseEntity.ok("删除成功");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("会员未找到");
        }
    }

    // 单个删除团体会员
    @DeleteMapping("/delete-single-team/{id}")
    public ResponseEntity<String> deleteSingleTeam(@PathVariable("id") Integer id) {
        int result = memberService.deleteTeam(id);
        if (result > 0) {
            return ResponseEntity.ok("删除成功");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("会员未找到");
        }
    }

    // 获取全部类型的团体会员数据
    @GetMapping("/team-members")
    public PageResult<Member> getTeamMembers(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize)
    {
        return memberService.getAllTeamMembers(pageNum, pageSize);
    }

    // 未审核的团体会员数据
    @GetMapping("/unchecked-teams")
    public PageResult<Member> getAllUncheckedMembers(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize)
    {
        return memberService.getAllUnCheckedTeamMembers(pageNum, pageSize);
    }

    // 已审核的团体会员数据
    @GetMapping("/checked-teams")
    public PageResult<Member> getAllCheckedMembers(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize)
    {
        return memberService.getAllCheckedTeamMembers(pageNum, pageSize);
    }

    @PostMapping("/update-team/{id}")
    public ResponseEntity<Void> updateTeam(@PathVariable Integer id, @RequestBody Member member) {
        if (memberService.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        member.setId(id);
        memberService.updateTeam(member);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = fileService.saveFile(file);
            Map<String, String> response = new HashMap<>();
            response.put("url", fileUrl);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/add-team")
    public ResponseEntity<String> addTeamMember(@RequestBody Member teamMember) {
        try {
            teamMember.setType("团体会员");
            int isAdded = memberService.addTeamMember(teamMember);

            if (isAdded > 0) {
                return ResponseEntity.ok("团队会员添加成功");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("团队会员添加失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("添加过程中出现错误");
        }
    }

    // 角色验证
    @GetMapping("/user/type")
    public ResponseEntity<Map<String, String>> getUserType(@RequestParam String username) {
        Member member = memberService.selectByName(username);
        if (member != null) {
            Map<String, String> response = new HashMap<>();
            response.put("type", member.getType());
            response.put("isAdmin", "管理员".equals(member.getType()) ? "true" : "false");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "User not found"));
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PasswordChangeRequest {
        private String oldPassword;
        private String newPassword;
        private String confirmPassword;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Member member, HttpSession session) {
        Member existingMember = memberService.selectByName(member.getName());
        if (existingMember == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "会员不存在"));
        }
        if (existingMember.getStatus().equals("待审核") || existingMember.getStatus().equals("已拒绝")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap("message", "会员未审核或已被拒绝，无法登录"));
        }
        boolean passwordMatches = memberService.validatePassword(member.getPassword(), existingMember.getPassword());
        if (passwordMatches) {
            session.setAttribute("username", existingMember.getName());
            session.setAttribute("usertype", existingMember.getType());
            session.setAttribute("userId", existingMember.getId());
//            System.out.println("类型：" + (String) session.getAttribute("usertype"));
            Map<String, Object> response = new HashMap<>();
            response.put("message", "登录成功");
            response.put("id", existingMember.getId());
            response.put("usertype", existingMember.getType()); // 返回用户类型

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("message", "用户名或密码错误"));
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<Map<String, Object>> changePassword(
            @RequestHeader("username") String username,
            @RequestBody PasswordChangeRequest request) {

        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("message", "用户未登录"));
        }
        Member existingMember = memberService.selectByName(username);
        if (existingMember == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "用户不存在"));
        }
        boolean oldPasswordMatches = memberService.validatePassword(request.getOldPassword(), existingMember.getPassword());
        if (!oldPasswordMatches) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("message", "旧密码错误"));
        }

        // 验证新密码和确认密码是否一致
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "新密码和确认密码不一致"));
        }
        Member memberToUpdate = new Member();
        memberToUpdate.setName(username);
        memberToUpdate.setPassword(DigestUtils.md5DigestAsHex(request.getNewPassword().getBytes())); // 使用更安全的密码加密方法

        boolean updated = memberService.changePwd(memberToUpdate) > 0;
        if (updated) {
            return ResponseEntity.ok(Collections.singletonMap("message", "密码修改成功"));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", "密码修改失败"));
        }
    }
}
