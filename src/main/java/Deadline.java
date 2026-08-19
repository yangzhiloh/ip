/**
 * Represents a task that must be completed by a deadline.
 */
public class Deadline extends Task {
    private final String by;

    /**
     * Creates a deadline with the specified description and due date or time.
     *
     * @param description Description of the deadline.
     * @param by Due date or time of the deadline.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return String.format(
                "[D]%s (by: %s)",
                super.toString(),
                by
        );
    }
}
