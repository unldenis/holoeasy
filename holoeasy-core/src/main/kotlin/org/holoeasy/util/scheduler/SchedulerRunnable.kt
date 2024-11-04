package org.holoeasy.util.scheduler

import org.jetbrains.annotations.NotNull

/**
 * An abstract class representing a runnable task that can be scheduled in the Minecraft scheduler.
 *
 * @param Plugin The type of the plugin.
 * @param Location The type of location.
 * @param World The type of world.
 * @param Chunk The type of chunk.
 * @param Entity The type of entity.
 */
abstract class SchedulerRunnable<Plugin, Location, World, Chunk, Entity>(
    private val minecraftScheduler: MinecraftScheduler<Plugin, Location, World, Chunk, Entity>
) : Runnable {

    private var task: SchedulerTask? = null

    /**
     * Returns true if this task has been cancelled.
     *
     * @return true if the task has been cancelled
     * @throws IllegalStateException if task was not scheduled yet
     */
    @Synchronized
    fun isCancelled(): Boolean {
        checkScheduled()
        return task!!.isCancelled()
    }

    /**
     * Attempts to cancel this task.
     *
     * @throws IllegalStateException if task was not scheduled yet
     */
    @Synchronized
    fun cancel() {
        checkScheduled()
        task?.cancel()
    }

    /**
     * Schedules this in the Bukkit scheduler to run on the next tick.
     *
     * @param plugin The Plugin associated with this task.
     * @return a Task that contains the id number
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalStateException if this was already scheduled
     */
    @Synchronized
    @NotNull
    fun runTask(plugin: Plugin): SchedulerTask {
        checkNotYetScheduled()
        return setupTask(minecraftScheduler.runTask(plugin, this))
    }

    /**
     * <b>Asynchronous tasks should never access any API in Bukkit. Great care
     * should be taken to assure the thread-safety of asynchronous tasks.</b>
     *
     * Schedules this in the Bukkit scheduler to run asynchronously.
     *
     * @param plugin The Plugin associated with this task.
     * @return a Task that contains the id number
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalStateException if this was already scheduled
     */
    @Synchronized
    @NotNull
    fun runTaskAsynchronously(@NotNull plugin: Plugin): SchedulerTask {
        checkNotYetScheduled()
        return setupTask(minecraftScheduler.runAsyncTask(plugin, this))
    }

    /**
     * Schedules this to run after the specified number of server ticks.
     *
     * @param plugin The Plugin associated with this task.
     * @param delay the ticks to wait before running the task
     * @return a Task that contains the id number
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalStateException if this was already scheduled
     */
    @Synchronized
    @NotNull
    fun runTaskLater(@NotNull plugin: Plugin, delay: Long): SchedulerTask {
        checkNotYetScheduled()
        return setupTask(minecraftScheduler.createDelayedTask(plugin, this, delay))
    }

    /**
     * <b>Asynchronous tasks should never access any API in Bukkit. Great care
     * should be taken to assure the thread-safety of asynchronous tasks.</b>
     *
     * Schedules this to run asynchronously after the specified number of
     * server ticks.
     *
     * @param plugin The Plugin associated with this task.
     * @param delay the ticks to wait before running the task
     * @return a Task that contains the id number
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalStateException if this was already scheduled
     */
    @Synchronized
    @NotNull
    fun runTaskLaterAsynchronously(@NotNull plugin: Plugin, delay: Long): SchedulerTask {
        checkNotYetScheduled()
        return setupTask(minecraftScheduler.createAsyncDelayedTask(plugin, this, delay))
    }

    /**
     * Schedules this to repeatedly run until cancelled, starting after the
     * specified number of server ticks.
     *
     * @param plugin The Plugin associated with this task.
     * @param delay the ticks to wait before running the task
     * @param period the ticks to wait between runs
     * @return a Task that contains the id number
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalStateException if this was already scheduled
     */
    @Synchronized
    @NotNull
    fun runTaskTimer(@NotNull plugin: Plugin, delay: Long, period: Long): SchedulerTask {
        checkNotYetScheduled()
        return setupTask(minecraftScheduler.createRepeatingTask(plugin, this, delay, period))
    }

    /**
     * <b>Asynchronous tasks should never access any API in Bukkit. Great care
     * should be taken to assure the thread-safety of asynchronous tasks.</b>
     *
     * Schedules this to repeatedly run asynchronously until cancelled,
     * starting after the specified number of server ticks.
     *
     * @param plugin The Plugin associated with this task.
     * @param delay the ticks to wait before running the task for the first time
     * @param period the ticks to wait between runs
     * @return a Task that contains the id number
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalStateException if this was already scheduled
     */
    @Synchronized
    @NotNull
    fun runTaskTimerAsynchronously(@NotNull plugin: Plugin, delay: Long, period: Long): SchedulerTask {
        checkNotYetScheduled()
        return setupTask(minecraftScheduler.createAsyncRepeatingTask(plugin, this, delay, period))
    }

    /**
     * Gets the task id for this runnable.
     *
     * @return the task id that this runnable was scheduled as
     * @throws IllegalStateException if task was not scheduled yet
     */
    @Synchronized
    fun getTaskId(): Int {
        checkScheduled()
        return task!!.getTaskId()
    }

    private fun checkScheduled() {
        if (task == null) {
            throw IllegalStateException("Not scheduled yet")
        }
    }

    private fun checkNotYetScheduled() {
        if (task != null) {
            throw IllegalStateException("Already scheduled as ${task!!.getTaskId()}")
        }
    }

    @NotNull
    private fun setupTask(task: SchedulerTask): SchedulerTask {
        this.task = task
        return task
    }
}
