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

            try {
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
                } else if (command.equals("mark") || command.startsWith("mark ")) {
                    int taskIndex = parseTaskIndex(command, "mark", taskCount);
                    Task task = tasks[taskIndex];

                    task.markAsDone();

                    System.out.println(line);
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println(" " + task);
                    System.out.println(line);
                } else if (command.equals("unmark") || command.startsWith("unmark ")) {
                    int taskIndex = parseTaskIndex(command, "unmark", taskCount);
                    Task task = tasks[taskIndex];

                    task.markAsNotDone();

                    System.out.println(line);
                    System.out.println("Okay! I've marked this task as not done.");
                    System.out.println(" " + task);
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
                        String by = command.substring(byIndex + 5).trim();

                        if (description.isEmpty()) {
                            throw new EstherException(
                                    "... You want to add a task but you're not giving me anything???");
                        }

                        if (by.isEmpty()) {
                            throw new EstherException(
                                    "HELLO I NEED A TIME OR DATE!");
                        }

                        task = new Deadline(description, by);
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

                    tasks[taskCount] = task;
                    taskCount++;

                    String taskWord = taskCount == 1 ? "task" : "tasks";

                    System.out.println(line);
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + task);
                    System.out.println("Now you have " + taskCount + " " + taskWord + " in the list.");
                    System.out.println(line);
                }

            } catch (EstherException exception) {
                System.out.println(line);
                System.out.println(exception.getMessage());
                System.out.println(line);

            }
        }

        scanner.close();

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
                    "OOPS! Please provide a task number.");
        }

        int taskNumber;

        try {
            taskNumber = Integer.parseInt(numberText);
        } catch (NumberFormatException exception) {
            throw new EstherException(
                    "OOPS! The task number must be a whole number.");
        }

        if (taskNumber < 1 || taskNumber > taskCount) {
            throw new EstherException(
                    "OOPS! That task number does not exist.");
        }

        return taskNumber - 1;
    }
}
