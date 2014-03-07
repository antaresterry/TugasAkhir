package com.skripsi.atma;

public interface Copyable<T> {
    T copy ();
    T createForCopy ();
    void copyTo (T dest);
}
