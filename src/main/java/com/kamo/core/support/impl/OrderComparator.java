package com.kamo.core.support.impl;


import java.util.Comparator;

public abstract class OrderComparator implements Comparator<Object> {


    @Override
    public int compare(Object o1, Object o2) {
        if ( o1 instanceof Comparable){
            return ((Comparable) o1).compareTo(o2);
        }
        if ( o2 instanceof Comparable){
            return ((Comparable) o1).compareTo(o1);
        }
        return Integer.compare(getOrder(o1),getOrder(o2));
    }

  protected abstract int getOrder(Object object);
}
