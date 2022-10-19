package blockchain;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class BlockChainFrame extends JFrame {
    private JPanel blocksPanel;

    public BlockChainFrame() {
        super("BlockChain");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        this.initComponents();
        setLayout(null);
        setVisible(true);
    }

    private void initComponents() {


//        listScroller.setPreferredSize(new Dimension(250, 80));
         this.blocksPanel = new JPanel(new GridLayout(1, 0));

        JScrollPane listScroller = new JScrollPane(this.blocksPanel);
        listScroller.setSize(1000, 500);
        listScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
//        listScroller.setCorner(JScrollPane.LOWER_RIGHT_CORNER ,new JLabel(""));
        listScroller.setBorder(new EmptyBorder(new Insets(10, 10, 10, 25)));
        add(listScroller);

    }

    public void addBlockToFrame(String blockText) {
        JLabel block = new JLabel(blockText);
        block.setBackground(new Color(75, 255, 255));
        block.setBorder(new LineBorder(Color.BLACK, 5));
        block.setAlignmentY(Component.TOP_ALIGNMENT);
        this.blocksPanel.add(block);
        this.blocksPanel.add(Box.createRigidArea(new Dimension(2, 0)));
        this.blocksPanel.revalidate();
    }

}
