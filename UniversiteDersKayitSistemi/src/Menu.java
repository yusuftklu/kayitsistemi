import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class Menu extends JFrame{
    private JButton OgrenciFormuButton;
    private JPanel MenuPaneli;
    private JButton DersFormuButton;
    private JButton OgretimGorevlisiFormuButton;
    private JList list1;

    public  Menu(){
        setTitle("Menü");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);
        setVisible(true);
        setContentPane(MenuPaneli);

        DersFormuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DersFormu DersFormu = new DersFormu();
                DersFormu.setVisible(true);
            }
        });
        OgretimGorevlisiFormuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OgretimGorevlisiFormu OgretimGorevlisiFormu = new OgretimGorevlisiFormu();
                OgretimGorevlisiFormu.setVisible(true);
            }
        });
        OgrenciFormuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OgrenciFormu OgrenciFormu =new OgrenciFormu();
                OgrenciFormu.setVisible(true);
            }
        });

        OgrenciFormuButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                OgrenciFormuButton.setBackground(new Color(255, 255, 255));
                super.mouseEntered(e);
            }
        });
        OgrenciFormuButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                OgrenciFormuButton.setBackground(new Color(255, 255, 255));
                super.mouseExited(e);
            }
        });
        //Burda DersKayitFormuButton özellikler var
        DersFormuButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                DersFormuButton.setBackground(new Color(255, 255, 255));
                super.mouseEntered(e);
            }
        });
        DersFormuButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                DersFormuButton.setBackground(new Color(255, 255, 255));
                super.mouseExited(e);
            }
        });
        //Burda ÖğretimGörevlisiFormuButton özellikler var
        OgretimGorevlisiFormuButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                OgretimGorevlisiFormuButton.setBackground(new Color(255, 250, 225));
                super.mouseEntered(e);
            }
        });
        OgretimGorevlisiFormuButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                OgretimGorevlisiFormuButton.setBackground(new Color(255, 255, 255));
                super.mouseExited(e);
            }
        });
    }
    public static void main(String[] args) {
        new Menu();
    }
}
