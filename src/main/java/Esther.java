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

        Task[] tasks = new Task[100];
        int taskCount = 0;

        System.out.println(line);
        System.out.println("Heyyyy! I'm your favourite assistant Esther.");
        System.out.println("What can I do for you today?");
        System.out.println(line);

        while (true) {
            String command = scanner.nextLine();

            if (command.equals("bye")) {
                System.out.println(line);
                System.out.println("Byeee! Hope to see you soooon!");
                System.out.println(line);
                break;
            } else if (command.equals("list")) {
                int tasksLeft = 0;

                for (int i = 0; i < taskCount; i++) {
                    if (!tasks[i].isDone()) {
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

                for (int i = 0; i < taskCount; i++) {
                    System.out.println(String.format(
                        "%d.%s",
                        i + 1,
                        tasks[i].toString()
                    ));
                }

                System.out.println(line);
            } else if (command.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(command.substring(5).trim());
                int taskIndex = taskNumber - 1;
                Task task = tasks[taskIndex];

                task.markAsDone();

                System.out.println(line);
                System.out.println("Nice! I've marked this task as done:");
                System.out.println(" " + task);
                System.out.println(line);
            } else if (command.startsWith("unmark ")) {
                int taskNumber = Integer.parseInt(command.substring(7).trim());
                int taskIndex = taskNumber - 1;
                Task task = tasks[taskIndex];

                task.markAsNotDone();

                System.out.println(line);
                System.out.println("Okay! I've marked this task as not done.");
                System.out.println(" " + task);
                System.out.println(line);
            } else {
                Task task;

                if (command.startsWith("todo ")) {
                    String description = command.substring(5).trim();
                    task = new Todo(description);
                } else if (command.startsWith("deadline ")) {
                    int byIndex = command.indexOf(" /by ");
                    String description = command.substring(9, byIndex).trim();
                    String by = command.substring(byIndex + 5).trim();
                    task = new Deadline(description, by);
                } else if (command.startsWith("event ")) {
                    int fromIndex = command.indexOf(" /from ");
                    int toIndex = command.indexOf(" /to ");
                    String description = command.substring(6, fromIndex).trim();
                    String from = command.substring(fromIndex + 7, toIndex).trim();
                    String to = command.substring(toIndex + 5).trim();
                    task = new Event(description, from, to);
                } else {
                    task = new Task(command);
                }

                tasks[taskCount] = task;
                taskCount++;

                String taskWord = taskCount == 1 ? "task" : "tasks";

                System.out.println(line);
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + task);
                System.out.println("Now you have " + taskCount + " " + taskWord + " in the list.");
                System.out.println(line);
            }

        }

        scanner.close();

    }
}
