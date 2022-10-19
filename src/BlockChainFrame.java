package blockchain;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class BlockChainFrame extends JFrame {
    private JPanel blocksPanel;
    volatile private GridBagConstraints constraints;

    public BlockChainFrame() {
        super("BlockChain");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);

        this.initComponents();
        setLayout(null);
        setVisible(true);
    }

    synchronized private void initComponents() {
        this.blocksPanel = new JPanel();
        GridBagLayout gridBagLayout = new GridBagLayout();
        this.constraints = new GridBagConstraints();
        this.constraints.fill = GridBagConstraints.VERTICAL;
        this.constraints.gridy = 0;
        this.constraints.gridx = 0;

        this.blocksPanel.setLayout(gridBagLayout);
        JScrollPane listScroller = new JScrollPane(this.blocksPanel);
        listScroller.setSize(1000, 600);
        listScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        listScroller.setBorder(new EmptyBorder(new Insets(10, 10, 10, 25)));
        add(listScroller);
    }

    public void addBlockToFrame(String blockText) {
        JPanel panel = new JPanel();
        JLabel block = new JLabel(blockText, SwingConstants.LEFT);
        panel.setBackground(new Color(167, 255, 255));
//        panel.setBorder(new LineBorder(Color.BLACK, 5));
//        panel.setPreferredSize(new Dimension(300, 500));
        panel.add(block);
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setPreferredSize(new Dimension(300, 500));
        scrollPane.setBorder(new LineBorder(Color.BLACK, 5));

        this.blocksPanel.add(scrollPane, constraints);
        this.blocksPanel.revalidate();
        this.constraints.gridx += 1;
        this.blocksPanel.add(new JLabel("--->"), constraints);
        this.constraints.gridx += 1;
        this.blocksPanel.revalidate();
    }

}
