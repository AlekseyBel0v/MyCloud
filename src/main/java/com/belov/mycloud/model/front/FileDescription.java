package com.belov.mycloud.model.front;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Модель для конвертации json в ответ на /list
 */
@Data
@RequiredArgsConstructor
public class FileDescription {
    private final String filename;
    private final long size;
}
