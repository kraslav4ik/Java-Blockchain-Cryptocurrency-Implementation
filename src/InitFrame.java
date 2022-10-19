package blockchain;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

public class InitFrame extends JFrame {

    private final int MIN_BLOCKS = 1;
    private final int MAX_BLOCKS = 15;
    private final int MIN_MINERS = 1;
    private final int MAX_MINERS = 10;
    private final int MIN_USERS = 2;
    private final int MAX_USERS = 10;
    private final int MIN_MONEY = 1;
    private final int MAX_MONEY = 1000;

    public InitFrame() {
        super("BlockChain");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

        this.initComponents();

        setLayout(null);
        setVisible(true);
    }

    private void initComponents() {
        JLabel helloLabel = new JLabel("<html><h2><center>This is BlockChain implementation!<BR><center>Please, fulfill next fields to create your own BlockChain.</h2></html>");
        helloLabel.setBounds(20,0, 450,80);
        add(helloLabel);

        JPanel panel = new JPanel();
        panel.setBounds(50, 100, 400, 200);
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0.5;
        c.weighty = 0.5;

        JLabel blocksAmountLabel = new JLabel("Number of blocks in chain (min=1, max=15):");
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        panel.add(blocksAmountLabel, c);

        JTextField blocksAmountTextField = new JTextField();
        blocksAmountTextField.setHorizontalAlignment(JTextField.CENTER);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 10;
        c.gridx = 1;
        c.gridy = 0;
        panel.add(blocksAmountTextField, c);

        JLabel minersAmountLabel = new JLabel("Miners amount (min=1, max=10):");
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        panel.add(minersAmountLabel, c);

        JTextField minersAmountTextField = new JTextField();
        minersAmountTextField.setHorizontalAlignment(JTextField.CENTER);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        panel.add(minersAmountTextField, c);

        JLabel UsersAmountLabel = new JLabel("Users amount (min=2, max=10):");
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 2;
        panel.add(UsersAmountLabel, c);

        JTextField usersAmountTextField = new JTextField();
        usersAmountTextField.setHorizontalAlignment(JTextField.CENTER);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 2;
        panel.add(usersAmountTextField, c);

        JLabel moneyLabel = new JLabel("Virtual Currency per user? (min=1, max=1000):");
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 3;
        panel.add(moneyLabel, c);

        JTextField moneyTextField = new JTextField();
        moneyTextField.setHorizontalAlignment(JTextField.CENTER);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 3;
        panel.add(moneyTextField, c);


        JLabel MinersAsUsersLabel = new JLabel("Miners can make transactions like users?");
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 4;
        panel.add(MinersAsUsersLabel, c);

        JRadioButton yes = new JRadioButton("Yes");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 4;
        panel.add(yes, c);

        JRadioButton no = new JRadioButton("No");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 4;
        panel.add(no, c);

        ButtonGroup options = new ButtonGroup();
        options.add(yes);
        options.add(no);
        no.setSelected(true);
        add(panel);


        JButton acceptButton = new JButton("Create BlockChain");
        acceptButton.setBounds(20, 300, 150, 50);
        add(acceptButton);
        acceptButton.addActionListener(actionEvent -> {
            JLabel status = new JLabel("");
            status.setBounds(190, 300, 280, 50);
            add(status);
            try {
                int blocksNum = Integer.parseInt(blocksAmountTextField.getText());
                int minersNum = Integer.parseInt(minersAmountTextField.getText());
                int usersNum = Integer.parseInt(usersAmountTextField.getText());
                int moneyPerUser = Integer.parseInt(moneyTextField.getText());
                boolean correctData = true;

                if (blocksNum < MIN_BLOCKS || blocksNum > MAX_BLOCKS) {
                    c.gridx = 2;
                    c.gridy = 0;
                    c.weightx = 1;
                    correctData = false;
                    panel.add(new JLabel("<html><font color=red><- Incorrect</font></html>"), c);

                }
                if (minersNum < MIN_MINERS || minersNum > MAX_MINERS) {
                    c.gridx = 2;
                    c.gridy = 1;
                    correctData = false;
                    panel.add(new JLabel("<html><font color=red> <- Incorrect</font></html>"), c);
                }
                if (usersNum < MIN_USERS || usersNum > MAX_USERS) {
                    c.gridx = 2;
                    c.gridy = 2;
                    correctData = false;
                    panel.add(new JLabel("<html><font color=red> <- Incorrect</font></html>"), c);
                }
                if (moneyPerUser < MIN_MONEY || moneyPerUser > MAX_MONEY) {
                    c.gridx = 2;
                    c.gridy = 3;
                    correctData = false;
                    panel.add(new JLabel("<html><font color=red> <- Incorrect</font></html>"), c);
                }
                panel.revalidate();
                if (correctData) {
                    boolean minersAsUsers = yes.isSelected();

                    status.setText("<html><font color=green>Success</font></html>");


                    ParsedInput input = new ParsedInput(blocksNum, minersNum, usersNum, moneyPerUser, minersAsUsers);
                    Thread newFrame = new Thread(() -> {
                        try {
                            SwingUtilities.invokeAndWait(() -> {
                                BlockChainFrame blockChainFrame = new BlockChainFrame();
                                dispose();
                                BlockChainCreation.createBlockChain(input, blockChainFrame);
                            });
                        } catch (InterruptedException | InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    newFrame.start();
                }
            } catch (NumberFormatException | NullPointerException e) {
                status.setText("<html><font color=red>All the fields should be filled with integer numbers</font></html>");
            }
        });
    }


    class ParsedInput {
        private final int blocksNum;
        private final int minersNum;
        private final int usersNum;
        private final int moneyPerUser;
        private final boolean minersAsUsers;

        public ParsedInput(int blocksNum, int minersNum, int usersNum, int moneyPerUser, boolean minersAsUsers) {
            this.blocksNum = blocksNum;
            this.minersNum = minersNum;
            this.usersNum = usersNum;
            this.moneyPerUser = moneyPerUser;
            this.minersAsUsers = minersAsUsers;
        }

        public int getBlocksNum() {
            return blocksNum;
        }

        public int getMinersNum() {
            return minersNum;
        }

        public int getUsersNum() {
            return usersNum;
        }

        public int getMoneyPerUser() {
            return moneyPerUser;
        }

        public boolean isMinersAsUsers() {
            return minersAsUsers;
        }
    }

}
