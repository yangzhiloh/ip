package esther.command;

import java.util.List;

/**
 * Represents the result of processing one Esther command.
 *
 * @param messages Messages to display in order.
 * @param shouldExit Whether the application should exit after displaying the messages.
 */
public record CommandResult(List<String> messages, boolean shouldExit) {
    /**
     * Creates a command result with an immutable message list.
     *
     * @param messages Messages to display in order.
     * @param shouldExit Whether the application should exit.
     */
    public CommandResult {
        messages = List.copyOf(messages);
    }
}
