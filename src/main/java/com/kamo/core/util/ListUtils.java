package com.kamo.core.util;

import com.kamo.core.support.impl.AnnotationOrderComparator;

import java.util.*;
import java.util.function.Predicate;

public final class ListUtils {
    private ListUtils() {
    }

    public static <T> List<T> deduplication(List<T> oldList) {
        List<T> newList = new ArrayList<>(new HashSet<>(oldList));
        return newList;
    }

    public static <T> List<T> array2List(T[] array) {
        List<T> list = new ArrayList<>(Arrays.asList(array));
        return list;
    }

    public static <T> void sort(List<T> list, Comparator<? super T> c) {
        list.sort(c);
    }

    public static <T> void sort(List<T> list) {
        AnnotationOrderComparator.sort(list);
    }

    public static <T> T filterFist(List<T> list,Predicate<T> predicate) {
        for (T element : list) {
            if (predicate.test(element)) {
                return element;
            }
        }
        return null;
    }
    public static <T> List<T> filter(List<T> list,Predicate<T> predicate) {
        List<T> newList = new ArrayList<>();
        for (T element : list) {
            if (predicate.test(element)) {
                newList.add(element);
            }
        }
        return newList;
    }
}
