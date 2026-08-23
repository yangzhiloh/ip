package esther;

import java.io.IOException;
import java.nio.file.Path;

import esther.exception.EstherException;
import esther.parser.Parser;
import esther.storage.Storage;
import esther.task.Task;
import esther.task.TaskList;
import esther.ui.Ui;

/**
 * Represents the Esther personal assistant chatbot.
 */
public class Esther {
    /**
     * Creates an Esther application.
     */
    public Esther() {
    }

    /**
     * Runs Esther's command-line interface.
     *
     * @param args Command-line arguments supplied to the application.
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        Storage storage = new Storage(Path.of("data", "esther.txt"));

        TaskList tasks;

        try {
            tasks = storage.load();
        } catch (IOException | EstherException exception) {
            ui.showLoadingError();
            tasks = new TaskList();
        }

        ui.showWelcome();

        while (true) {
            String command = ui.readCommand();

            try {
                if (command.equals("bye")) {
                    ui.showGoodbye();
                    break;
                } else if (command.equals("list")) {
                    ui.showTaskList(tasks);
                } else if (command.equals("mark") || command.startsWith("mark ")) {
                    int taskIndex = Parser.parseTaskIndex(
                            command, "mark", tasks.size());
                    Task task = tasks.get(taskIndex);

                    task.markAsDone();
                    storage.save(tasks);

                    ui.showTaskMarked(task);
                } else if (command.equals("unmark") || command.startsWith("unmark ")) {
                    int taskIndex = Parser.parseTaskIndex(
                            command, "unmark", tasks.size());
                    Task task = tasks.get(taskIndex);

                    task.markAsNotDone();
                    storage.save(tasks);

                    ui.showTaskUnmarked(task);
                } else if (command.equals("delete")
                        || command.startsWith("delete ")) {
                    int taskIndex = Parser.parseTaskIndex(
                            command, "delete", tasks.size());

                    Task removedTask = tasks.delete(taskIndex);
                    storage.save(tasks);

                    ui.showTaskDeleted(removedTask, tasks.size());

                } else {
                    Task task = Parser.parseTask(command);

                    tasks.add(task);
                    storage.save(tasks);

                    ui.showTaskAdded(task, tasks.size());
                }

            } catch (EstherException exception) {
                ui.showError(exception.getMessage());

            } catch (IOException exception) {
                ui.showSaveError();
            }
        }

        ui.close();
    }
}
