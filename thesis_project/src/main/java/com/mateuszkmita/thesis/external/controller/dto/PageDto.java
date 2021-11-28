package com.mateuszkmita.thesis.external.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

@Getter
@Setter
public class PageDto<T> {
    private Iterable<T> data;
    private int page;
    private int numberOfPages;
    private int allDataSize;
//
//    @Override
//    public Iterator<T> iterator() {
//        return data.iterator();
//    }
//
//    @Override
//    public void forEach(Consumer<? super T> action) {
//        data.forEach(action);
//    }
//
//    @Override
//    public Spliterator<T> spliterator() {
//        return data.spliterator();
//    }
}
