import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Represents the Esther personal assistant chatbot.
 */
public class Esther {
    private static final Path DATA_FILE_PATH =
            Path.of("data", "esther.txt");

    /**
     * Runs Esther's command-line interface.
     *
     * @param args Command-line arguments supplied to the application.
     */
    public static void main(String[] args) {
        String line = "___________________________________________________________";

        Scanner scanner = new Scanner(System.in);

        ArrayList<Task> tasks;

        try {
            tasks = loadTasks();
        } catch (IOException | EstherException exception) {
            System.out.println(line);
            System.out.println(
                    "I couldn't load your saved tasks. Starting empty.");
            System.out.println(line);
            tasks = new ArrayList<>();
        }

        System.out.println(line);
        System.out.println("Heyyyy! I'm your favourite assistant Esther.");
        System.out.println("What can I do for you today?");
        System.out.println(line);

        while (true) {
            String command = scanner.nextLine();

            try {
                if (command.equals("bye")) {
                    System.out.println(line);
                    System.out.println("Byeee! Hope to see you soooon!");
                    System.out.println(line);
                    break;
                } else if (command.equals("list")) {
                    int tasksLeft = 0;

                    for (int i = 0; i < tasks.size(); i++) {
                        if (!tasks.get(i).isDone()) {
                            tasksLeft++;
                        }
                    }
                    System.out.println(line);

                    if (tasksLeft == 0) {System.out.println(
                        "WOW! You have no tasks left, good work!"
                    );}

                    if (1 <= tasksLeft && tasksLeft <= 5) {
                        String taskWord = tasksLeft == 1 ? "task" : "tasks";
                        System.out.println(String.format(
                            "Only %d %s left! Should be a piece of cake for you!",
                            tasksLeft,
                            taskWord
                        ));
                    }

                    if (tasksLeft > 5) {
                        System.out.println(String.format(
                        "%d tasks left?? What have you been doing this whole time?? You better focus up!",
                        tasksLeft
                        ));
                    }

                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println(String.format(
                            "%d.%s",
                            i + 1,
                            tasks.get(i)
                        ));
                    }

                    System.out.println(line);
                } else if (command.equals("mark") || command.startsWith("mark ")) {
                    int taskIndex = parseTaskIndex(command, "mark", tasks.size());
                    Task task = tasks.get(taskIndex);

                    task.markAsDone();
                    saveTasks(tasks);

                    System.out.println(line);
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println(" " + task);
                    System.out.println(line);
                } else if (command.equals("unmark") || command.startsWith("unmark ")) {
                    int taskIndex = parseTaskIndex(command, "unmark", tasks.size());
                    Task task = tasks.get(taskIndex);

                    task.markAsNotDone();
                    saveTasks(tasks);

                    System.out.println(line);
                    System.out.println("Okay! I've marked this task as not done.");
                    System.out.println(" " + task);
                    System.out.println(line);
                } else if (command.equals("delete")
                        || command.startsWith("delete ")) {
                    int taskIndex = parseTaskIndex(
                            command, "delete", tasks.size());

                    Task removedTask = tasks.remove(taskIndex);
                    saveTasks(tasks);
                    String taskWord = tasks.size() == 1 ? "task" : "tasks";

                    System.out.println(line);
                    System.out.println("Noted. I've removed this task:");
                    System.out.println("  " + removedTask);
                    System.out.println(
                            "Now you have " + tasks.size() + " "
                                    + taskWord + " in the list.");
                    System.out.println(line);

                } else {
                    Task task;

                    if (command.equals("todo") || command.startsWith("todo ")) {
                        String description = command.substring(4).trim();

                        if (description.isEmpty()) {
                            throw new EstherException(
                            "... You want to add a task but you're not giving me anything???");
                        }

                        task = new Todo(description);
                    } else if (command.equals("deadline")
                            || command.startsWith("deadline ")) {
                        int byIndex = command.indexOf(" /by ");

                        if (command.endsWith(" /by")) {
                            throw new EstherException(
                                    "HELLO I NEED A TIME OR DATE!");
                        }

                        if (byIndex == -1) {
                            throw new EstherException(
                                    "PLEASEEEEE use this format: deadline DESCRIPTION /by DATE_OR_TIME");
                        }

                        String description = command.substring(8, byIndex).trim();
                        String byText = command.substring(byIndex + 5).trim();

                        if (description.isEmpty()) {
                            throw new EstherException(
                                    "... You want to add a task but you're not giving me anything???");
                        }

                        if (byText.isEmpty()) {
                            throw new EstherException(
                                    "HELLO I NEED A TIME OR DATE!");
                        }

                        try {
                            LocalDate by = LocalDate.parse(byText);
                            task = new Deadline(description, by);
                        } catch (DateTimeParseException exception) {
                            throw new EstherException(
                                    "Use date format yyyy-MM-dd, "
                                            + "e.g. 2026-08-30.");
                        }
                    } else if (command.equals("event")
                            || command.startsWith("event ")) {
                        String fromMarker = " /from";
                        String toMarker = " /to";

                        int fromIndex = command.indexOf(fromMarker);
                        int toIndex = command.indexOf(toMarker);

                        if (fromIndex == -1 || toIndex == -1 || toIndex <= fromIndex) {
                            throw new EstherException(
                                    "PLEASEEEEEEE use this format: event DESCRIPTION /from START /to END");
                        }

                        String description = command.substring(5, fromIndex).trim();
                        String from = command.substring(
                                fromIndex + fromMarker.length(), toIndex).trim();
                        String to = command.substring(
                                toIndex + toMarker.length()).trim();

                        if (description.isEmpty()) {
                            throw new EstherException(
                                    "... You want to add a task but you're not giving me anything???");
                        }

                        if (from.isEmpty()) {
                            throw new EstherException(
                                    "HELLO I NEED A STARTING TIME OR DATE!");
                        }

                        if (to.isEmpty()) {
                            throw new EstherException(
                                    "HELLO I NEED AN ENDING TIME OR DATE!");
                        }

                        task = new Event(description, from, to);
                    } else {
                        throw new EstherException(
                            "You're not speaking my language."
                        );
                    }

                    tasks.add(task);
                    saveTasks(tasks);

                    String taskWord = tasks.size() == 1 ? "task" : "tasks";

                    System.out.println(line);
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + task);
                    System.out.println("Now you have " + tasks.size() + " " + taskWord + " in the list.");
                    System.out.println(line);
                }

            } catch (EstherException exception) {
                System.out.println(line);
                System.out.println(exception.getMessage());
                System.out.println(line);

            } catch (IOException exception) {
                System.out.println(line);
                System.out.println(
                        "I couldn't save your tasks. Please try again.");
                System.out.println(line);
            }
        }

        scanner.close();

    }

    /**
     * Loads tasks from the data file.
     *
     * @return Tasks reconstructed from the data file.
     * @throws IOException If the data file cannot be read.
     * @throws EstherException If the data file contains invalid data.
     */
    private static ArrayList<Task> loadTasks()
            throws IOException, EstherException {
        ArrayList<Task> tasks = new ArrayList<>();

        if (!Files.exists(DATA_FILE_PATH)) {
            return tasks;
        }

        for (String taskData : Files.readAllLines(
                DATA_FILE_PATH, StandardCharsets.UTF_8)) {
            if (!taskData.isBlank()) {
                tasks.add(parseTaskData(taskData));
            }
        }

        return tasks;
    }

    /**
     * Reconstructs one task from its stored representation.
     *
     * @param taskData Stored representation of one task.
     * @return Reconstructed task.
     * @throws EstherException If the stored representation is invalid.
     */
    private static Task parseTaskData(String taskData)
            throws EstherException {
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

    /**
     * Saves all tasks to the data file.
     *
     * @param tasks Tasks to save.
     * @throws IOException If the tasks cannot be saved.
     */
    private static void saveTasks(ArrayList<Task> tasks)
            throws IOException {
        Path parentDirectory = DATA_FILE_PATH.getParent();
        if (parentDirectory != null) {
            Files.createDirectories(parentDirectory);
        }

        ArrayList<String> taskData = new ArrayList<>();
        for (Task task : tasks) {
            taskData.add(task.toDataString());
        }

        Files.write(DATA_FILE_PATH, taskData, StandardCharsets.UTF_8);
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
    private static int parseTaskIndex(String command, String commandWord,
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
}
