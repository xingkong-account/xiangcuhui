package com.controller;

import com.bean.Article;
import com.bean.Member;
import com.service.FileService;
import com.service.MembersService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/add")
    public boolean register(@RequestBody Member member) {
        // 检查用户名是否已存在
        Member existingMember = memberService.selectByName(member.getName());
        if (existingMember != null) {
            return false;
        }
        try {
            // 尝试插入新用户
            int rowsAffected = memberService.add(member);
            return rowsAffected > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @GetMapping("/personal-list")
    public List<Member> personalList() {
        return memberService.listIndividuals();
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

    @GetMapping("/individual-members")
    public List<Member> getMembersByStatus() {
        return memberService.getMembersByType("个人会员");
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

    // 拒绝用户
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

    // 拒绝用户
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
    public ResponseEntity<Void> updateArticle(@PathVariable Integer id, @RequestBody Member member) {
        if (memberService.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        member.setId(id);
        memberService.updateIndividual(member);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-personal/{id}")
    public ResponseEntity<String> deletePersonal(@PathVariable("id") Integer id) {
        int result = memberService.deleteIndividual(id);
        if (result > 0) {
            return ResponseEntity.ok("删除成功");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("会员未找到");
        }
    }

    @GetMapping("/team-members")
    public List<Member> getTeamMembers() {
        return memberService.getMembersByType("团体会员");
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

    @GetMapping("/user/role")
    public ResponseEntity<Boolean> getUserRole(@RequestParam String username) {
        System.out.println("Received request for user role: " + username);
        Member member = memberService.selectByName(username);
        if (member != null) {
            System.out.println("Member found: " + member);
            boolean isAdmin = "admin".equals(member.getName());
            return ResponseEntity.ok(isAdmin);
        } else {
            System.out.println("Member not found for username: " + username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
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
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap("message", "会员未审核或已拒绝，无法登录"));
        }

        boolean passwordMatches = memberService.validatePassword(member.getPassword(), existingMember.getPassword());
        if (passwordMatches) {
            session.setAttribute("username", existingMember.getName());
            session.setAttribute("userId", existingMember.getId());

            Map<String, Object> response = new HashMap<>();
            String username = (String) session.getAttribute("username");
            response.put("message", "登录成功");
            response.put("id", existingMember.getId());

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
