package com.ll.exam.jwt.app.article.entiry;

import com.ll.exam.jwt.app.base.entity.BaseEntity;
import com.ll.exam.jwt.app.member.entity.Member;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Article extends BaseEntity {
    @ManyToOne
    private Member author;
    private String subject;
    private String content;

    public Article(long id) {
        super(id);
    }
}