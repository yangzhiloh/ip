package esther.storage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import esther.exception.EstherException;
import esther.task.Deadline;
import esther.task.Event;
import esther.task.Task;
import esther.task.TaskList;
import esther.task.Todo;

/**
 * Loads tasks from and saves tasks to a data file.
 */
public class Storage {
    private final Path filePath;

    /**
     * Creates storage that uses the specified data file.
     *
     * @param filePath Path of the data file.
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the data file.
     *
     * @return Tasks reconstructed from the data file.
     * @throws IOException If the data file cannot be read.
     * @throws EstherException If the data file contains invalid data.
     */
    public TaskList load() throws IOException, EstherException {
        TaskList tasks = new TaskList();

        if (!Files.exists(filePath)) {
            return tasks;
        }

        for (String taskData : Files.readAllLines(
                filePath, StandardCharsets.UTF_8)) {
            if (!taskData.isBlank()) {
                tasks.add(parseTaskData(taskData));
            }
        }

        return tasks;
    }

    /**
     * Saves all tasks to the data file.
     *
     * @param tasks Tasks to save.
     * @throws IOException If the tasks cannot be saved.
     */
    public void save(TaskList tasks) throws IOException {
        Path parentDirectory = filePath.getParent();
        if (parentDirectory != null) {
            Files.createDirectories(parentDirectory);
        }

        ArrayList<String> taskData = new ArrayList<>();
        for (Task task : tasks.asList()) {
            taskData.add(task.toDataString());
        }

        Files.write(filePath, taskData, StandardCharsets.UTF_8);
    }

    /**
     * Reconstructs one task from its stored representation.
     *
     * @param taskData Stored representation of one task.
     * @return Reconstructed task.
     * @throws EstherException If the stored representation is invalid.
     */
    private Task parseTaskData(String taskData) throws EstherException {
        String[] fields = taskData.split(" \\| ", -1);

        if (fields.length < 3 || fields[2].isBlank()) {
            throw new EstherException(
                    "The data file contains invalid task data.");
        }

        Task task;

        switch (fields[0]) {
            case "T":
                if (fields.length != 3) {
                    throw new EstherException(
                            "The data file contains an invalid todo.");
                }
                task = new Todo(fields[2]);
                break;
            case "D":
                if (fields.length != 4 || fields[3].isBlank()) {
                    throw new EstherException(
                            "The data file contains an invalid deadline.");
                }
                try {
                    LocalDate by = LocalDate.parse(fields[3]);
                    task = new Deadline(fields[2], by);
                } catch (DateTimeParseException exception) {
                    throw new EstherException(
                            "The data file contains an invalid deadline date.");
                }
                break;
            case "E":
                if (fields.length != 5
                        || fields[3].isBlank()
                        || fields[4].isBlank()) {
                    throw new EstherException(
                            "The data file contains an invalid event.");
                }
                task = new Event(fields[2], fields[3], fields[4]);
                break;
            default:
                throw new EstherException(
                        "The data file contains an unknown task type.");
        }

        if (fields[1].equals("1")) {
            task.markAsDone();
        } else if (!fields[1].equals("0")) {
            throw new EstherException(
                    "The data file contains an invalid task status.");
        }

        return task;
    }
}
