package com.smartbudget.converter;

public interface Converter<S, T> {

    T convert(S source);

}
