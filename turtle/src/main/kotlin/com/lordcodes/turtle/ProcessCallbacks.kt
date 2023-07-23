package com.lordcodes.turtle

/**
 * Callbacks into the underlying process, useful for monitoring or customising behaviour.
 */
interface ProcessCallbacks {
    /**
     * The process started, can be used to access the [Process] object.
     *
     * @param [process] The process that was started.
     */
    fun onProcessStart(process: Process) {}
}
