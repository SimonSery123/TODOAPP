import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class EnhancedTaskManagerGUI extends JFrame {
    private JList<String> taskList;
    private DefaultListModel<String> listModel;
    private JTextField taskInput;
    private JButton addButton;
    private JButton removeButton;
    private JButton completeButton;
    private JComboBox<String> priorityComboBox;
    private JTextArea taskDetails;
    private Map<String, Task> tasks;
    private JLabel statusLabel;
    private Timer timer;

    public EnhancedTaskManagerGUI() {
        tasks = new HashMap<>();
        setTitle("Enhanced Task Manager");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 240, 240));

        // Initialize components
        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskList.setBackground(new Color(149, 142, 142));
        taskList.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        taskInput = new JTextField(20);
        addButton = createStyledButton("Add Task", new Color(78, 225, 78));
        removeButton = createStyledButton("Remove Task", new Color(220, 100, 100));
        completeButton = createStyledButton("Mark Complete", new Color(100, 100, 220));
        priorityComboBox = new JComboBox<>(new String[]{"Low", "Medium", "High"});
        taskDetails = new JTextArea(5, 20);
        taskDetails.setEditable(false);
        taskDetails.setBackground(new Color(161, 158, 158));
        taskDetails.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        statusLabel = new JLabel("Welcome to Task Manager");
        statusLabel.setHorizontalAlignment(JLabel.CENTER);

        // Layout
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.add(new JLabel("Task:"));
        inputPanel.add(taskInput);
        inputPanel.add(new JLabel("Priority:"));
        inputPanel.add(priorityComboBox);
        inputPanel.add(addButton);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(removeButton);
        buttonPanel.add(completeButton);

        JPanel westPanel = new JPanel(new BorderLayout(5, 5));
        westPanel.add(new JScrollPane(taskList), BorderLayout.CENTER);
        westPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.add(new JLabel("Task Details:"), BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(taskDetails), BorderLayout.CENTER);

        add(inputPanel, BorderLayout.NORTH);
        add(westPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        // Event listeners
        addButton.addActionListener(e -> addTask());
        removeButton.addActionListener(e -> removeTask());
        completeButton.addActionListener(e -> markComplete());
        taskList.addListSelectionListener(e -> showTaskDetails());

        // Timer for status updates
        timer = new Timer(5000, e -> updateStatus());
        timer.start();

        setVisible(true);
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(5, 15, 5, 15));
        return button;
    }

    private void addTask() {
        String taskName = taskInput.getText().trim();
        if (!taskName.isEmpty() && !tasks.containsKey(taskName)) {
            String priority = (String) priorityComboBox.getSelectedItem();
            Task task = new Task(taskName, priority);
            tasks.put(taskName, task);
            listModel.addElement(taskName);
            taskInput.setText("");
            updateStatus("Task added: " + taskName);
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a unique task name.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void removeTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            String taskName = listModel.get(selectedIndex);
            listModel.remove(selectedIndex);
            tasks.remove(taskName);
            taskDetails.setText("");
            updateStatus("Task removed: " + taskName);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a task to remove.", "No Selection", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void markComplete() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            String taskName = listModel.get(selectedIndex);
            Task task = tasks.get(taskName);
            task.setCompleted(true);
            showTaskDetails();
            updateStatus("Task completed: " + taskName);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a task to mark as complete.", "No Selection", JOptionPane.INFORMATION_MESSAGE);
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

    private void updateStatus() {
        int totalTasks = tasks.size();
        long completedTasks = tasks.values().stream().filter(Task::isCompleted).count();
        statusLabel.setText(String.format("Total Tasks: %d | Completed: %d | Pending: %d",
                totalTasks, completedTasks, totalTasks - completedTasks));
    }

    private void updateStatus(String message) {
        statusLabel.setText(message);
        timer.restart();
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

        public boolean isCompleted() {
            return completed;
        }

        @Override
        public String toString() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return String.format("Task: %s\nPriority: %s\nStatus: %s\nCreated: %s",
                    name, priority, (completed ? "Completed" : "Pending"),
                    sdf.format(creationDate));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new EnhancedTaskManagerGUI();
        });
    }
}