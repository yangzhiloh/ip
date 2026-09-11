package esther.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import esther.exception.EstherException;
import esther.storage.Storage;
import esther.task.Task;
import esther.task.TaskList;
import esther.task.Todo;

/**
 * Tests shared command processing behavior.
 */
public class CommandProcessorTest {
    @TempDir
    private Path temporaryDirectory;

    @Test
    public void processTodo_addsTaskAndReturnsConfirmation()
            throws IOException, EstherException {
        CommandProcessor processor = newProcessor();

        CommandResult result = processor.process("todo read book");

        assertFalse(result.shouldExit());
        assertFalse(result.isError());
        assertTrue(result.messages().stream().anyMatch(
                message -> message.contains("I've added this task")));
    }

    @Test
    public void processInvalidCommand_returnsErrorMessage()
            throws IOException, EstherException {
        CommandResult result = newProcessor().process("todo");

        assertFalse(result.shouldExit());
        assertTrue(result.isError());
        assertTrue(result.messages().stream().anyMatch(
                message -> message.contains("want to add a task")));
    }

    @Test
    public void processBye_returnsExitResult()
            throws IOException, EstherException {
        CommandResult result = newProcessor().process("bye");

        assertTrue(result.shouldExit());
        assertEquals(1, result.messages().size());
    }

    @Test
    public void processTodo_savesTaskToStorage()
            throws IOException, EstherException {
        Storage storage = new Storage(
                temporaryDirectory.resolve("tasks.txt"));
        CommandProcessor processor = new CommandProcessor(
                storage, new TaskList());

        processor.process("todo read book");

        assertEquals(1, storage.load().size());
        assertEquals("[T][ ] read book", storage.load().get(0).toString());
    }

    @Test
    public void processSort_mixedStatuses_reordersTasksStably()
            throws IOException, EstherException {
        Task completedFirst = new Todo("submit form");
        completedFirst.markAsDone();
        Task incompleteFirst = new Todo("buy milk");
        Task completedSecond = new Todo("return book");
        completedSecond.markAsDone();
        Task incompleteSecond = new Todo("read notes");
        TaskList tasks = new TaskList(List.of(
                completedFirst,
                incompleteFirst,
                completedSecond,
                incompleteSecond));
        CommandProcessor processor = new CommandProcessor(
                new Storage(temporaryDirectory.resolve("tasks.txt")), tasks);

        CommandResult result = processor.process("sort");

        assertEquals("I've sorted your tasks by completion status:",
                result.messages().get(0));
        assertEquals(
                List.of(incompleteFirst, incompleteSecond,
                        completedFirst, completedSecond),
                tasks.asList());
    }

    @Test
    public void processSort_reorderedTasks_persistsOrder()
            throws IOException, EstherException {
        Task completedTask = new Todo("submit form");
        completedTask.markAsDone();
        Task incompleteTask = new Todo("buy milk");
        Storage storage = new Storage(
                temporaryDirectory.resolve("tasks.txt"));
        CommandProcessor processor = new CommandProcessor(
                storage, new TaskList(List.of(completedTask, incompleteTask)));

        processor.process("sort");

        TaskList loadedTasks = storage.load();
        assertEquals("[T][ ] buy milk", loadedTasks.get(0).toString());
        assertEquals("[T][X] submit form", loadedTasks.get(1).toString());
    }

    private CommandProcessor newProcessor() {
        Storage storage = new Storage(
                temporaryDirectory.resolve("tasks.txt"));
        return new CommandProcessor(storage, new TaskList());
    }
}
