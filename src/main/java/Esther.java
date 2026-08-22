import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * Represents the Esther personal assistant chatbot.
 */
public class Esther {
    /**
     * Runs Esther's command-line interface.
     *
     * @param args Command-line arguments supplied to the application.
     */
    public static void main(String[] args) {
        String line = "___________________________________________________________";

        Scanner scanner = new Scanner(System.in);
        Storage storage = new Storage(Path.of("data", "esther.txt"));

        TaskList tasks;

        try {
            tasks = storage.load();
        } catch (IOException | EstherException exception) {
            System.out.println(line);
            System.out.println(
                    "I couldn't load your saved tasks. Starting empty.");
            System.out.println(line);
            tasks = new TaskList();
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
                    int tasksLeft = tasks.countNotDone();
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
                    int taskIndex = Parser.parseTaskIndex(
                            command, "mark", tasks.size());
                    Task task = tasks.get(taskIndex);

                    task.markAsDone();
                    storage.save(tasks);

                    System.out.println(line);
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println(" " + task);
                    System.out.println(line);
                } else if (command.equals("unmark") || command.startsWith("unmark ")) {
                    int taskIndex = Parser.parseTaskIndex(
                            command, "unmark", tasks.size());
                    Task task = tasks.get(taskIndex);

                    task.markAsNotDone();
                    storage.save(tasks);

                    System.out.println(line);
                    System.out.println("Okay! I've marked this task as not done.");
                    System.out.println(" " + task);
                    System.out.println(line);
                } else if (command.equals("delete")
                        || command.startsWith("delete ")) {
                    int taskIndex = Parser.parseTaskIndex(
                            command, "delete", tasks.size());

                    Task removedTask = tasks.delete(taskIndex);
                    storage.save(tasks);
                    String taskWord = tasks.size() == 1 ? "task" : "tasks";

                    System.out.println(line);
                    System.out.println("Noted. I've removed this task:");
                    System.out.println("  " + removedTask);
                    System.out.println(
                            "Now you have " + tasks.size() + " "
                                    + taskWord + " in the list.");
                    System.out.println(line);

                } else {
                    Task task = Parser.parseTask(command);

                    tasks.add(task);
                    storage.save(tasks);

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

}
