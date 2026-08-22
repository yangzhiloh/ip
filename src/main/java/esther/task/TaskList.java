package esther.task;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the tasks stored by Esther.
 */
public class TaskList {
    private final List<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list containing the specified tasks.
     *
     * @param tasks Initial tasks to store.
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Returns the task at the specified index.
     *
     * @param index Zero-based position of the task.
     * @return Task at the specified index.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Adds a task to the list.
     *
     * @param task Task to add.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Deletes and returns the task at the specified index.
     *
     * @param index Zero-based position of the task.
     * @return Deleted task.
     */
    public Task delete(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the number of stored tasks.
     *
     * @return Number of stored tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Counts tasks that have not been completed.
     *
     * @return Number of incomplete tasks.
     */
    public int countNotDone() {
        int tasksLeft = 0;

        for (Task task : tasks) {
            if (!task.isDone()) {
                tasksLeft++;
            }
        }

        return tasksLeft;
    }

    /**
     * Returns tasks whose descriptions contain the specified keyword.
     *
     * @param keyword Keyword to search for.
     * @return Matching tasks.
     */
    public List<Task> find(String keyword) {
        List<Task> matchingTasks = new ArrayList<>();

        for (Task task : tasks) {
            if (task.hasKeyword(keyword)) {
                matchingTasks.add(task);
            }
        }

        return List.copyOf(matchingTasks);
    }

    /**
     * Returns an unmodifiable copy of the stored tasks.
     *
     * @return Copy of the stored tasks.
     */
    public List<Task> asList() {
        return List.copyOf(tasks);
    }
}
