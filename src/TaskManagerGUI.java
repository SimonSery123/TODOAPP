import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class TaskManagerGUI extends JFrame {
    private JList<String> taskList;
    private DefaultListModel<String> listModel;
    private JTextField taskInput;
    private JButton addButton;
    private JButton removeButton;
    private JButton completeButton;
    private JComboBox<String> priorityComboBox;
    private JTextArea taskDetails;
    private Map<String, Task> tasks;

    public TaskManagerGUI() {
        tasks = new HashMap<>();
        setTitle("Task Manager");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize components
        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);
        taskInput = new JTextField(20);
        addButton = new JButton("Add Task");
        removeButton = new JButton("Remove Task");
        completeButton = new JButton("Mark Complete");
        priorityComboBox = new JComboBox<>(new String[]{"Low", "Medium", "High"});
        taskDetails = new JTextArea(5, 20);
        taskDetails.setEditable(false);

        // Layout
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Task:"));
        inputPanel.add(taskInput);
        inputPanel.add(new JLabel("Priority:"));
        inputPanel.add(priorityComboBox);
        inputPanel.add(addButton);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(removeButton);
        buttonPanel.add(completeButton);

        JPanel westPanel = new JPanel(new BorderLayout());
        westPanel.add(new JScrollPane(taskList), BorderLayout.CENTER);
        westPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(inputPanel, BorderLayout.NORTH);
        add(westPanel, BorderLayout.WEST);
        add(new JScrollPane(taskDetails), BorderLayout.CENTER);

        // Event listeners
        addButton.addActionListener(e -> addTask());
        removeButton.addActionListener(e -> removeTask());
        completeButton.addActionListener(e -> markComplete());
        taskList.addListSelectionListener(e -> showTaskDetails());

        setVisible(true);
    }

    private void addTask() {
        String taskName = taskInput.getText().trim();
        if (!taskName.isEmpty() && !tasks.containsKey(taskName)) {
            String priority = (String) priorityComboBox.getSelectedItem();
            Task task = new Task(taskName, priority);
            tasks.put(taskName, task);
            listModel.addElement(taskName);
            taskInput.setText("");
        }
    }

    private void removeTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            String taskName = listModel.get(selectedIndex);
            listModel.remove(selectedIndex);
            tasks.remove(taskName);
            taskDetails.setText("");
        }
    }

    private void markComplete() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            String taskName = listModel.get(selectedIndex);
            Task task = tasks.get(taskName);
            task.setCompleted(true);
            showTaskDetails();
        }
    }

    private void showTaskDetails() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            String taskName = listModel.get(selectedIndex);
            Task task = tasks.get(taskName);
            taskDetails.setText(task.toString());
        }
    }

    private class Task {
        private String name;
        private String priority;
        private boolean completed;
        private Date creationDate;

        public Task(String name, String priority) {
            this.name = name;
            this.priority = priority;
            this.completed = false;
            this.creationDate = new Date();
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }

        @Override
        public String toString() {
            return "Task: " + name + "\n" +
                    "Priority: " + priority + "\n" +
                    "Status: " + (completed ? "Completed" : "Pending") + "\n" +
                    "Created: " + creationDate;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TaskManagerGUI::new);
    }
}