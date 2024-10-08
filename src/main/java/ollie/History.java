package ollie;

import java.util.ArrayList;

import ollie.command.Command;
import ollie.exception.OllieException;

/**
 * Represents a list of commands which are used to reverse the command previously done.
 */
public class History {
    private final ArrayList<Command> history = new ArrayList<>();


    /**
     * Adds command.
     */
    public void add(Command command) {
        history.add(command);
    }

    /**
     * Removes the latest added task in its ArrayList of Task and execute it.
     *
     * @throws OllieException If no task is left to be undone
     */
    public Response undo(TaskList tasks, Ui ui, Storage storage) throws OllieException {
        if (history.isEmpty()) {
            throw new OllieException("No task left to undo!");
        }

        int indexOfLastElement = history.size() - 1;
        Command command = history.get(indexOfLastElement);
        history.remove(indexOfLastElement);

        assert (command != null);
        // Input new history object so that changes undone wil not interfere with current history
        return command.execute(tasks, ui, storage, new History());
    }
}
