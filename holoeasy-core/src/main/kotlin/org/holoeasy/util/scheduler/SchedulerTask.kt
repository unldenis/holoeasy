package org.holoeasy.util.scheduler

/**
 * Represents a task scheduled by the scheduler.
 */
interface SchedulerTask {

    /**
     * Cancels the scheduled task.
     */
    fun cancel()

    /**
     * Checks if the task has been cancelled.
     *
     * @return `true` if the task is cancelled, `false` otherwise.
     */
    fun isCancelled(): Boolean

    /**
     * Gets the unique identifier for the task.
     *
     * @return The task ID.
     */
    fun getTaskId(): Int

    /**
     * Checks if the task is currently running.
     *
     * @return `true` if the task is running, `false` otherwise.
     */
    fun isRunning(): Boolean
}
