package com.mateuszkmita.thesis.external.controller.dto.page;

public record PageDto<T>(
        Iterable<T> data,
        int page,
        int numberOfPages,
        long allDataSize
) {
}
