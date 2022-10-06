package com.ll.exam.jwt.app.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ll.exam.jwt.app.base.entity.BaseEntity;
import com.ll.exam.jwt.util.Util;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Member extends BaseEntity {
    @Column(unique = true)
    private String username;
    @JsonIgnore
    private String password;
    private String email;

    public Member(long id) {
        super(id);
    }

    // 현재 회원이 가지고 있는 권한들을 List<GrantedAuthority> 형태로 리턴
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("MEMBER"));

        return authorities;
    }
    public Map<String, Object> getClaims(){
        return Util.mapOf(
                "id",this.getId(),
                "CreateDate",this.getCreateDate(),
                "ModifyDate",this.getModifyDate(),
                "username",this.getUsername(),
                "email",this.getEmail(),
                "authorities",this.getAuthorities()
        );
    }
}