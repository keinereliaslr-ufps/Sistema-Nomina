package app;

import controller.NominaController;
import model.NominaService;
import view.NominaFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        NominaService service = new NominaService();
        NominaController controller = new NominaController(service);

        SwingUtilities.invokeLater(() -> {
            NominaFrame frame = new NominaFrame(controller);
            frame.setVisible(true);
        });
    }
}
