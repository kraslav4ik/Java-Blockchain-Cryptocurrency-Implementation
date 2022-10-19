package blockchain;


import java.lang.reflect.InvocationTargetException;
import javax.swing.*;


public class Main {
    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeAndWait(InitFrame::new);

    }
}
