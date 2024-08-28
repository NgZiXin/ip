package ollie;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import ollie.task.*;
import ollie.exception.*;
public class Storage {
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public ArrayList<Task> load() throws OllieException {
        try {
            File f = new File(this.filePath); // create a File for the given file path
            Scanner s = new Scanner(f); // create a Scanner using the File as the source
            ArrayList<Task> output = new ArrayList<>();

            // Parse file
            try {
                while (s.hasNext()) {
                    String input = s.nextLine();
                    String[] splitString = input.split(" \\| ", 3); // Type | 1 | Content

                    Task task;
                    switch (splitString[0]) {
                        case "D" -> {
                            // Save as deadline
                            String[] details = splitString[2].split(" \\| ", 2);
                            task = new Deadline(details[0], LocalDate.parse(details[1], formatter));
                        }
                        case "E" -> {
                            // Save as event
                            String[] details = splitString[2].split(" \\| ", 3);
                            task = new Event(details[0], LocalDate.parse(details[1], formatter), LocalDate.parse(details[2], formatter));
                        }
                        case "T" ->
                            // Save as todo
                                task = new Todo(splitString[2]);
                        default -> throw new CorruptFileException(filePath);
                    }

                    // Check for mark
                    if (Integer.parseInt(splitString[1]) != 0) {
                        task.markAsDone();
                    }

                    output.add(task);
                }
            } catch (Exception e) {
                throw new CorruptFileException(filePath);
            }
            return output;
        } catch (FileNotFoundException | CorruptFileException e) {
            throw new OllieException(e.getMessage());
        }
    }

    public void save(ArrayList<Task> tasks) throws OllieException {
        try {
            FileWriter fw = new FileWriter(this.filePath);
            for (Task t : tasks) {
                fw.write(t.getFormattedString() + "\n");
            }
            fw.close();
        } catch (IOException e) {
            throw new OllieException(e.getMessage());
        }
    }
}
