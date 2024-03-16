package com.belov.mycloud.model.front;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class Error {
    private final String message;
    private final Integer id;
}