package com.apm4all.tracy.extensions.annotations;

/**
 * Enumeration determining resolution of task ID from http request used in @RequestProfiling processing.
 *
 * @author Jakub Stas
 * @see RequestProfiling
 * @since 4.0.0
 */
public enum Resolution {

    /**
     * Constant that indicates the use of value passed in a header as a task ID.
     * <p>
     * <b>API note:</b> If the attribute {@link RequestProfiling#taskId()} specifies a string value, a header with this name will
     * be looked up and it's value will be used. If the attribute {@link RequestProfiling#taskId()} is not specified default
     * {@link com.apm4all.tracy.extensions.XTracyHeader#X_TRACY_TASK_ID} will be used for the lookup. If no such header exists a
     * random task ID will be generated by Tracy.
     *
     * @see RequestProfiling#taskId()
     */
    HEADER,

    /**
     * Constant that indicates the use of literal string value provided in taskId attribute of @RequestProfiling annotation
     * as a task ID.
     * <p>
     * <b>API note:</b> If the attribute {@link RequestProfiling#taskId()} is not specified a random task ID will be generated by Tracy.
     *
     * @see RequestProfiling#taskId()
     */
    STRING,

    /**
     * Constant that indicates the use of randomly generated task id by Tracy itself as a task ID.
     *
     * @see RequestProfiling#taskId()
     */
    RANDOM
}