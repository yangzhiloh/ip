/**
 * Represents a task tracked by Esther.
 */
public class Task {
    protected String description;
    protected boolean isDone;
    protected final TaskType type;

    /**
     * Creates an incomplete task with the specified description.
     *
     * @param description Description of the task.
     * @param type Type of the task.
     */
    public Task(String description, TaskType type) {
        this.description = description;
        this.type = type;
        this.isDone = false;
    }

    /**
     * Marks this task as completed.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as incomplete.
     */
    public void markAsNotDone() {
        this.isDone = false;
    }

    public boolean isDone() {
        return this.isDone;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    /**
     * Returns this task in the format used by the data file.
     *
     * @return Serialized representation of this task.
     */
    public String toDataString() {
        return String.format("%s | %d | %s",
                type.getSymbol(), isDone ? 1 : 0, description);
    }

    @Override
    public String toString() {
        return String.format(
            "[%s][%s] %s",
            this.type.getSymbol(),
            getStatusIcon(),
            this.description
        );
    }
}
