package ollie.command;

import ollie.History;
import ollie.Response;
import ollie.Storage;
import ollie.TaskList;
import ollie.Ui;
import ollie.exception.OllieException;

/**
 * Represents a command for undoing the previous command
 */
public class UndoCommand extends Command {

    /**
     * Executes the undoing of a task
     *
     * @param tasks   List of tasks.
     * @param ui      User interface controller.
     * @param storage Storage controller for file manipulation.
     */
    @Override
    public Response execute(TaskList tasks, Ui ui, Storage storage, History history) throws OllieException {
        Response response = history.undo(tasks, ui, storage);
        return response;
    }
}
