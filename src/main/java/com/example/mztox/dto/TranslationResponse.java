package com.example.mztox.dto;

import lombok.Data;

import java.util.List;

@Data
public class TranslationResponse {

    private String content;
    private int itemLen;
    private List<TranslationItem> items; //items는 TranslationItem 객체들의 리스트

}