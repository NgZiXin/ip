public class Parser {
    public static Command parse(String input) throws OllieException{
        if (input.matches("list")) {
            return new ListCommand();
        } else if (input.matches("^mark.*")) {
            int index = Parser.getIndex(input);
            return new MarkCommand(index);
        } else if (input.matches("^unmark.*")) {
            int index = Parser.getIndex(input);
            return new UnmarkCommand(index);
        } else if (input.matches("^(deadline|event|todo).*")) {
            Task task = Parser.parseTask(input);
            return new AddCommand(task);
        } else if (input.matches("^delete.*")) {
            int index = Parser.getIndex(input);
            return new DeleteCommand(index);
        } else if (input.matches("bye")) {
            return new ExitCommand();
        } else {
            throw new OllieException("I'm sorry, but I don't know what that means :-(");
        }
    }

    private static int getIndex(String s) throws OllieException {
        if (!s.matches(".* \\d+")) {
            throw new OllieException("Missing Serial Number after command.");
        }
        return Integer.parseInt(s.replaceAll("\\D+", "")) - 1;
    }

    private static Task parseTask(String s) throws OllieException {
        Task task;

        // Input parser:
        if (s.matches("^deadline.*$")){
            if (!s.contains("/by")) {
                throw new OllieException("Use deadline with a \"/by\" keyword and a date/time.");
            }
            String[] splitString = s.split("/by", 2);

            String desc = splitString[0].replaceFirst("deadline","").trim();
            if (desc.isEmpty()) {
                throw new OllieException("Description of deadline cannot be empty!");
            }

            String by = splitString[1].trim();
            if (by.isEmpty()) {
                throw new OllieException("Date/Time of deadline cannot be empty!");
            }

            task = new Deadline(desc, by);
        } else if(s.matches("^event.*")) {
            if (!s.contains("/from")) {
                throw new OllieException("Use deadline with a \"/from\" keyword and a date/time.");
            }
            if (!s.contains("/to")) {
                throw new OllieException("Use deadline with a \"/to\" keyword and a date/time.");
            }
            if (!s.matches(".*/from.*/to.*")) {
                throw new OllieException("\"/from\" keyword must come before \"/to\" keyword.");
            }
            String[] splitString = s.split("/from|/to", 3);

            String desc = splitString[0].replaceFirst("event","").trim();
            if (desc.isEmpty()) {
                throw new OllieException("Description of event cannot be empty!");
            }

            String from = splitString[1].trim();
            if (from.isEmpty()) {
                throw new OllieException("Date/Time after /from cannot be empty!");
            }

            String to = splitString[2].trim();
            if (to.isEmpty()) {
                throw new OllieException("Date/Time after /to cannot be empty!");
            }

            task = new Event(desc, from, to);
        } else {
            String desc = s.replaceFirst("todo","").trim();
            if (desc.isEmpty()) {
                throw new OllieException("Description of todo cannot be empty!");
            }

            task = new Todo(desc);
        }
        return task;
    }
}
