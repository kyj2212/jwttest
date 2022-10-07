package com.ll.exam.jwt.app.article.repository;

import com.ll.exam.jwt.app.article.entiry.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findAllByOrderByIdDesc();
}
