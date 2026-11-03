import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class DSAQuest extends JFrame {

    private final Color DARK = new Color(8, 15, 31);
    private final Color DARK_PANEL = new Color(17, 29, 48);
    private final Color PANEL = new Color(24, 39, 62);
    private final Color PANEL_ALT = new Color(31, 49, 75);
    private final Color BLUE = new Color(59, 130, 246);
    private final Color BLUE_DARK = new Color(37, 94, 201);
    private final Color GREEN = new Color(34, 197, 94);
    private final Color GREEN_DARK = new Color(22, 163, 74);
    private final Color RED = new Color(239, 68, 68);
    private final Color WHITE = new Color(240, 244, 255);
    private final Color SOFT = new Color(166, 194, 255);

    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    private JComboBox<String> topicBox;
    private JComboBox<String> difficultyBox;

    private JLabel questionLabel;
    private JLabel progressLabel;
    private JLabel scoreLabel;
    private JLabel timeLabel;
    private JLabel accuracyLabel;
    private JLabel streakLabel;
    private JLabel feedbackLabel;

    private JRadioButton[] answerButtons;
    private ButtonGroup answerGroup;

    private JButton submitButton;
    private JButton nextButton;
    private JButton exitButton;

    private List<Question> quizQuestions;

    private Timer questionTimer;
    private int currentQuestion = 0;
    private int score = 0;
    private int correctAnswers = 0;
    private int answeredCount = 0;
    private int streak = 0;
    private int timeLeft = 30;

    static class Question {
        String topic;
        String difficulty;
        String question;
        String[] choices;
        int correctAnswer;

        Question(String topic,
                 String difficulty,
                 String question,
                 String[] choices,
                 int correctAnswer) {
            this.topic = topic;
            this.difficulty = difficulty;
            this.question = question;
            this.choices = choices;
            this.correctAnswer = correctAnswer;
        }
    }

    static class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
        }
    }

    static class SinglyLinkedList {
        private Node head;

        public void insertAtHead(int value) {
            Node newNode = new Node(value);
            newNode.next = head;
            head = newNode;
        }

        public void insertAtTail(int value) {
            Node newNode = new Node(value);
            if (head == null) {
                head = newNode;
                return;
            }

            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }

        public boolean delete(int value) {
            if (head == null) {
                return false;
            }

            if (head.data == value) {
                head = head.next;
                return true;
            }

            Node current = head;
            while (current.next != null && current.next.data != value) {
                current = current.next;
            }

            if (current.next == null) {
                return false;
            }

            current.next = current.next.next;
            return true;
        }

        public boolean contains(int value) {
            Node current = head;
            while (current != null) {
                if (current.data == value) {
                    return true;
                }
                current = current.next;
            }
            return false;
        }

        public int size() {
            int count = 0;
            Node current = head;
            while (current != null) {
                count++;
                current = current.next;
            }
            return count;
        }

        public void print() {
            Node current = head;
            if (current == null) {
                System.out.println("Linked List: empty");
                return;
            }

            StringBuilder sb = new StringBuilder("Linked List: ");
            while (current != null) {
                sb.append(current.data);
                if (current.next != null) {
                    sb.append(" -> ");
                }
                current = current.next;
            }
            System.out.println(sb);
        }
    }

    static class ArrayStack {
        private final int[] stack;
        private int top = -1;

        ArrayStack(int capacity) {
            this.stack = new int[capacity];
        }

        public boolean push(int value) {
            if (top == stack.length - 1) {
                return false;
            }
            stack[++top] = value;
            return true;
        }

        public Integer pop() {
            if (isEmpty()) {
                return null;
            }
            return stack[top--];
        }

        public Integer peek() {
            if (isEmpty()) {
                return null;
            }
            return stack[top];
        }

        public boolean isEmpty() {
            return top == -1;
        }

        public int size() {
            return top + 1;
        }

        public void print() {
            if (isEmpty()) {
                System.out.println("Stack: empty");
                return;
            }
            StringBuilder sb = new StringBuilder("Stack: ");
            for (int i = top; i >= 0; i--) {
                sb.append(stack[i]);
                if (i > 0) {
                    sb.append(" <- ");
                }
            }
            System.out.println(sb);
        }
    }

    static class CircularQueue {
        private final int[] queue;
        private int front = -1;
        private int rear = -1;
        private int size = 0;

        CircularQueue(int capacity) {
            this.queue = new int[capacity];
        }

        public boolean enqueue(int value) {
            if (isFull()) {
                return false;
            }
            if (isEmpty()) {
                front = 0;
            }
            rear = (rear + 1) % queue.length;
            queue[rear] = value;
            size++;
            return true;
        }

        public Integer dequeue() {
            if (isEmpty()) {
                return null;
            }

            int value = queue[front];
            if (front == rear) {
                front = -1;
                rear = -1;
            } else {
                front = (front + 1) % queue.length;
            }
            size--;
            return value;
        }

        public Integer peek() {
            if (isEmpty()) {
                return null;
            }
            return queue[front];
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public boolean isFull() {
            return size == queue.length;
        }

        public int size() {
            return size;
        }

        public void print() {
            if (isEmpty()) {
                System.out.println("Queue: empty");
                return;
            }
            StringBuilder sb = new StringBuilder("Queue: ");
            for (int i = 0; i < size; i++) {
                int index = (front + i) % queue.length;
                sb.append(queue[index]);
                if (i < size - 1) {
                    sb.append(" <- ");
                }
            }
            System.out.println(sb);
        }
    }

    private static class RoundedBorder extends AbstractBorder {
        private final Color color;
        private final int radius;

        RoundedBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(1.5f));
            g2.draw(new RoundRectangle2D.Double(x + 0.8, y + 0.8, width - 1.6, height - 1.6, radius, radius));
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(8, 10, 8, 10);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = 10;
            insets.right = 10;
            insets.top = 8;
            insets.bottom = 8;
            return insets;
        }
    }

    private static class RoundedPanel extends JPanel {
        private final Color panelColor;
        private final Color borderColor;
        private final int radius;

        RoundedPanel(Color panelColor, Color borderColor, int radius) {
            this.panelColor = panelColor;
            this.borderColor = borderColor;
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(panelColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, radius, radius);
            g2.dispose();
        }
    }

    private JPanel createGradientPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                GradientPaint background = new GradientPaint(
                    0, 0, new Color(6, 12, 24),
                    getWidth(), getHeight(), new Color(17, 30, 50)
                );
                g2.setPaint(background);
                g2.fillRect(0, 0, getWidth(), getHeight());

                int glowX = getWidth() / 2;
                int glowY = getHeight() / 3;
                RadialGradientPaint glow = new RadialGradientPaint(
                    glowX, glowY, Math.max(getWidth(), getHeight()) * 0.75f,
                    new float[]{0f, 0.5f, 1f},
                    new Color[]{new Color(59, 130, 246, 80), new Color(43, 67, 120, 40), new Color(6, 12, 24, 0)}
                );
                g2.setPaint(glow);
                g2.fillRect(0, 0, getWidth(), getHeight());

                g2.setColor(new Color(255, 255, 255, 18));
                for (int x = 0; x < getWidth(); x += 48) {
                    g2.drawLine(x, 0, x, getHeight());
                }
                for (int y = 0; y < getHeight(); y += 48) {
                    g2.drawLine(0, y, getWidth(), y);
                }

                g2.setColor(new Color(118, 188, 255, 50));
                g2.fillOval(getWidth() - 180, 60, 120, 120);
                g2.fillOval(80, getHeight() - 200, 140, 140);
                g2.dispose();
            }
        };
        panel.setOpaque(true);
        return panel;
    }

    public DSAQuest() {
        setTitle("DSA Quest - The Algorithm Challenge");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(DARK);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createHomePanel(), "HOME");
        mainPanel.add(createQuizPanel(), "QUIZ");
        mainPanel.add(createResultPanel(), "RESULT");

        add(mainPanel);
        cardLayout.show(mainPanel, "HOME");
    }

    private JPanel createHomePanel() {
        JPanel panel = createGradientPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        topBar.setOpaque(false);
        exitButton = createButton("EXIT", new Color(239, 68, 68));
        exitButton.setPreferredSize(new Dimension(120, 42));
        exitButton.addActionListener(e -> dispose());
        topBar.add(exitButton);
        panel.add(topBar, BorderLayout.NORTH);

        JPanel centerWrapper = new JPanel();
        centerWrapper.setOpaque(false);
        centerWrapper.setLayout(new GridBagLayout());

        JPanel centerCard = new JPanel();
        centerCard.setBackground(new Color(13, 22, 38));
        centerCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(96, 145, 255), 2),
            new EmptyBorder(26, 26, 26, 26)
        ));
        centerCard.setLayout(new BoxLayout(centerCard, BoxLayout.Y_AXIS));
        centerCard.setPreferredSize(new Dimension(860, 660));
        centerCard.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel heroRow = new JPanel(new BorderLayout());
        heroRow.setOpaque(false);
        heroRow.setMaximumSize(new Dimension(700, 120));

        JLabel leftAccent = new JLabel(" ");
        leftAccent.setOpaque(true);
        leftAccent.setBackground(new Color(118, 188, 255));
        leftAccent.setPreferredSize(new Dimension(4, 80));

        JPanel titleStack = new JPanel();
        titleStack.setOpaque(false);
        titleStack.setLayout(new BoxLayout(titleStack, BoxLayout.Y_AXIS));
        titleStack.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel badge = new JLabel("WELCOME TO");
        badge.setFont(new Font("Segoe UI", Font.BOLD, 13));
        badge.setForeground(new Color(170, 215, 255));
        badge.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("DSA QUEST");
        title.setFont(new Font("Segoe UI", Font.BOLD, 62));
        title.setForeground(new Color(247, 249, 255));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("MASTER DATA STRUCTURES & ALGORITHMS");
        subtitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        subtitle.setForeground(new Color(132, 186, 255));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        titleStack.add(badge);
        titleStack.add(Box.createVerticalStrut(6));
        titleStack.add(title);
        titleStack.add(Box.createVerticalStrut(6));
        titleStack.add(subtitle);

        heroRow.add(leftAccent, BorderLayout.WEST);
        heroRow.add(titleStack, BorderLayout.CENTER);

        JLabel description = new JLabel(
            "<html><center>Sharpen your logic, solve challenge rounds,<br>and master essential DSA concepts.</center></html>"
        );
        description.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        description.setForeground(new Color(235, 240, 255));
        description.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel featureBar = new JPanel(new GridLayout(1, 3, 14, 0));
        featureBar.setOpaque(false);
        featureBar.setMaximumSize(new Dimension(560, 70));

        JPanel level = makeStatBadge("10+ TOPICS", "ACTIVE");
        JPanel timer = makeStatBadge("30s", "PER ROUND");
        JPanel mode = makeStatBadge("3 LEVELS", "DIFFICULTY");

        featureBar.add(level);
        featureBar.add(timer);
        featureBar.add(mode);

        JLabel footerNote = new JLabel("DSA PRACTICE MODE");
        footerNote.setFont(new Font("Segoe UI", Font.BOLD, 12));
        footerNote.setForeground(new Color(152, 205, 255));
        footerNote.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel optionCard = new JPanel();
        optionCard.setBackground(new Color(11, 18, 31));
        optionCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(120, 160, 240), 1),
            new EmptyBorder(18, 18, 18, 18)
        ));
        optionCard.setLayout(new BoxLayout(optionCard, BoxLayout.Y_AXIS));
        optionCard.setMaximumSize(new Dimension(560, 360));

        JLabel topicTitle = new JLabel("SELECT TOPIC");
        styleLabel(topicTitle);

        topicBox = new JComboBox<>(new String[]{
            "All Topics",
            "Arrays",
            "Linked Lists",
            "Stacks",
            "Queues",
            "Trees",
            "Hashing",
            "Graphs",
            "Sorting",
            "Searching"
        });
        styleCombo(topicBox);

        JLabel difficultyTitle = new JLabel("SELECT DIFFICULTY");
        styleLabel(difficultyTitle);

        difficultyBox = new JComboBox<>(new String[]{
            "Easy", "Medium", "Hard"
        });
        styleCombo(difficultyBox);

        JButton startButton = createButton("START GAME", GREEN);
        startButton.setPreferredSize(new Dimension(260, 52));
        startButton.addActionListener(e -> startGame());

        JButton instructionButton = createButton("HOW TO PLAY", BLUE);
        instructionButton.setPreferredSize(new Dimension(260, 52));
        instructionButton.addActionListener(e -> showHowToPlayDialog());

        centerCard.add(heroRow);
        centerCard.add(Box.createVerticalStrut(14));
        centerCard.add(description);
        centerCard.add(Box.createVerticalStrut(18));
        centerCard.add(featureBar);
        centerCard.add(Box.createVerticalStrut(14));
        centerCard.add(footerNote);
        centerCard.add(Box.createVerticalStrut(18));
        centerCard.add(optionCard);

        optionCard.add(topicTitle);
        optionCard.add(Box.createVerticalStrut(8));
        optionCard.add(topicBox);
        optionCard.add(Box.createVerticalStrut(14));
        optionCard.add(difficultyTitle);
        optionCard.add(Box.createVerticalStrut(8));
        optionCard.add(difficultyBox);
        optionCard.add(Box.createVerticalStrut(22));
        optionCard.add(startButton);
        optionCard.add(Box.createVerticalStrut(10));
        optionCard.add(instructionButton);

        centerWrapper.add(centerCard);
        panel.add(centerWrapper, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createQuizPanel() {
        JPanel panel = createGradientPanel();
        panel.setLayout(new BorderLayout(20, 20));
        panel.setBorder(new EmptyBorder(32, 42, 32, 42));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setBorder(new EmptyBorder(0, 5, 8, 5));

        JPanel topStats = new JPanel(new GridLayout(1, 5));
        topStats.setOpaque(false);

        progressLabel = new JLabel("Question 1");
        progressLabel.setForeground(WHITE);
        progressLabel.setFont(new Font("SansSerif", Font.BOLD, 18));

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setForeground(GREEN);
        scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 18));

        timeLabel = new JLabel("Time: 30s");
        timeLabel.setForeground(new Color(255, 200, 90));
        timeLabel.setFont(new Font("SansSerif", Font.BOLD, 18));

        accuracyLabel = new JLabel("Accuracy: 0%");
        accuracyLabel.setForeground(new Color(160, 210, 255));
        accuracyLabel.setFont(new Font("SansSerif", Font.BOLD, 18));

        streakLabel = new JLabel("Streak: 0");
        streakLabel.setForeground(new Color(255, 128, 171));
        streakLabel.setFont(new Font("SansSerif", Font.BOLD, 18));

        topStats.add(progressLabel);
        topStats.add(scoreLabel);
        topStats.add(timeLabel);
        topStats.add(accuracyLabel);
        topStats.add(streakLabel);

        topBar.add(topStats, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(new EmptyBorder(20, 22, 20, 22));

        JPanel questionCard = new JPanel();
        questionCard.setBackground(new Color(17, 28, 46));
        questionCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(120, 160, 240), 1),
            new EmptyBorder(22, 22, 22, 22)
        ));
        questionCard.setLayout(new BorderLayout());

        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Segoe UI", Font.BOLD, 25));
        questionLabel.setForeground(new Color(245, 248, 255));
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        questionLabel.setVerticalAlignment(SwingConstants.CENTER);
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        questionCard.add(questionLabel, BorderLayout.CENTER);
        centerPanel.add(questionCard);
        centerPanel.add(Box.createVerticalStrut(18));

        answerButtons = new JRadioButton[4];
        answerGroup = new ButtonGroup();

        for (int i = 0; i < 4; i++) {
            answerButtons[i] = createAnswerButton();
            answerGroup.add(answerButtons[i]);
            answerButtons[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            answerButtons[i].setMaximumSize(new Dimension(900, 56));
            centerPanel.add(answerButtons[i]);
            centerPanel.add(Box.createVerticalStrut(12));
        }

        feedbackLabel = new JLabel(" ");
        feedbackLabel.setFont(new Font("SansSerif", Font.BOLD, 17));
        feedbackLabel.setForeground(GREEN);
        feedbackLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(Box.createVerticalStrut(8));
        centerPanel.add(feedbackLabel);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setOpaque(false);

        submitButton = createButton("SUBMIT ANSWER", BLUE);
        submitButton.setPreferredSize(new Dimension(230, 54));
        submitButton.setFont(new Font("Segoe UI", Font.BOLD, 16));

        nextButton = createButton("NEXT QUESTION", new Color(34, 197, 94));
        nextButton.setPreferredSize(new Dimension(240, 54));
        nextButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nextButton.setEnabled(false);
        nextButton.setBackground(new Color(34, 197, 94));
        nextButton.setForeground(Color.WHITE);
        nextButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(22, 163, 74), 2),
            new EmptyBorder(12, 18, 12, 18)
        ));

        JButton menuButton = createButton("BACK TO MENU", new Color(88, 101, 242));
        menuButton.setPreferredSize(new Dimension(220, 54));
        menuButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        menuButton.addActionListener(e -> returnToHome());

        submitButton.addActionListener(e -> checkAnswer());
        nextButton.addActionListener(e -> nextQuestion());

        bottomPanel.add(submitButton);
        bottomPanel.add(nextButton);
        bottomPanel.add(menuButton);

        panel.add(topBar, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createResultPanel() {
        JPanel panel = createGradientPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(70, 60, 70, 60));

        JLabel title = new JLabel("QUIZ COMPLETED!");
        title.setFont(new Font("SansSerif", Font.BOLD, 40));
        title.setForeground(new Color(120, 255, 180));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel finalScore = new JLabel();
        finalScore.setName("finalScore");
        finalScore.setFont(new Font("SansSerif", Font.BOLD, 28));
        finalScore.setForeground(WHITE);
        finalScore.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel resultMessage = new JLabel();
        resultMessage.setName("resultMessage");
        resultMessage.setFont(new Font("SansSerif", Font.PLAIN, 20));
        resultMessage.setForeground(SOFT);
        resultMessage.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel leaderboard = new JLabel();
        leaderboard.setName("leaderboard");
        leaderboard.setFont(new Font("SansSerif", Font.BOLD, 16));
        leaderboard.setForeground(new Color(255, 215, 120));
        leaderboard.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton playAgain = createButton("BACK TO MENU", GREEN);
        playAgain.setPreferredSize(new Dimension(260, 52));
        playAgain.addActionListener(e -> returnToHome());

        JButton exitButton = createButton("EXIT GAME", BLUE);
        exitButton.setPreferredSize(new Dimension(260, 52));
        exitButton.addActionListener(e -> dispose());

        panel.add(title);
        panel.add(Box.createVerticalStrut(20));
        panel.add(finalScore);
        panel.add(Box.createVerticalStrut(12));
        panel.add(resultMessage);
        panel.add(Box.createVerticalStrut(12));
        panel.add(leaderboard);
        panel.add(Box.createVerticalStrut(30));
        panel.add(playAgain);
        panel.add(Box.createVerticalStrut(12));
        panel.add(exitButton);

        return panel;
    }

    private JRadioButton createAnswerButton() {
        JRadioButton button = new JRadioButton();
        button.setOpaque(true);
        button.setBackground(new Color(22, 38, 62));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setHorizontalTextPosition(SwingConstants.RIGHT);
        button.setVerticalAlignment(SwingConstants.CENTER);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(110, 145, 220), 1),
            new EmptyBorder(12, 14, 12, 14)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setIconTextGap(12);

        button.addActionListener(e -> {
            for (JRadioButton other : answerButtons) {
                if (other != null && other != button) {
                    other.setBackground(new Color(22, 38, 62));
                    other.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(110, 145, 220), 1),
                        new EmptyBorder(12, 14, 12, 14)
                    ));
                }
            }
            button.setBackground(new Color(35, 59, 98));
            button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 200, 255), 1),
                new EmptyBorder(12, 14, 12, 14)
            ));
        });

        return button;
    }

    private List<Question> getQuestions() {
        List<Question> questions = new ArrayList<>();

        add(questions, "Arrays", "Easy",
            "What is the index of the first element in a Java array?",
            new String[]{"0", "1", "-1", "2"}, 0);

        add(questions, "Arrays", "Medium",
            "What is the time complexity of accessing an array element by index?",
            new String[]{"O(n)", "O(log n)", "O(1)", "O(n²)"}, 2);

        add(questions, "Arrays", "Hard",
            "Which technique is commonly used to find a pair with a target sum in a sorted array?",
            new String[]{"Bubble Sort", "Two Pointers", "DFS", "BFS"}, 1);

        add(questions, "Linked Lists", "Easy",
            "What does a node in a singly linked list contain?",
            new String[]{"Only data", "Data and next reference", "Only an index", "Two arrays"}, 1);

        add(questions, "Linked Lists", "Medium",
            "What is the time complexity of inserting at the head of a linked list?",
            new String[]{"O(1)", "O(n)", "O(log n)", "O(n²)"}, 0);

        add(questions, "Linked Lists", "Hard",
            "Which technique can detect a cycle in a linked list?",
            new String[]{"Binary Search", "Merge Sort", "Floyd's Cycle Detection", "Linear Search"}, 2);

        add(questions, "Stacks", "Easy",
            "Which principle does a stack follow?",
            new String[]{"FIFO", "LIFO", "Random", "Priority"}, 1);

        add(questions, "Stacks", "Medium",
            "Which operation removes the top element of a stack?",
            new String[]{"Push", "Peek", "Pop", "Insert"}, 2);

        add(questions, "Stacks", "Hard",
            "Which data structure is commonly used to check balanced parentheses?",
            new String[]{"Queue", "Stack", "Graph", "Array only"}, 1);

        add(questions, "Queues", "Easy",
            "Which principle does a queue follow?",
            new String[]{"LIFO", "FIFO", "Random", "None"}, 1);

        add(questions, "Queues", "Medium",
            "Which operation adds an element to a queue?",
            new String[]{"Dequeue", "Pop", "Enqueue", "Peek"}, 2);

        add(questions, "Queues", "Hard",
            "Which queue connects the last position back to the first?",
            new String[]{"Priority Queue", "Circular Queue", "Simple Stack", "Binary Queue"}, 1);

        add(questions, "Trees", "Easy",
            "What is the topmost node of a tree called?",
            new String[]{"Leaf", "Root", "Child", "Edge"}, 1);

        add(questions, "Trees", "Medium",
            "In a Binary Search Tree, values in the left subtree are generally:",
            new String[]{"Greater than the root", "Less than the root", "Always equal", "Unrelated"}, 1);

        add(questions, "Trees", "Hard",
            "Which traversal visits Left, Root, then Right?",
            new String[]{"Preorder", "Postorder", "Inorder", "Level order"}, 2);

        add(questions, "Hashing", "Easy",
            "What is the main purpose of a hash function?",
            new String[]{"To sort data", "To map data to a fixed-size index", "To remove duplicates", "To create a tree"}, 1);

        add(questions, "Hashing", "Medium",
            "What happens when two different keys produce the same hash value?",
            new String[]{"A stack overflow", "A collision occurs", "The tree is balanced", "The queue is reset"}, 1);

        add(questions, "Graphs", "Easy",
            "What does a graph consist of?",
            new String[]{"Nodes and edges", "Only arrays", "Only stacks", "Indexes and loops"}, 0);

        add(questions, "Graphs", "Medium",
            "Which algorithm is commonly used for unweighted shortest paths?",
            new String[]{"Bubble Sort", "BFS", "Insertion Sort", "Binary Search"}, 1);

        add(questions, "Graphs", "Hard",
            "Which algorithm finds shortest paths with non-negative edge weights?",
            new String[]{"Dijkstra's Algorithm", "DFS", "Bubble Sort", "Selection Sort"}, 0);

        add(questions, "Sorting", "Easy",
            "Which sorting algorithm repeatedly compares adjacent elements?",
            new String[]{"Merge Sort", "Bubble Sort", "Binary Search", "DFS"}, 1);

        add(questions, "Sorting", "Medium",
            "What is the average time complexity of Merge Sort?",
            new String[]{"O(n)", "O(n²)", "O(n log n)", "O(log n)"}, 2);

        add(questions, "Sorting", "Hard",
            "What is the worst-case time complexity of Quick Sort?",
            new String[]{"O(n)", "O(n²)", "O(log n)", "O(1)"}, 1);

        add(questions, "Searching", "Easy",
            "Which searching algorithm checks elements one by one?",
            new String[]{"Binary Search", "Linear Search", "Merge Sort", "DFS"}, 1);

        add(questions, "Searching", "Medium",
            "What is the time complexity of Binary Search on sorted data?",
            new String[]{"O(n)", "O(n²)", "O(log n)", "O(1)"}, 2);

        add(questions, "Searching", "Hard",
            "What condition is required for standard Binary Search?",
            new String[]{"Data must be sorted", "Data must be reversed", "No duplicates allowed", "Data must be a graph"}, 0);

        return questions;
    }

    private void add(List<Question> list, String topic,
                     String difficulty, String question,
                     String[] choices, int answer) {
        list.add(new Question(topic, difficulty, question, choices, answer));
    }

    private void returnToHome() {
        if (questionTimer != null) {
            questionTimer.stop();
        }
        currentQuestion = 0;
        score = 0;
        correctAnswers = 0;
        answeredCount = 0;
        streak = 0;
        timeLeft = 30;
        cardLayout.show(mainPanel, "HOME");
    }

    private void startGame() {
        String selectedTopic = (String) topicBox.getSelectedItem();
        String selectedDifficulty = (String) difficultyBox.getSelectedItem();

        quizQuestions = new ArrayList<>();

        for (Question q : getQuestions()) {
            boolean topicMatches = selectedTopic.equals("All Topics") || q.topic.equals(selectedTopic);
            boolean difficultyMatches = q.difficulty.equals(selectedDifficulty);

            if (topicMatches && difficultyMatches) {
                quizQuestions.add(q);
            }
        }

        if (quizQuestions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No questions available.");
            return;
        }

        if (questionTimer != null) {
            questionTimer.stop();
        }

        Collections.shuffle(quizQuestions);
        currentQuestion = 0;
        score = 0;
        correctAnswers = 0;
        answeredCount = 0;
        streak = 0;

        cardLayout.show(mainPanel, "QUIZ");
        showQuestion();
    }

    private void startQuestionTimer() {
        if (questionTimer != null) {
            questionTimer.stop();
        }

        timeLeft = 30;
        timeLabel.setText("Time: 30s");

        questionTimer = new Timer(1000, e -> {
            timeLeft--;
            timeLabel.setText("Time: " + Math.max(timeLeft, 0) + "s");

            if (timeLeft <= 0) {
                questionTimer.stop();
                handleAnswer(-1, true);
            }
        });
        questionTimer.start();
    }

    private void updateAccuracyLabel() {
        int percent = (answeredCount == 0) ? 0 : (correctAnswers * 100) / answeredCount;
        accuracyLabel.setText("Accuracy: " + percent + "%");
    }

    private void updateScoreboard() {
        scoreLabel.setText("Score: " + score);
        streakLabel.setText("Streak: " + streak);
        updateAccuracyLabel();
    }

    private void showQuestion() {
        if (currentQuestion >= quizQuestions.size()) {
            showResults();
            return;
        }

        Question q = quizQuestions.get(currentQuestion);

        progressLabel.setText("Question " + (currentQuestion + 1) + " / " + quizQuestions.size());
        updateScoreboard();
        startQuestionTimer();

        questionLabel.setText(
            "<html><div style='width:760px; text-align:center; line-height:1.5; color:#F4F7FF; font-family:Segoe UI, Sans-Serif; font-size:26px; font-weight:bold'>" +
            "<span style='color:#A6C2FF; font-size:18px; font-weight:bold'>" + q.topic + " - " + q.difficulty + "</span><br><br>" +
            q.question + "</div></html>"
        );

        for (int i = 0; i < 4; i++) {
            answerButtons[i].setText((char) ('A' + i) + ". " + q.choices[i]);
            answerButtons[i].setSelected(false);
            answerButtons[i].setEnabled(true);
            answerButtons[i].setBorder(new RoundedBorder(new Color(100, 140, 220), 18));
        }

        answerGroup.clearSelection();
        feedbackLabel.setText(" ");
        feedbackLabel.setForeground(GREEN);
        submitButton.setEnabled(true);
        nextButton.setEnabled(false);
    }

    private void handleAnswer(int selected, boolean timedOut) {
        if (questionTimer != null) {
            questionTimer.stop();
        }

        Question q = quizQuestions.get(currentQuestion);
        boolean correct = selected == q.correctAnswer;

        if (correct) {
            score += 10;
            correctAnswers++;
            streak++;
            feedbackLabel.setForeground(GREEN);
            feedbackLabel.setText(timedOut ? "TIME'S UP! Correct answer: " + q.choices[q.correctAnswer] : "CORRECT! +10 POINTS");
        } else {
            streak = 0;
            feedbackLabel.setForeground(new Color(255, 110, 110));
            feedbackLabel.setText(timedOut ? "TIME'S UP! Correct answer: " + q.choices[q.correctAnswer] : "INCORRECT! Correct answer: " + q.choices[q.correctAnswer]);
        }

        answeredCount++;
        updateScoreboard();

        for (JRadioButton button : answerButtons) {
            button.setEnabled(false);
        }

        submitButton.setEnabled(false);
        nextButton.setEnabled(true);
        nextButton.setBackground(new Color(34, 197, 94));
        nextButton.setForeground(Color.WHITE);
        nextButton.setOpaque(true);
        nextButton.setContentAreaFilled(true);
    }

    private void checkAnswer() {
        int selected = -1;

        for (int i = 0; i < answerButtons.length; i++) {
            if (answerButtons[i].isSelected()) {
                selected = i;
                break;
            }
        }

        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Please select an answer first!");
            return;
        }

        handleAnswer(selected, false);
    }

    private void nextQuestion() {
        currentQuestion++;
        if (currentQuestion < quizQuestions.size()) {
            showQuestion();
        } else {
            showResults();
        }
    }

    private void showResults() {
        cardLayout.show(mainPanel, "RESULT");

        int total = quizQuestions.size();
        int percentage = (correctAnswers * 100) / total;

        JLabel finalScore = findResultLabel("finalScore");
        JLabel resultMessage = findResultLabel("resultMessage");
        JLabel leaderboard = findResultLabel("leaderboard");

        if (finalScore != null) {
            finalScore.setText("YOUR SCORE: " + score + " / " + (total * 10));
        }

        String message;
        if (percentage == 100) {
            message = "Excellent! You are a DSA Champion!";
        } else if (percentage >= 70) {
            message = "Great job! Keep improving!";
        } else if (percentage >= 40) {
            message = "Good effort! Keep practicing!";
        } else {
            message = "Keep learning! You can do better!";
        }

        if (resultMessage != null) {
            resultMessage.setText(
                "<html><center>" + message + "<br><br>" +
                "Correct answers: " + correctAnswers + " out of " + total +
                "<br>Accuracy: " + percentage + "%</center></html>"
            );
        }

        if (leaderboard != null) {
            leaderboard.setText("LEADERBOARD: " + correctAnswers + " wins • Streak: " + streak + " • Rank: " + getRankLabel(percentage));
        }
    }

    private String getRankLabel(int percentage) {
        if (percentage == 100) return "Elite Master";
        if (percentage >= 70) return "Gold Tier";
        if (percentage >= 40) return "Silver Tier";
        return "Practice Mode";
    }

    private JLabel findResultLabel(String name) {
        for (Component component : ((JPanel) mainPanel.getComponent(2)).getComponents()) {
            if (component instanceof JLabel && name.equals(component.getName())) {
                return (JLabel) component;
            }
        }
        return null;
    }

    private void showHowToPlayDialog() {
        JDialog dialog = new JDialog(this, "How to Play", true);
        dialog.setSize(640, 430);
        dialog.setLocationRelativeTo(this);
        dialog.setUndecorated(false);
        dialog.getContentPane().setBackground(new Color(8, 15, 31));

        JPanel content = new JPanel();
        content.setLayout(new BorderLayout(16, 16));
        content.setBackground(new Color(11, 18, 31));
        content.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("HOW TO PLAY");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(137, 197, 255));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel body = new JLabel(
            "<html><body style='width: 460px; font-family:Segoe UI; font-size:16px; color:#F4F7FF; line-height:1.8'>"
            + "1. Choose your topic and difficulty.<br>"
            + "2. Click <b>START GAME</b> to begin the round.<br>"
            + "3. Read the question carefully before answering.<br>"
            + "4. Pick the best option and click <b>SUBMIT ANSWER</b>.<br>"
            + "5. Earn points for correct answers and track your streak.<br>"
            + "6. Use <b>BACK TO MENU</b> anytime to restart from home.<br><br>"
            + "<span style='color:#A6C2FF'>Complete the challenge and improve your DSA skills!</span>"
            + "</body></html>"
        );
        body.setVerticalAlignment(SwingConstants.TOP);

        JButton closeButton = createButton("CLOSE", BLUE);
        closeButton.setPreferredSize(new Dimension(160, 46));
        closeButton.addActionListener(e -> dialog.dispose());

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setOpaque(false);
        footer.add(closeButton);

        content.add(title, BorderLayout.NORTH);
        content.add(body, BorderLayout.CENTER);
        content.add(footer, BorderLayout.SOUTH);

        dialog.setContentPane(content);
        dialog.setVisible(true);
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker(), 2),
            new EmptyBorder(12, 18, 12, 18)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(220, 48));
        button.setMaximumSize(new Dimension(300, 48));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setText(text);

        final Color base = color;
        final Color hover = color.brighter();

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hover);
                button.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(base);
                button.setForeground(Color.WHITE);
            }
        });

        return button;
    }

    private JPanel makeStatBadge(String value, String labelText) {
        JPanel box = new JPanel();
        box.setOpaque(true);
        box.setBackground(new Color(10, 18, 32));
        box.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(108, 149, 255), 1),
            new EmptyBorder(10, 12, 10, 12)
        ));
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        valueLabel.setForeground(new Color(235, 245, 255));
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel labelName = new JLabel(labelText);
        labelName.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        labelName.setForeground(new Color(160, 200, 255));
        labelName.setAlignmentX(Component.CENTER_ALIGNMENT);

        box.add(valueLabel);
        box.add(Box.createVerticalStrut(2));
        box.add(labelName);
        return box;
    }

    private void styleLabel(JLabel label) {
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void styleCombo(JComboBox<String> combo) {
        combo.setFont(new Font("SansSerif", Font.PLAIN, 15));
        combo.setBackground(new Color(17, 29, 48));
        combo.setForeground(Color.WHITE);
        combo.setOpaque(true);
        combo.setMaximumSize(new Dimension(370, 42));
        combo.setAlignmentX(Component.CENTER_ALIGNMENT);
        combo.setBorder(BorderFactory.createLineBorder(new Color(120, 160, 240), 1));

        DefaultListCellRenderer renderer = new DefaultListCellRenderer();
        renderer.setOpaque(true);
        renderer.setForeground(Color.WHITE);
        renderer.setBackground(new Color(17, 29, 48));
        combo.setRenderer(renderer);
    }

    public static void main(String[] args) {
        SinglyLinkedList list = new SinglyLinkedList();
        list.insertAtHead(10);
        list.insertAtHead(20);
        list.insertAtTail(30);
        list.print();
        System.out.println("Contains 20? " + list.contains(20));
        System.out.println("Deleted 20? " + list.delete(20));
        list.print();

        ArrayStack stack = new ArrayStack(5);
        stack.push(10);
        stack.push(20);
        stack.push(30);
        stack.print();
        System.out.println("Top = " + stack.peek());
        System.out.println("Popped = " + stack.pop());
        stack.print();

        CircularQueue queue = new CircularQueue(4);
        queue.enqueue(5);
        queue.enqueue(8);
        queue.enqueue(12);
        queue.print();
        System.out.println("Dequeued = " + queue.dequeue());
        queue.print();

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                UIManager.put("ComboBox.background", new Color(17, 29, 48));
                UIManager.put("ComboBox.foreground", Color.WHITE);
                UIManager.put("ComboBox.selectionBackground", new Color(59, 130, 246));
                UIManager.put("ComboBox.selectionForeground", Color.WHITE);
                UIManager.put("Button.foreground", Color.WHITE);
                UIManager.put("Button.background", new Color(59, 130, 246));
                UIManager.put("Label.foreground", Color.WHITE);
                UIManager.put("RadioButton.foreground", Color.WHITE);
                UIManager.put("CheckBox.foreground", Color.WHITE);
                UIManager.put("Panel.background", new Color(8, 15, 31));
            } catch (ClassNotFoundException | InstantiationException |
                     IllegalAccessException | UnsupportedLookAndFeelException e) {
                System.out.println("Using default Java interface.");
            }

            DSAQuest game = new DSAQuest();
            game.setVisible(true);
        });
    }
}