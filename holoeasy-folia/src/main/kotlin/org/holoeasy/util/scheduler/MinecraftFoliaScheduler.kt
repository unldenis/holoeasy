package org.holoeasy.util.scheduler

import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import org.bukkit.Bukkit
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.plugin.Plugin
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.NotNull
import java.util.concurrent.TimeUnit

@ApiStatus.NonExtendable
class MinecraftFoliaScheduler : MinecraftScheduler<Plugin, Location, World, Chunk, Entity> {

    /**
     * Schedules a task to be executed synchronously on the server's main thread.
     *
     * @param plugin The plugin associated with this task.
     * @param task   The task to be executed.
     */
    override fun runTask(plugin: Plugin, task: Runnable): SchedulerTask {
        return FoliaSchedulerTask(Bukkit.getGlobalRegionScheduler().run(plugin) { task.run() })
    }

    /**
     * Schedules a task to be executed asynchronously on a separate thread.
     * This method is suitable for handling time-consuming tasks to avoid blocking the main thread.
     *
     * @param plugin The plugin associated with this task.
     * @param task   The task to be executed.
     */
    override fun runAsyncTask(plugin: Plugin, task: Runnable): SchedulerTask {
        return FoliaSchedulerTask(Bukkit.getAsyncScheduler().runNow(plugin) { task.run() })
    }

    /**
     * Creates a delayed task that will run the specified `task` after the given `delay`.
     *
     * @param plugin The plugin associated with this task.
     * @param task   The task to be executed after the delay.
     * @param delay  The delay before the task is executed.
     */
    override fun createDelayedTask(plugin: Plugin, task: Runnable, delay: Long): SchedulerTask {
        return FoliaSchedulerTask(Bukkit.getGlobalRegionScheduler().runDelayed(plugin, { task.run() }, delay))
    }

    /**
     * Creates a repeating task that will run the specified `task` after an initial `delay`,
     * and then repeatedly execute with the given `period` between executions.
     *
     * @param plugin The plugin associated with this task.
     * @param task   The task to be executed repeatedly.
     * @param delay  The delay before the first execution.
     * @param period The time between successive executions.
     */
    override fun createRepeatingTask(plugin: Plugin, task: Runnable, delay: Long, period: Long): SchedulerTask {
        return FoliaSchedulerTask(
            Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin, { task.run() }, delay, period)
        )
    }

    /**
     * Schedules an asynchronous delayed task that will run the specified `task` after the given `delay`.
     * The task will be executed on a separate thread, making it suitable for non-blocking operations.
     *
     * @param plugin The plugin associated with this task.
     * @param task   The task to be executed after the delay.
     * @param delay  The delay before the task is executed.
     */
    override fun createAsyncDelayedTask(plugin: Plugin, task: Runnable, delay: Long): SchedulerTask {
        return FoliaSchedulerTask(
            Bukkit.getAsyncScheduler().runDelayed(
                plugin, { task.run() },
                delay / 20,
                TimeUnit.SECONDS
            )
        )
    }

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
    override fun createAsyncRepeatingTask(plugin: Plugin, task: Runnable, delay: Long, period: Long): SchedulerTask {
        return FoliaSchedulerTask(
            Bukkit.getAsyncScheduler().runAtFixedRate(
                plugin, { task.run() },
                delay / 20,
                period / 20,
                TimeUnit.SECONDS
            )
        )
    }

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
    override fun createDelayedTaskForWorld(
        plugin: Plugin,
        task: Runnable,
        world: World,
        @NotNull chunk: Chunk,
        delay: Long
    ): SchedulerTask {
        return FoliaSchedulerTask(
            Bukkit.getRegionScheduler().runDelayed(
                plugin, world, chunk.x, chunk.z, { task.run() },
                delay
            )
        )
    }

    /**
     * Creates a delayed task for a specific location.
     *
     * @param plugin   The plugin that owns this task.
     * @param task     The runnable task to execute.
     * @param location The location at which the task will be executed.
     * @param delay    The delay in ticks before the task is executed.
     * @return A SchedulerTask representing the created task.
     */
    override fun createDelayedForLocation(
        plugin: Plugin,
        task: Runnable,
        location: Location,
        delay: Long
    ): SchedulerTask {
        return FoliaSchedulerTask(
            Bukkit.getRegionScheduler().runDelayed(
                plugin, location, { task.run() }, delay
            )
        )
    }

    /**
     * Creates a delayed task for a specific location.
     *
     * @param plugin  The plugin that owns this task.
     * @param task    The runnable task to execute.
     * @param retired Retire callback to run if the entity is retired before the run callback can be invoked, may be null.
     * @param entity  The entity in which the task will be executed.
     * @param delay   The delay in ticks before the task is executed.
     * @return A SchedulerTask representing the created task.
     */
    override fun createDelayedForEntity(
        plugin: Plugin,
        task: Runnable,
        retired: Runnable?,
        @NotNull entity: Entity,
        delay: Long
    ): SchedulerTask {
        return FoliaSchedulerTask(entity.scheduler.runDelayed(plugin, { task.run() }, retired, delay))
    }

    /**
     * Creates a task for a specific world and chunk.
     *
     * @param plugin The plugin that owns this task.
     * @param task   The runnable task to execute.
     * @param world  The world in which the chunk is located.
     * @param chunk  The chunk in which the task will be executed.
     * @return A SchedulerTask representing the created task.
     */
    override fun createTaskForWorld(
        plugin: Plugin,
        task: Runnable,
        world: World,
        @NotNull chunk: Chunk
    ): SchedulerTask {
        return FoliaSchedulerTask(
            Bukkit.getRegionScheduler().run(plugin, world, chunk.x, chunk.z) { task.run() })
    }

    /**
     * Creates a task for a specific location.
     *
     * @param plugin   The plugin that owns this task.
     * @param task     The runnable task to execute.
     * @param location The location at which the task will be executed.
     * @return A SchedulerTask representing the created task.
     */
    override fun createTaskForLocation(plugin: Plugin, task: Runnable, location: Location): SchedulerTask {
        return FoliaSchedulerTask(Bukkit.getRegionScheduler().run(plugin, location) { task.run() })
    }

    /**
     * Creates a task for a specific location.
     *
     * @param plugin  The plugin that owns this task.
     * @param task    The runnable task to execute.
     * @param retired Retire callback to run if the entity is retired before the run callback can be invoked, may be null.
     * @param entity  The entity in which the task will be executed.
     * @return A SchedulerTask representing the created task.
     */
    override fun createTaskForEntity(
        plugin: Plugin,
        task: Runnable,
        retired: Runnable?,
        @NotNull entity: Entity
    ): SchedulerTask {
        return FoliaSchedulerTask(entity.scheduler.run(plugin, { task.run() }, retired))
    }

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
    override fun createRepeatingTaskForWorld(
        plugin: Plugin,
        task: Runnable,
        world: World,
        @NotNull chunk: Chunk,
        delay: Long,
        period: Long
    ): SchedulerTask {
        return FoliaSchedulerTask(
            Bukkit.getRegionScheduler().runAtFixedRate(
                plugin, world, chunk.x, chunk.z,
                { task.run() },
                delay,
                period
            )
        )
    }

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
    override fun createRepeatingTaskForLocation(
        plugin: Plugin,
        task: Runnable,
        location: Location,
        delay: Long,
        period: Long
    ): SchedulerTask {
        return FoliaSchedulerTask(
            Bukkit.getRegionScheduler().runAtFixedRate(
                plugin, location, { task.run() }, delay, period
            )
        )
    }

    /**
     * Creates a repeating task for a specific location.
     *
     * @param plugin  The plugin that owns this task.
     * @param task    The runnable task to execute.
     * @param retired Retire callback to run if the entity is retired before the run callback can be invoked, may be null.
     * @param entity  The entity in which the task will be executed.
     * @param delay   The initial delay in ticks before the first execution.
     * @param period  The period in ticks between consecutive executions.
     * @return A SchedulerTask representing the created task.
     */
    override fun createRepeatingTaskForEntity(
        plugin: Plugin,
        task: Runnable,
        retired: Runnable?,
        @NotNull entity: Entity,
        delay: Long,
        period: Long
    ): SchedulerTask {
        return FoliaSchedulerTask(
            entity.scheduler.runAtFixedRate(
                plugin,
                { task.run() },
                retired,
                delay,
                period
            )
        )
    }

    /**
     * Cancels all tasks associated with the given `plugin`.
     *
     * @param plugin The plugin whose tasks should be canceled.
     */
    override fun cancelTasks(plugin: Plugin) {
        Bukkit.getGlobalRegionScheduler().cancelTasks(plugin)
    }

    /**
     * Gets the scheduler instance for this class.
     * Since this class is already a subclass of {@link MinecraftScheduler},
     * it returns the current instance as the scheduler.
     *
     * @return The scheduler instance for this class (i.e., this).
     */
    override fun getScheduler(): MinecraftScheduler<Plugin, Location, World, Chunk, Entity> {
        return this
    }

    class FoliaSchedulerTask(private val scheduledTask: ScheduledTask?) : SchedulerTask {

        override fun cancel() {
            scheduledTask?.cancel()
        }

        override fun isCancelled(): Boolean {
            if (scheduledTask == null) return true
            return scheduledTask.isCancelled
        }

        override fun getTaskId(): Int {
            return 0
        }

        override fun isRunning(): Boolean {
            return when (scheduledTask?.executionState) {
                ScheduledTask.ExecutionState.IDLE, ScheduledTask.ExecutionState.RUNNING -> true
                else -> false
            }
        }
    }
}