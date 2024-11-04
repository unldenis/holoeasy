package org.holoeasy.util.scheduler

import org.jetbrains.annotations.NotNull

/**
 * A non-extendable interface representing a scheduler for task scheduling and cancellation.
 */
interface MinecraftScheduler<Plugin, Location, World, Chunk, Entity> {

    /**
     * Schedules a task to be executed synchronously on the server's main thread.
     *
     * @param plugin The plugin associated with this task.
     * @param task   The task to be executed.
     */
    fun runTask(plugin: Plugin, task: Runnable): SchedulerTask

    /**
     * Schedules a task to be executed asynchronously on a separate thread.
     * This method is suitable for handling time-consuming tasks to avoid blocking the main thread.
     *
     * @param plugin The plugin associated with this task.
     * @param task   The task to be executed.
     */
    fun runAsyncTask(plugin: Plugin, task: Runnable): SchedulerTask

    /**
     * Creates a delayed task that will run the specified `task` after the given `delay`.
     *
     * @param plugin The plugin associated with this task.
     * @param task   The task to be executed after the delay.
     * @param delay  The delay before the task is executed.
     */
    fun createDelayedTask(plugin: Plugin, task: Runnable, delay: Long): SchedulerTask

    /**
     * Creates a repeating task that will run the specified `task` after an initial `delay`,
     * and then repeatedly execute with the given `period` between executions.
     *
     * @param plugin The plugin associated with this task.
     * @param task   The task to be executed repeatedly.
     * @param delay  The delay before the first execution.
     * @param period The time between successive executions.
     */
    fun createRepeatingTask(plugin: Plugin, task: Runnable, delay: Long, period: Long): SchedulerTask

    /**
     * Schedules an asynchronous delayed task that will run the specified `task` after the given `delay`.
     * The task will be executed on a separate thread, making it suitable for non-blocking operations.
     *
     * @param plugin The plugin associated with this task.
     * @param task   The task to be executed after the delay.
     * @param delay  The delay before the task is executed.
     */
    fun createAsyncDelayedTask(plugin: Plugin, task: Runnable, delay: Long): SchedulerTask

    /**
     * Schedules an asynchronous repeating task that will run the specified `task` after an initial `delay`,
     * and then repeatedly execute with the given `period` between executions.
     * The task will be executed on a separate thread, making it suitable for non-blocking operations.
     *
     * @param plugin The plugin associated with this task.
     * @param task   The task to be executed repeatedly.
     * @param delay  The delay before the first execution.
     * @param period The time between successive executions.
     */
    fun createAsyncRepeatingTask(plugin: Plugin, task: Runnable, delay: Long, period: Long): SchedulerTask

    /**
     * Creates a delayed task for a specific world and chunk.
     *
     * @param plugin The plugin that owns this task.
     * @param task   The runnable task to execute.
     * @param world  The world in which the chunk is located.
     * @param chunk  The chunk in which the task will be executed.
     * @param delay  The delay in ticks before the task is executed.
     * @return A SchedulerTask representing the created task.
     */
    fun createDelayedTaskForWorld(
        plugin: Plugin,
        task: Runnable,
        world: World,
        @NotNull chunk: Chunk,
        delay: Long
    ): SchedulerTask

    /**
     * Creates a delayed task for a specific location.
     *
     * @param plugin   The plugin that owns this task.
     * @param task     The runnable task to execute.
     * @param location The location at which the task will be executed.
     * @param delay    The delay in ticks before the task is executed.
     * @return A SchedulerTask representing the created task.
     */
    fun createDelayedForLocation(plugin: Plugin, task: Runnable, location: Location, delay: Long): SchedulerTask

    /**
     * Creates a delayed task for a specific entity.
     *
     * @param plugin  The plugin that owns this task.
     * @param task    The runnable task to execute.
     * @param retired Retire callback to run if the entity is retired before the run callback can be invoked, may be null.
     * @param entity  The entity in which the task will be executed.
     * @param delay   The delay in ticks before the task is executed.
     * @return A SchedulerTask representing the created task.
     */
    fun createDelayedForEntity(
        plugin: Plugin,
        task: Runnable,
        retired: Runnable?,
        entity: Entity,
        delay: Long
    ): SchedulerTask

    /**
     * Creates a task for a specific world and chunk.
     *
     * @param plugin The plugin that owns this task.
     * @param task   The runnable task to execute.
     * @param world  The world in which the chunk is located.
     * @param chunk  The chunk in which the task will be executed.
     * @return A SchedulerTask representing the created task.
     */
    fun createTaskForWorld(plugin: Plugin, task: Runnable, world: World, @NotNull chunk: Chunk): SchedulerTask

    /**
     * Creates a task for a specific location.
     *
     * @param plugin   The plugin that owns this task.
     * @param task     The runnable task to execute.
     * @param location The location at which the task will be executed.
     * @return A SchedulerTask representing the created task.
     */
    fun createTaskForLocation(plugin: Plugin, task: Runnable, location: Location): SchedulerTask

    /**
     * Creates a task for a specific entity.
     *
     * @param plugin  The plugin that owns this task.
     * @param task    The runnable task to execute.
     * @param retired Retire callback to run if the entity is retired before the run callback can be invoked, may be null.
     * @param entity  The entity in which the task will be executed.
     * @return A SchedulerTask representing the created task.
     */
    fun createTaskForEntity(plugin: Plugin, task: Runnable, retired: Runnable?, entity: Entity): SchedulerTask

    /**
     * Creates a repeating task for a specific world and chunk.
     *
     * @param plugin The plugin that owns this task.
     * @param task   The runnable task to execute.
     * @param world  The world in which the chunk is located.
     * @param chunk  The chunk in which the task will be executed.
     * @param delay  The initial delay in ticks before the first execution.
     * @param period The period in ticks between consecutive executions.
     * @return A SchedulerTask representing the created task.
     */
    fun createRepeatingTaskForWorld(
        plugin: Plugin,
        task: Runnable,
        world: World,
        @NotNull chunk: Chunk,
        delay: Long,
        period: Long
    ): SchedulerTask

    /**
     * Creates a repeating task for a specific location.
     *
     * @param plugin   The plugin that owns this task.
     * @param task     The runnable task to execute.
     * @param location The location at which the task will be executed.
     * @param delay    The initial delay in ticks before the first execution.
     * @param period   The period in ticks between consecutive executions.
     * @return A SchedulerTask representing the created task.
     */
    fun createRepeatingTaskForLocation(
        plugin: Plugin,
        task: Runnable,
        location: Location,
        delay: Long,
        period: Long
    ): SchedulerTask

    /**
     * Creates a repeating task for a specific entity.
     *
     * @param plugin  The plugin that owns this task.
     * @param task    The runnable task to execute.
     * @param retired Retire callback to run if the entity is retired before the run callback can be invoked, may be null.
     * @param entity  The entity in which the task will be executed.
     * @param delay   The initial delay in ticks before the first execution.
     * @param period  The period in ticks between consecutive executions.
     * @return A SchedulerTask representing the created task.
     */
    fun createRepeatingTaskForEntity(
        plugin: Plugin,
        task: Runnable,
        retired: Runnable?,
        entity: Entity,
        delay: Long,
        period: Long
    ): SchedulerTask

    /**
     * Cancels all tasks associated with the given `plugin`.
     *
     * @param plugin The plugin whose tasks should be canceled.
     */
    fun cancelTasks(plugin: Plugin)

    /**
     * Gets the scheduler
     *
     * @return The scheduler
     */
    fun getScheduler(): MinecraftScheduler<Plugin, Location, World, Chunk, Entity>
}
