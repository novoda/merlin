package com.novoda.support;

import java.util.ArrayList;
import java.util.List;

public final class Logger {

    private static final List<LogHandle> HANDLES = new ArrayList<>();

    private Logger() {
    }

    public static void attach(LogHandle handle) {
        HANDLES.add(handle);
    }

    public static void detach(LogHandle handle) {
        HANDLES.remove(handle);
    }

    public static void detachAll() {
        HANDLES.clear();
    }

    public static void v(Object... message) {
        for (int i = 0; i < HANDLES.size(); i++) {
            LogHandle handle = HANDLES.get(i);
            handle.v(message);
        }
    }

    public static void i(Object... message) {
        for (int i = 0; i < HANDLES.size(); i++) {
            LogHandle handle = HANDLES.get(i);
            handle.i(message);
        }
    }

    public static void d(Object... message) {
        for (int i = 0; i < HANDLES.size(); i++) {
            LogHandle handle = HANDLES.get(i);
            handle.d(message);
        }
    }

    public static void d(Throwable throwable, Object... message) {
        for (int i = 0; i < HANDLES.size(); i++) {
            LogHandle handle = HANDLES.get(i);
            handle.d(throwable, message);
        }
    }

    public static void w(Object... message) {
        for (int i = 0; i < HANDLES.size(); i++) {
            LogHandle handle = HANDLES.get(i);
            handle.w(message);
        }
    }

    public static void w(Throwable throwable, Object... message) {
        for (int i = 0; i < HANDLES.size(); i++) {
            LogHandle handle = HANDLES.get(i);
            handle.w(throwable, message);
        }
    }

    public static void e(Object... message) {
        for (int i = 0; i < HANDLES.size(); i++) {
            LogHandle handle = HANDLES.get(i);
            handle.e(message);
        }
    }

    public static void e(Throwable throwable, Object... message) {
        for (int i = 0; i < HANDLES.size(); i++) {
            LogHandle handle = HANDLES.get(i);
            handle.e(throwable, message);
        }
    }

    public interface LogHandle {

        void v(Object... message);

        void i(Object... message);

        void d(Object... msg);

        void d(Throwable throwable, Object... message);

        void w(Object... message);

        void w(Throwable throwable, Object... message);

        void e(Object... message);

        void e(Throwable throwable, Object... message);
    }
}
