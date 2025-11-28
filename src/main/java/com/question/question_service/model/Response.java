package com.question.question_service.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Response {
    private String id;
    private String response;
}
