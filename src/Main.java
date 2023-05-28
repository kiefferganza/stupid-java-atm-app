import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

class Account {
    private int pin;
    private double balance;

    public Account(int pin, double balance) {
        this.pin = pin;
        this.balance = balance;
    }

    public int getPin() {
        return pin;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) {
        if (amount > balance) {
            JOptionPane.showMessageDialog(null, "Insufficient balance.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            balance -= amount;
            JOptionPane.showMessageDialog(null, "Withdrawal successful.");
        }
    }
}

class ATM {
    private Map<Integer, Account> accounts;
    private Account currentAccount;

    public ATM() {
        accounts = new HashMap<>();
        // Initialize dummy accounts for testing
        accounts.put(1234, new Account(1234, 1000.0));
        accounts.put(5678, new Account(5678, 5000.0));
    }

    public boolean login(int pin) {
        if (accounts.containsKey(pin)) {
            currentAccount = accounts.get(pin);
            return true;
        } else {
            return false;
        }
    }

    public double getBalance() {
        return currentAccount.getBalance();
    }

    public void deposit(double amount) {
        currentAccount.deposit(amount);
    }

    public void withdraw(double amount) {
        currentAccount.withdraw(amount);
    }
}

public class Main extends JFrame implements ActionListener {
    private ATM atm;
    private JPanel loginPanel;
    private JPanel sidebarPanel;
    private JPanel centerPanel;
    private JTextField pinField;
    private JButton loginButton;
    private JButton depositButton;
    private JButton withdrawButton;
    private JButton checkBalanceButton;

    public Main() {
        atm = new ATM();

        loginPanel = new JPanel(new FlowLayout());
        JLabel pinLabel = new JLabel("PIN:");
        pinField = new JTextField(10);
        loginButton = new JButton("Login");
        loginButton.addActionListener(this);

        loginPanel.add(pinLabel);
        loginPanel.add(pinField);
        loginPanel.add(loginButton);

        sidebarPanel = new JPanel(new GridLayout(5, 2));
        depositButton = new JButton("Deposit");
        depositButton.addActionListener(this);
        withdrawButton = new JButton("Withdraw");
        withdrawButton.addActionListener(this);
        checkBalanceButton = new JButton("Check Balance");
        checkBalanceButton.addActionListener(this);
        sidebarPanel.add(depositButton);
        sidebarPanel.add(new JLabel());
        sidebarPanel.add(withdrawButton);
        sidebarPanel.add(new JLabel());

        sidebarPanel.add(checkBalanceButton);

        centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.blue);
        sidebarPanel.setBackground(Color.RED);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        setTitle("Simple ATM");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(loginPanel, BorderLayout.NORTH);
        pack();
        setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            int pin = Integer.parseInt(pinField.getText());
            if (atm.login(pin)) {
                getContentPane().remove(loginPanel);
                add(sidebarPanel, BorderLayout.WEST);
                add(centerPanel, BorderLayout.CENTER);
                pack();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid PIN. Login failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == depositButton) {
            centerPanel.removeAll();
            JPanel depositPanel = new JPanel(new FlowLayout());

            JLabel amountLabel = new JLabel("Amount to Deposit:");
            JTextField depositField = new JTextField(10);
            JButton depositConfirmButton = new JButton("Deposit");
            depositPanel.add(amountLabel);
            depositPanel.add(depositField);
            depositPanel.add(depositConfirmButton);

            depositConfirmButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    double amount = Double.parseDouble(depositField.getText());
                    atm.deposit(amount);
                    JOptionPane.showMessageDialog(Main.this, "Deposit successful.");
                }
            });

            centerPanel.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(10, 10, 10, 10);
            centerPanel.add(depositPanel, gbc);
            revalidate();
            repaint();
        } else if (e.getSource() == withdrawButton) {
            centerPanel.removeAll();
            JPanel withdrawPanel = new JPanel(new FlowLayout());

            JLabel amountLabel = new JLabel("Amount to Withdraw:");
            JTextField withdrawField = new JTextField(10);
            JButton withdrawConfirmButton = new JButton("Withdraw");
            withdrawPanel.add(amountLabel);
            withdrawPanel.add(withdrawField);
            withdrawPanel.add(withdrawConfirmButton);

            withdrawConfirmButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    double amount = Double.parseDouble(withdrawField.getText());
                    atm.withdraw(amount);
                }
            });

            centerPanel.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(10, 10, 10, 10);
            centerPanel.add(withdrawPanel, gbc);
            revalidate();
            repaint();
        } else if (e.getSource() == checkBalanceButton) {
            centerPanel.removeAll();

            double balance = atm.getBalance();
            JPanel withdrawPanel = new JPanel(new FlowLayout());
            JLabel amountLabel = new JLabel("Amount to Withdraw: " + balance);
            withdrawPanel.add(amountLabel);

            centerPanel.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(10, 10, 10, 10);
            centerPanel.add(withdrawPanel, gbc);
            revalidate();
            repaint();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
}
