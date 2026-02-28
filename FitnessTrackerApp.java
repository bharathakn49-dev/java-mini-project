import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

// ========= Interface =========
interface Trackable {
    void startSession();
    void endSession();
}

// ========= Abstract Class =========
abstract class Workout implements Trackable {
    protected String userName;
    protected int duration; // minutes
    protected Date timestamp = new Date();

    public Workout(String userName, int duration) {
        this.userName = userName;
        this.duration = duration;
    }

    public abstract double calculateCaloriesBurned();

    @Override
    public void startSession() {
        System.out.println(userName + " started a session at " + timestamp);
    }

    @Override
    public void endSession() {
        System.out.println(userName + " ended the session.");
    }

    public String getUserName() { return userName; }
    public int getDuration() { return duration; }

    @Override
    public String toString() {
        return String.format("%s | %s min | %.1f kcal | %s",
                getClass().getSimpleName(),
                duration,
                calculateCaloriesBurned(),
                timestamp.toString());
    }
}

// ========= Inheritance (1) =========
class CardioWorkout extends Workout {
    private int avgHeartRate;

    public CardioWorkout(String userName, int duration, int avgHeartRate) {
        super(userName, duration);
        this.avgHeartRate = avgHeartRate;
    }

    // ========= Polymorphism: override =========
    @Override
    public double calculateCaloriesBurned() {
        // very simple formula for demo
        return duration * (avgHeartRate / 10.0);
    }
}

// ========= Inheritance (2) =========
class StrengthWorkout extends Workout {
    private int sets, reps, weightKg;

    public StrengthWorkout(String userName, int duration, int sets, int reps, int weightKg) {
        super(userName, duration);
        this.sets = sets;
        this.reps = reps;
        this.weightKg = weightKg;
    }

    // ========= Polymorphism: override =========
    @Override
    public double calculateCaloriesBurned() {
        // simple demo formula
        return sets * reps * (weightKg / 15.0);
    }
}

// ========= Custom Exception =========
class InvalidWorkoutDataException extends Exception {
    public InvalidWorkoutDataException(String message) {
        super(message);
    }
}

// ========= Simple user profile =========
class UserProfile {
    private final String name;
    private final ArrayList<Workout> workouts = new ArrayList<>();

    public UserProfile(String name) { this.name = name; }
    public String getName() { return name; }
    public ArrayList<Workout> getWorkouts() { return workouts; }
    public void addWorkout(Workout w) { workouts.add(w); }
}

// ========= Multithreading: simulate concurrent sessions =========
class UserSession extends Thread {
    private final Workout workout;
    public UserSession(Workout w) { this.workout = w; }

    @Override
    public void run() {
        workout.startSession();
        try { Thread.sleep(800); } catch (InterruptedException ignored) {}
        workout.endSession();
    }
}

// ========= Swing GUI =========
public class FitnessTrackerApp extends JFrame {

    // ===== Collections: users and workouts =====
    private final ArrayList<UserProfile> users = new ArrayList<>();
    private final ArrayList<Workout> allWorkouts = new ArrayList<>();

    // UI fields
    private JTextField nameField, durationField, heartField, setsField, repsField, weightField;
    private JTextArea outputArea;
    private JComboBox<String> usersCombo;

    public FitnessTrackerApp() {
        setTitle("Fitness Tracker Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(720, 520);
        setLayout(new BorderLayout(6,6));

        // Top: form grid
        JPanel form = new JPanel(new GridLayout(6,2,6,6));
        form.add(new JLabel("User Name:"));
        nameField = new JTextField();
        form.add(nameField);

        form.add(new JLabel("Duration (min):"));
        durationField = new JTextField();
        form.add(durationField);

        form.add(new JLabel("Avg Heart Rate (Cardio):"));
        heartField = new JTextField();
        form.add(heartField);

        form.add(new JLabel("Sets (Strength):"));
        setsField = new JTextField();
        form.add(setsField);

        form.add(new JLabel("Reps (Strength):"));
        repsField = new JTextField();
        form.add(repsField);

        form.add(new JLabel("Weight (kg):"));
        weightField = new JTextField();
        form.add(weightField);

        add(form, BorderLayout.NORTH);

        // Center: buttons row
        JPanel buttons = new JPanel(new GridLayout(1,4,6,6));
        JButton addCardio = new JButton("Add Cardio Workout");
        JButton addStrength = new JButton("Add Strength Workout");
        JButton viewHistory = new JButton("View History");
        JButton report = new JButton("Generate Report");
        buttons.add(addCardio);
        buttons.add(addStrength);
        buttons.add(viewHistory);
        buttons.add(report);
        add(buttons, BorderLayout.CENTER);

        // Bottom: users dropdown + output
        JPanel bottom = new JPanel(new BorderLayout(6,6));
        usersCombo = new JComboBox<>();
        usersCombo.setPrototypeDisplayValue("Select / auto-added user");
        bottom.add(usersCombo, BorderLayout.NORTH);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        bottom.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        add(bottom, BorderLayout.SOUTH);

        // Actions
        addCardio.addActionListener(e -> {
            try { handleAddCardio(); }
            catch (InvalidWorkoutDataException ex) { showError(ex.getMessage()); }
        });

        addStrength.addActionListener(e -> {
            try { handleAddStrength(); }
            catch (InvalidWorkoutDataException ex) { showError(ex.getMessage()); }
        });

        viewHistory.addActionListener(e -> showHistoryDialog());
        report.addActionListener(e -> showReportDialog());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ===== Helpers =====
    private void handleAddCardio() throws InvalidWorkoutDataException {
        String user = getOrCreateUser();
        int duration = parsePositive(durationField.getText(), "Duration");
        int hr = parsePositive(heartField.getText(), "Avg Heart Rate");

        CardioWorkout w = new CardioWorkout(user, duration, hr);
        addWorkoutForUser(user, w);
        outputArea.append(user + " (Cardio): " + String.format("%.1f kcal\n", w.calculateCaloriesBurned()));
        new UserSession(w).start(); // multithreaded simulation
        clearOnlyNumbers();
    }

    private void handleAddStrength() throws InvalidWorkoutDataException {
        String user = getOrCreateUser();
        int duration = parsePositive(durationField.getText(), "Duration");
        int sets = parsePositive(setsField.getText(), "Sets");
        int reps = parsePositive(repsField.getText(), "Reps");
        int wt   = parsePositive(weightField.getText(), "Weight");

        StrengthWorkout w = new StrengthWorkout(user, duration, sets, reps, wt);
        addWorkoutForUser(user, w);
        outputArea.append(user + " (Strength): " + String.format("%.1f kcal\n", w.calculateCaloriesBurned()));
        new UserSession(w).start();
        clearOnlyNumbers();
    }

    private int parsePositive(String txt, String field) throws InvalidWorkoutDataException {
        try {
            int v = Integer.parseInt(txt.trim());
            if (v <= 0) throw new NumberFormatException();
            return v;
        } catch (NumberFormatException ex) {
            throw new InvalidWorkoutDataException(field + " must be a positive integer.");
        }
    }

    private String getOrCreateUser() throws InvalidWorkoutDataException {
        String name = nameField.getText().trim();
        if (name.isEmpty()) throw new InvalidWorkoutDataException("User Name is required.");

        // find existing
        for (UserProfile up : users) if (up.getName().equalsIgnoreCase(name)) {
            if (usersCombo.getSelectedItem() == null || !usersCombo.getSelectedItem().toString().equalsIgnoreCase(name)) {
                usersCombo.setSelectedItem(name);
            }
            return up.getName();
        }
        // create new
        UserProfile up = new UserProfile(name);
        users.add(up);
        usersCombo.addItem(name);
        usersCombo.setSelectedItem(name);
        return name;
    }

    private void addWorkoutForUser(String userName, Workout w) {
        // all workouts list (for global history)
        allWorkouts.add(w);
        // attach to user profile
        for (UserProfile up : users) {
            if (up.getName().equalsIgnoreCase(userName)) {
                up.addWorkout(w);
                return;
            }
        }
    }

    private void showHistoryDialog() {
        String selected = (String) usersCombo.getSelectedItem();
        JTextArea area = new JTextArea(15, 50);
        area.setEditable(false);
        StringBuilder sb = new StringBuilder();

        if (selected == null) {
            sb.append("All Workouts:\n");
            for (Workout w : allWorkouts) sb.append(" - ").append(w).append("\n");
        } else {
            for (UserProfile up : users) {
                if (up.getName().equalsIgnoreCase(selected)) {
                    sb.append("History for ").append(up.getName()).append(":\n");
                    for (Workout w : up.getWorkouts()) sb.append(" - ").append(w).append("\n");
                    break;
                }
            }
        }
        area.setText(sb.toString().isEmpty() ? "No workouts yet." : sb.toString());
        JOptionPane.showMessageDialog(this, new JScrollPane(area), "Workout History", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showReportDialog() {
        StringBuilder sb = new StringBuilder();
        if (users.isEmpty()) {
            sb.append("No users yet.");
        } else {
            for (UserProfile up : users) {
                double total = 0;
                int minutes = 0;
                for (Workout w : up.getWorkouts()) {
                    total += w.calculateCaloriesBurned();
                    minutes += w.getDuration();
                }
                sb.append(String.format("%s -> Sessions: %d | Minutes: %d | Total Calories: %.1f\n",
                        up.getName(), up.getWorkouts().size(), minutes, total));
            }
        }
        JTextArea area = new JTextArea(sb.toString(), 12, 50);
        area.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(area), "Report", JOptionPane.INFORMATION_MESSAGE);
    }

    private void clearOnlyNumbers() {
        durationField.setText("");
        heartField.setText("");
        setsField.setText("");
        repsField.setText("");
        weightField.setText("");
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Invalid Data", JOptionPane.ERROR_MESSAGE);
    }

    // ========= Entry point =========
    public static void main(String[] args) {
        SwingUtilities.invokeLater(FitnessTrackerApp::new);
    }
}
