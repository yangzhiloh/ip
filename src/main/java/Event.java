/**
 * Represents a task occurring between two times.
 */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Creates an event with the specified description and time range.
     *
     * @param description Description of the event.
     * @param from Starting date or time of the event.
     * @param to Ending date or time of the event.
     */
    public Event(String description, String from, String to) {
        super(description, TaskType.EVENT);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return String.format(
                "%s (from: %s to: %s)",
                super.toString(),
                from,
                to
        );
    }
}
