package ggs.srr.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity @Getter
public class Quotes {

    @Id @GeneratedValue
    private Long id;
    private String name;
    private String content;

    public Quotes(){
    }
    public Quotes(Long id, String name, String content) {
        this.id = id;
        this.name = name;
        this.content = content;
    }
}
