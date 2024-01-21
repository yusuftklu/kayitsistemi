import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class OgretimGorevlisiFormu extends JFrame {
    private JPanel OgretimGorevlisiPanel;
    private JTextField textOgretimGorevlisiNo;
    private JTextField textOgretimGorevlisiAd;
    private JTextField textOgretimGorevlisiSoyad;
    private JComboBox<String> comboBoxBolum;
    private JTextField textField3;
    private JList<String> list1;
    private JLabel OgretimGorevlisiNo;
    private JLabel OgretimGorevlisiAd;
    private JLabel OgretimGorevlisiSoyad;
    private JLabel Bolum;
    private JLabel arama;
    private JButton OgretimGorevlisiKaydet;
    private JButton menuButton;

    public OgretimGorevlisiFormu() {
        setTitle("Öğretim Görevlisi Sayfası");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);
        setVisible(true);
        setContentPane(OgretimGorevlisiPanel);
        OgretimGorevlisiKaydet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateInputs() && !isDuplicateCourse()) {
                    ogretimGorevlisiKaydet();
                    clearForm();

                }
                else {
                    if (isDuplicateCourse()) {
                        JOptionPane.showMessageDialog(OgretimGorevlisiFormu.this, "Bu Öğretim Görevlisi zaten kayıtlı.", "Hata", JOptionPane.ERROR_MESSAGE);
                    }
                    else {

                        JOptionPane.showMessageDialog(OgretimGorevlisiFormu.this, "Lütfen tüm alanları doldurun.", "Hata", JOptionPane.ERROR_MESSAGE);
                    }
                }
                fillList1WithOgretimGorevlisiData();

            }
        });
        fillList1WithOgretimGorevlisiData();
        textField3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterListByTextField();
            }
        });
        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Menu menu = new Menu();
                menu.setVisible(true);
                dispose();
            }
        });
    }
    private void clearForm() {
        textOgretimGorevlisiNo.setText("");
        textOgretimGorevlisiAd.setText("");
        textOgretimGorevlisiSoyad.setText("");
    }
    private boolean validateInputs() {
        return !textOgretimGorevlisiAd.getText().isEmpty() &&
                !textOgretimGorevlisiSoyad.getText().isEmpty() &&
                !textOgretimGorevlisiNo.getText().isEmpty() ;

    }

    private void fillList1WithOgretimGorevlisiData() {
        DefaultListModel<String> listModel = new DefaultListModel<>();

        String existingData = readExistingData();
        String[] lines = existingData.split("\n");
        for (String line : lines) {
            listModel.addElement(line);
        }

        list1.setModel(listModel);
    }
    private void filterListByTextField() {
        DefaultListModel<String> listModel = new DefaultListModel<>();

        String existingData = readExistingData();
        String[] lines = existingData.split("\n");

        String filterText = textField3.getText().trim().toLowerCase();

        if (filterText.isEmpty()) {
            for (String line : lines) {
                listModel.addElement(line);
            }
        } else {
            for (String line : lines) {
                if (line.toLowerCase().contains(filterText)) {
                    listModel.addElement(line);
                }
                else if (filterText.equals("ad") && line.split(",").length > 0) {
                    listModel.addElement(line.split(",")[0]);
                } else if (filterText.equals("soyad") && line.split(",").length > 1) {
                    listModel.addElement(line.split(",")[1]);
                } else if (filterText.equals("ogretim görevlisi no") && line.split(",").length > 2) {
                    listModel.addElement(line.split(",")[2]);
                } else if (filterText.equals("bolum") && line.split(",").length > 3) {
                    listModel.addElement(line.split(",")[3]);
                }


            }
        }

        list1.setModel(listModel);
    }
    private boolean isDuplicateCourse() {
        OgretimGorevlisi ogretimGorevlisi = createOgretimGorevlisiFromInputs();
        String searchData = ogretimGorevlisi.getOgretimGorevlisiAdi() + "," + ogretimGorevlisi.getOgretimGorevlisiSoyadi() + "," + ogretimGorevlisi.getOgretimGorevlisiNo() + "," + ogretimGorevlisi.getOgretimGorevlisiBolum() ;

        String existingData = readExistingData();
        return existingData.contains(searchData);
    }
    private void ogretimGorevlisiKaydet() {// Yeni Öğretim Görevlisi kaydını oluşturur ve dosyaya kaydeder.
        OgretimGorevlisi ogretimGorevlisi = createOgretimGorevlisiFromInputs();
        String newData = ogretimGorevlisi.getOgretimGorevlisiAdi() + "," + ogretimGorevlisi.getOgretimGorevlisiSoyadi() + "," + ogretimGorevlisi.getOgretimGorevlisiNo() + "," + ogretimGorevlisi.getOgretimGorevlisiBolum() ;

        String existingData = readExistingData();
        if (!existingData.isEmpty()) {
            existingData += "\n";
        }
        existingData += newData;

        writeDataToFile(existingData);

        JOptionPane.showMessageDialog(this, "Öğretim Görevlisi Bilgileri kaydedilmiştir.", "Bilgi", JOptionPane.INFORMATION_MESSAGE);

    }
    private OgretimGorevlisi createOgretimGorevlisiFromInputs() {
        OgretimGorevlisi ogretimGorevlisi = new OgretimGorevlisi();
        ogretimGorevlisi.setOgretimGorevlisiAdi(textOgretimGorevlisiAd.getText());
        ogretimGorevlisi.setOgretimGorevlisiSoyadi(textOgretimGorevlisiSoyad.getText());
        ogretimGorevlisi.setOgretimGorevlisiNo(textOgretimGorevlisiNo.getText());
        String selectedBolum = (String) comboBoxBolum.getSelectedItem();
        ogretimGorevlisi.setOgretimGorevlisiBolum(selectedBolum);


        return ogretimGorevlisi;
    }

    private String readExistingData() {
        Path filePath = Paths.get("ogretimGorevlisi.csv");
        if (Files.exists(filePath)) {
            try {
                return new String(Files.readAllBytes(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    private void writeDataToFile(String data) {
        Path filePath = Paths.get("ogretimGorevlisi.csv");
        try {
            Files.write(filePath, data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Dosya yazma hatası.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
    public static void main(String[] args) {
        new OgretimGorevlisiFormu();
    }
}
