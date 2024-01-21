import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class OgrenciFormu extends JFrame {
    private JPanel OGPanel;
    private JTextField textAd;
    private JTextField textSoyad;
    private JTextField textNo;
    private JComboBox<String> comboBoxBolum;
    private JComboBox<String> comboBoxDonem;
    private JComboBox<String> comboBoxDers;
    private JTextField textField4;
    private JList<String> list1;
    private JLabel Ad;
    private JLabel Soyad;
    private JLabel No;
    private JLabel Bolum;
    private JLabel Donem;
    private JLabel Ders;
    private JLabel Arama;
    private JButton OgrenciKaydet;
    private JButton menuButton;
    private Ogrenci ogrenci;


    public OgrenciFormu() {
        setTitle("Ogrenci  Sayfası");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);
        setVisible(true);
        setContentPane(OGPanel);

        fillComboBoxBolum();
        fillComboBoxDonem();
        fillComboBoxDers();


        ogrenci = new Ogrenci();
        OgrenciKaydet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateInputs()) {
                    kaydetOgrenci();
                    clearForm();
                }
                else {
                    JOptionPane.showMessageDialog(OgrenciFormu.this, "Tüm alanları doldurmanız gerekiyor", "Hata", JOptionPane.ERROR_MESSAGE);
                }
                fillList1WithOgrenciData();
            }
        });
        fillList1WithOgrenciData();
        textField4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterListByTextField();
            }
        });
        comboBoxDonem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedDersDonemi = (String) comboBoxDonem.getSelectedItem();
                fillComboBoxDers();

            }
        });
        comboBoxBolum.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fillComboBoxDonem();
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
        textAd.setText("");
        textNo.setText("");
        textSoyad.setText("");
    }
    private void fillList1WithOgrenciData() {
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

        String filterText = textField4.getText().trim().toLowerCase();

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
                } else if (filterText.equals("ogrenci no") && line.split(",").length > 2) {
                    listModel.addElement(line.split(",")[2]);
                } else if (filterText.equals("bolum") && line.split(",").length > 3) {
                    listModel.addElement(line.split(",")[3]);
                }
                else if (filterText.equals("donem") && line.split(",").length > 4) {
                    listModel.addElement(line.split(",")[4]);
                }
                else if (filterText.equals("alınan ders") && line.split(",").length > 5) {
                    listModel.addElement(line.split(",")[5]);
                }
            }
        }

        list1.setModel(listModel);
    }

    private String readExistingData() {
        Path filePath = Paths.get("ogrenci.csv");
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
        Path filePath = Paths.get("ogrenci.csv");
        try {
            Files.write(filePath, data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Dosya yazma hatası.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
    private boolean validateInputs() {
        if (textNo.getText().isEmpty() || textSoyad.getText().isEmpty() || textAd.getText().isEmpty()) {
            return false;
        }
        return true;
    }
    private void kaydetOgrenci() {
        String ogrenciNo = textNo.getText().trim();
        String bolum = (String) comboBoxBolum.getSelectedItem();
        String soyad = textSoyad.getText().trim();
        String ogrenciAdi = textAd.getText().trim();
        String selectedDersDonemi = (String) comboBoxDonem.getSelectedItem();
        String selectedDersAdi = (String) comboBoxDers.getSelectedItem();


        ogrenci.setOgrenciAdi(ogrenciAdi);
        ogrenci.setOgrenciSoyadi(soyad);
        ogrenci.setOgrenciNo(ogrenciNo);
        ogrenci.setOgrenciBolum(bolum);
        ogrenci.setOgrenciDonem(selectedDersDonemi);
        ogrenci.setOgrenciDersSecimi(selectedDersAdi);


        String ogrenciVerisi = ogrenci.getOgrenciAdi() + "," + ogrenci.getOgrenciSoyadi() + "," + ogrenci.getOgrenciNo() + "," + ogrenci.getOgrenciBolum() + "," + ogrenci.getOgrenciDonem() + "," + ogrenci.getOgrenciDersSecimi();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("ogrenci.csv", true))) {
            writer.write(ogrenciVerisi);
            writer.newLine();

            JOptionPane.showMessageDialog(this, "Sayın Öğrenci Bilgileriniz kayıt altına alınmıştır", "Bilgi", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "File writing error.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fillComboBoxBolum() {
        Set<String> uniqueBolumValues = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("dersKayit.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String bolumValue = parts[3].trim();
                    if (!bolumValue.isEmpty()) {
                        uniqueBolumValues.add(bolumValue);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "File reading error.", "Hata", JOptionPane.ERROR_MESSAGE);
        }

        comboBoxBolum.removeAllItems();

        for (String bolumValue : uniqueBolumValues) {
            comboBoxBolum.addItem(bolumValue);
        }
    }

    private void fillComboBoxDonem() {
        String selectedBolum = (String) comboBoxBolum.getSelectedItem();

        Set<String> uniqueDonemValues = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("dersKayit.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4 && selectedBolum.equals(parts[3].trim())) {
                    String donemValue = parts[2].trim();
                    if (!donemValue.isEmpty()) {
                        uniqueDonemValues.add(donemValue);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Dosya okuma hatası.", "Hata", JOptionPane.ERROR_MESSAGE);
        }

        comboBoxDonem.removeAllItems();

        for (String donemValue : uniqueDonemValues) {
            comboBoxDonem.addItem(donemValue);
        }
    }

    private void fillComboBoxDers() {
        // Seçilen bolum ve donem
        String selectedBolum = (String) comboBoxBolum.getSelectedItem();
        String selectedDonem = (comboBoxDonem.getSelectedItem() != null) ? comboBoxDonem.getSelectedItem().toString() : "";

        Set<String> uniqueDersValues = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("dersKayit.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4 && selectedBolum.equals(parts[3].trim()) && selectedDonem.equals(parts[2].trim())) {
                    String dersValue = parts[0].trim();
                    if (!dersValue.isEmpty()) {
                        uniqueDersValues.add(dersValue);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Dosya okuma hatası.", "Hata", JOptionPane.ERROR_MESSAGE);
        }


        comboBoxDers.removeAllItems();


        for (String dersValue : uniqueDersValues) {
            comboBoxDers.addItem(dersValue);
        }
    }





    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            OgrenciFormu ogrenciFormu = new OgrenciFormu();
            ogrenciFormu.fillComboBoxDers();
        });
    }
}
