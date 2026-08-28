package esther.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import esther.exception.EstherException;
import esther.storage.Storage;
import esther.task.TaskList;

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
        assertTrue(result.messages().stream().anyMatch(
                message -> message.contains("I've added this task")));
    }

    @Test
    public void processInvalidCommand_returnsErrorMessage()
            throws IOException, EstherException {
        CommandResult result = newProcessor().process("todo");

        assertFalse(result.shouldExit());
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

    private CommandProcessor newProcessor() {
        Storage storage = new Storage(
                temporaryDirectory.resolve("tasks.txt"));
        return new CommandProcessor(storage, new TaskList());
    }
}
