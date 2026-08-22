package esther.parser;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import esther.exception.EstherException;
import esther.task.Deadline;
import esther.task.Event;
import esther.task.Task;
import esther.task.Todo;

/**
 * Interprets user commands and extracts their arguments.
 */
public final class Parser {
    private Parser() {
    }

    /**
     * Creates a task from an add-task command.
     *
     * @param command Full command entered by the user.
     * @return Task described by the command.
     * @throws EstherException If the command or its arguments are invalid.
     */
    public static Task parseTask(String command) throws EstherException {
        if (command.equals("todo") || command.startsWith("todo ")) {
            return parseTodo(command);
        } else if (command.equals("deadline")
                || command.startsWith("deadline ")) {
            return parseDeadline(command);
        } else if (command.equals("event")
                || command.startsWith("event ")) {
            return parseEvent(command);
        }

        throw new EstherException("You're not speaking my language.");
    }

    /**
     * Extracts and validates a task number from a command.
     *
     * @param command Full command entered by the user.
     * @param commandWord Command word being processed.
     * @param taskCount Number of tasks currently stored.
     * @return Valid zero-based task index.
     * @throws EstherException If the task number is missing or invalid.
     */
    public static int parseTaskIndex(String command, String commandWord,
            int taskCount) throws EstherException {
        String numberText = command.substring(commandWord.length()).trim();

        if (numberText.isEmpty()) {
            throw new EstherException(
                    "STOP PLAYING! THERES NO NUMBER AT ALL!!");
        }

        int taskNumber;

        try {
            taskNumber = Integer.parseInt(numberText);
        } catch (NumberFormatException exception) {
            throw new EstherException(
                    "... NOT A WHOLE NUMBER, ARE YOU KIDDING ME!");
        }

        if (taskNumber < 1 || taskNumber > taskCount) {
            throw new EstherException(
                    "STOP PLAYING! THIS NUMBER DOES NOT EXIST!");
        }

        return taskNumber - 1;
    }

    /**
     * Extracts and validates the keyword from a find command.
     *
     * @param command Find command entered by the user.
     * @return Keyword to search for.
     * @throws EstherException If no keyword was provided.
     */
    public static String parseFindKeyword(String command)
            throws EstherException {
        String keyword = command.substring("find".length()).trim();

        if (keyword.isEmpty()) {
            throw new EstherException(
                    "Please provide a keyword to search for.");
        }

        return keyword;
    }

    /**
     * Creates a todo from its command.
     *
     * @param command Todo command entered by the user.
     * @return Todo described by the command.
     * @throws EstherException If the description is empty.
     */
    private static Todo parseTodo(String command) throws EstherException {
        String description = command.substring(4).trim();

        if (description.isEmpty()) {
            throw new EstherException(
                    "... You want to add a task but you're not "
                            + "giving me anything???");
        }

        return new Todo(description);
    }

    /**
     * Creates a deadline from its command.
     *
     * @param command Deadline command entered by the user.
     * @return Deadline described by the command.
     * @throws EstherException If the description or date is invalid.
     */
    private static Deadline parseDeadline(String command)
            throws EstherException {
        int byIndex = command.indexOf(" /by ");

        if (command.endsWith(" /by")) {
            throw new EstherException("HELLO I NEED A TIME OR DATE!");
        }

        if (byIndex == -1) {
            throw new EstherException(
                    "PLEASEEEEE use this format: deadline DESCRIPTION "
                            + "/by DATE_OR_TIME");
        }

        String description = command.substring(8, byIndex).trim();
        String byText = command.substring(byIndex + 5).trim();

        if (description.isEmpty()) {
            throw new EstherException(
                    "... You want to add a task but you're not "
                            + "giving me anything???");
        }

        if (byText.isEmpty()) {
            throw new EstherException("HELLO I NEED A TIME OR DATE!");
        }

        try {
            LocalDate by = LocalDate.parse(byText);
            return new Deadline(description, by);
        } catch (DateTimeParseException exception) {
            throw new EstherException(
                    "Use date format yyyy-MM-dd, e.g. 2026-08-30.");
        }
    }

    /**
     * Creates an event from its command.
     *
     * @param command Event command entered by the user.
     * @return Event described by the command.
     * @throws EstherException If the description, start, or end is empty.
     */
    private static Event parseEvent(String command) throws EstherException {
        String fromMarker = " /from";
        String toMarker = " /to";

        int fromIndex = command.indexOf(fromMarker);
        int toIndex = command.indexOf(toMarker);

        if (fromIndex == -1 || toIndex == -1 || toIndex <= fromIndex) {
            throw new EstherException(
                    "PLEASEEEEEEE use this format: event DESCRIPTION "
                            + "/from START /to END");
        }

        String description = command.substring(5, fromIndex).trim();
        String from = command.substring(
                fromIndex + fromMarker.length(), toIndex).trim();
        String to = command.substring(toIndex + toMarker.length()).trim();

        if (description.isEmpty()) {
            throw new EstherException(
                    "... You want to add a task but you're not "
                            + "giving me anything???");
        }

        if (from.isEmpty()) {
            throw new EstherException(
                    "HELLO I NEED A STARTING TIME OR DATE!");
        }

        if (to.isEmpty()) {
            throw new EstherException(
                    "HELLO I NEED AN ENDING TIME OR DATE!");
        }

        return new Event(description, from, to);
    }
}
