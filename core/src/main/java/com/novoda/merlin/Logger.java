package com.novoda.merlin;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides a mechanism for adding a variety
 * of {@link LogHandle} that when logging will log to all handles.
 */
public final class Logger {

    private static final List<LogHandle> HANDLES = new ArrayList<>();

    private Logger() {
        // Uses static methods.
    }

    /**
     * Adds a given {@link LogHandle} to the internal list of LogHandles.
     *
     * @param handle LogHandle to log to.
     */
    public static void attach(LogHandle handle) {
        HANDLES.add(handle);
    }

    /**
     * Removes a given {@link LogHandle} from the internal list of LogHandles.
     *
     * @param handle LogHandle to remove.
     */
    public static void detach(LogHandle handle) {
        HANDLES.remove(handle);
    }

    /**
     * Removes all {@link LogHandle} from the internal list of LogHandles.
     */
    public static void detachAll() {
        HANDLES.clear();
    }

    /**
     * Calls each internally stored {@link LogHandle#v(Object...)}
     *
     * @param message to pass to each {@link LogHandle}
     */
    public static void v(Object... message) {
        for (int i = 0; i < HANDLES.size(); i++) {
            LogHandle handle = HANDLES.get(i);
            handle.v(message);
        }
    }

    /**
     * Calls each internally stored {@link LogHandle#i(Object...)}
     *
     * @param message to pass to each {@link LogHandle}
     */
    public static void i(Object... message) {
        for (int i = 0; i < HANDLES.size(); i++) {
            LogHandle handle = HANDLES.get(i);
            handle.i(message);
        }
    }

    /**
     * Calls each internally stored {@link LogHandle#d(Object...)}
     *
     * @param message to pass to each {@link LogHandle}
     */
    public static void d(Object... message) {
        for (int i = 0; i < HANDLES.size(); i++) {
            LogHandle handle = HANDLES.get(i);
            handle.d(message);
        }
    }

    /**
     * Calls each internally stored {@link LogHandle#d(Throwable, Object...)}
     *
     * @param throwable to pass to each {@link LogHandle}
     * @param message   to pass to each {@link LogHandle}
     */
    public static void d(Throwable throwable, Object... message) {
        for (int i = 0; i < HANDLES.size(); i++) {
            LogHandle handle = HANDLES.get(i);
            handle.d(throwable, message);
        }
    }

    /**
     * Calls each internally stored {@link LogHandle#w(Object...)}
     *
     * @param message to pass to each {@link LogHandle}
     */
    public static void w(Object... message) {
        for (int i = 0; i < HANDLES.size(); i++) {
            LogHandle handle = HANDLES.get(i);
            handle.w(message);
        }
    }

    /**
     * Calls each internally stored {@link LogHandle#w(Throwable, Object...)}
     *
     * @param throwable to pass to each {@link LogHandle}
     * @param message   to pass to each {@link LogHandle}
     */
    public static void w(Throwable throwable, Object... message) {
        for (int i = 0; i < HANDLES.size(); i++) {
            LogHandle handle = HANDLES.get(i);
            handle.w(throwable, message);
        }
    }

    /**
     * Calls each internally stored {@link LogHandle#e(Object...)}
     *
     * @param message to pass to each {@link LogHandle}
     */
    public static void e(Object... message) {
        for (int i = 0; i < HANDLES.size(); i++) {
            LogHandle handle = HANDLES.get(i);
            handle.e(message);
        }
    }

    /**
     * Calls each internally stored {@link LogHandle#e(Throwable, Object...)}
     *
     * @param throwable to pass to each {@link LogHandle}
     * @param message   to pass to each {@link LogHandle}
     */
    public static void e(Throwable throwable, Object... message) {
        for (int i = 0; i < HANDLES.size(); i++) {
            LogHandle handle = HANDLES.get(i);
            handle.e(throwable, message);
        }
    }

    /**
     * This interface can be used to create new logging strategies
     * similar to {@link MerlinBackwardsCompatibleLog} which
     * can then be attached to {@link Logger}.
     */
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
