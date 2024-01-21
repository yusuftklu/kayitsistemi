import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class DersFormu extends JFrame {
    private JPanel DersPaneli;
    private JTextField textDersAdi;
    private JTextField textDersKodu;
    private JComboBox<String> comboBoxDonem;
    private JComboBox<String> comboBoxDersBolum;
    private JComboBox<String> comboBoxOgretimGorevlisi;
    private JTextField textField3;
    private JList<String> list1;
    private JLabel DersAdi;
    private JLabel DersKodu;
    private JLabel Donem;
    private JLabel DersBolum;
    private JLabel DersOgretimGorevlisi;
    private JLabel arama;
    private JButton KaydetDers;
    private JButton button1;
    private Ders ders;

    public DersFormu() {
        setTitle("Ders Sayfası");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);
        setVisible(true);
        setContentPane(DersPaneli);

        fillComboBoxOgretimGorevlisi();

        ders = new Ders();

        KaydetDers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateInputs()) {
                    kaydetDers();
                    clearForm();
                }
                else {
                    JOptionPane.showMessageDialog(DersFormu.this, "Lütfen tüm alanları doldurun ve onay kutusunu işaretleyin.", "Hata", JOptionPane.ERROR_MESSAGE);
                }
                fillList1WithDersData();
            }
        });
        fillList1WithDersData();
        textField3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterListByTextField();
            }
        });

        comboBoxDersBolum.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedDersBolum = (String) comboBoxDersBolum.getSelectedItem();
                fillComboBoxOgretimGorevlisiBasedOnComboBoxDersBolum(selectedDersBolum);
            }
        });
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Menu menu = new Menu();
                menu.setVisible(true);
                dispose();
            }
        });
    }

    private void clearForm() {
        // Formdaki giriş alanlarını temizle
        textDersAdi.setText("");
        textDersKodu.setText("");

    }

    private void fillList1WithDersData() {
        DefaultListModel<String> listModel = new DefaultListModel<>();

        // Dosyadan veriyi oku ve list modeline ekle
        String existingData = readExistingData();
        String[] lines = existingData.split("\n");
        for (String line : lines) {
            listModel.addElement(line);
        }

        // List modelini JList'e ayarla
        list1.setModel(listModel);
    }

    private void filterListByTextField() {
        DefaultListModel<String> listModel = new DefaultListModel<>();

        // Dosyadan veriyi oku
        String existingData = readExistingData();
        String[] lines = existingData.split("\n");

        // textField3'e girilen değeri al
        String filterText = textField3.getText().trim().toLowerCase();

        // Eğer textField3 boşsa, tüm verileri listeye ekle
        if (filterText.isEmpty()) {
            for (String line : lines) {
                listModel.addElement(line);
            }
        }
        else {
            // Verileri filtrele ve list modeline ekle
            for (String line : lines) {
                if (line.toLowerCase().contains(filterText)) {
                    listModel.addElement(line);
                }
                else if (filterText.equals("donem") && line.split(",").length > 2) {
                    listModel.addElement(line.split(",")[2]);
                } else if (filterText.equals("ders kodu") && line.split(",").length > 1) {
                    listModel.addElement(line.split(",")[1]);
                } else if (filterText.equals("bolum") && line.split(",").length > 3) {
                    listModel.addElement(line.split(",")[3]);
                } else if (filterText.equals("ogretim görevlisi") && line.split(",").length > 4) {
                    listModel.addElement(line.split(",")[4]);
                }
                else if (filterText.equals("ders adı") && line.split(",").length > 0) {
                    listModel.addElement(line.split(",")[0]);
                }
            }
        }

        // List modelini JList'e ayarla
        list1.setModel(listModel);
    }

    private String readExistingData() {  // Var olan veriyi dosyadan okuyan metot.
        Path filePath = Paths.get("dersKayit.csv");
        if (Files.exists(filePath)) {
            try {
                return new String(Files.readAllBytes(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    private void writeDataToFile(String data) { // Veriyi dosyaya yazan metot.
        Path filePath = Paths.get("dersKayit.csv");
        try {
            Files.write(filePath, data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Dosya yazma hatası.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
    private Set<String> addedOgretimGorevlisi = new HashSet<>();

    // Kullanıcı girişlerini doğrulayan metot.
    private boolean validateInputs() {
        if (textDersAdi.getText().isEmpty() ||
                textDersKodu.getText().isEmpty()) {
            return false;
        }
        return true;
    }

    // Ders bilgilerini dosyaya kaydeden metot.
    private void kaydetDers() {
        String dersAdi = textDersAdi.getText().trim();
        String dersBolum = (String) comboBoxDersBolum.getSelectedItem();
        String dersKodu = textDersKodu.getText().trim();
        String dersDonem = (String) comboBoxDonem.getSelectedItem();
        String selecteddersOgretimGorevlisi = (String) comboBoxOgretimGorevlisi.getSelectedItem();



        ders.setDersAdi(dersAdi);
        ders.setDersKodu(dersKodu);
        ders.setDersDonemi(dersDonem);
        ders.setDersBolum(dersBolum);
        ders.setOgretimGorevlisi(selecteddersOgretimGorevlisi);


        String dersVerisi = ders.getDersAdi() + "," + ders.getDersKodu() + "," + ders.getDersDonemi() + "," + ders.getDersBolum() + "," + ders.getOgretimGorevlisi() ;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("dersKayit.csv", true))) {
            writer.write(dersVerisi);
            writer.newLine();

            JOptionPane.showMessageDialog(this, "Ders bilgileri kaydedilmiştir.", "Bilgi", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Dosya yazma hatası.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void fillComboBoxOgretimGorevlisi() {
        try (BufferedReader reader = new BufferedReader(new FileReader("ogretimGorevlisi.csv"))) {
            Set<String> addedValues = new HashSet<>(); // Aynı değerleri kontrol etmek için bir küme oluştur

            String header = reader.readLine();
            String[] headers = header.split(",");
            if (headers.length > 0 && headers.length > 1) {
                String concatenatedValue = headers[0].trim() + " " + headers[1].trim();
                if (addedValues.add(concatenatedValue)) {
                    comboBoxOgretimGorevlisi.addItem(concatenatedValue);
                }
            }
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 3) {
                    String dersOgretimGorevlisi = parts[0].trim() + " " + parts[1].trim();
                    if (addedValues.add(dersOgretimGorevlisi)) { // Eğer değer daha önce eklenmemişse ekle
                        comboBoxOgretimGorevlisi.addItem(dersOgretimGorevlisi);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Dosya okuma hatası.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void fillComboBoxOgretimGorevlisiBasedOnComboBoxDersBolum(String selectedDersBolum) {
        comboBoxOgretimGorevlisi.removeAllItems(); // Clear existing items

        try (BufferedReader reader = new BufferedReader(new FileReader("ogretimGorevlisi.csv"))) {
            Set<String> addedValues = new HashSet<>();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 3 && parts[3].trim().equals(selectedDersBolum.trim())) {
                    String dersOgretimGorevlisi = parts[0].trim() + " " + parts[1].trim();
                    if (addedValues.add(dersOgretimGorevlisi)) {
                        comboBoxOgretimGorevlisi.addItem(dersOgretimGorevlisi);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Dosya okuma hatası.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
    public static void main(String[] args) {
        new DersFormu();
    }

}
