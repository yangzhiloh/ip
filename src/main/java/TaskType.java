/**
 * Represents the supported types of tasks.
 */
public enum TaskType {
    TODO("T"),
    DEADLINE("D"),
    EVENT("E");

    private final String symbol;

    TaskType(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Returns the symbol used to display this task type.
     *
     * @return Display symbol of this task type.
     */
    public String getSymbol() {
        return symbol;
    }
}