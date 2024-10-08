package ollie.command;

import ollie.History;
import ollie.Response;
import ollie.Storage;
import ollie.TaskList;
import ollie.Ui;
import ollie.exception.OllieException;
import ollie.task.Task;

/**
 * Represents a command for removing a task from the task list.
 */
public class DeleteCommand extends Command {
    private int index;
    private Task taskToDelete;

    public DeleteCommand(int index) {
        this.index = index;
    }

    /**
     * Constructs a command object for deleting of task.
     */
    public DeleteCommand(Task taskToDelete) {
        assert (taskToDelete != null);
        this.taskToDelete = taskToDelete;
    }

    /**
     * Executes the deletion of task.
     *
     * @param tasks   List of tasks.
     * @param ui      User interface controller.
     * @param storage Storage controller for file manipulation.
     */
    @Override
    public Response execute(TaskList tasks, Ui ui, Storage storage, History history) throws OllieException {
        Task task;
        if (taskToDelete != null) {
            task = tasks.delete(taskToDelete);
        } else {
            task = tasks.delete(index);
        }
        assert (task != null);
        history.add(new AddCommand(task));
        return new Response(ui.getRemoveTaskMessage(task, tasks.getSize()), false);
    }
}

