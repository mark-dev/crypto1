package ru.study.gui;


import ru.study.crypto.AESCipher;
import ru.study.crypto.CryptoWrapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;


/**
 * Created by Mark
 */
public class MainFrame extends JFrame {
    private boolean isDecryptWasLast = false;
    private byte[] lastBytes;

    private MainFrame() {
        setTitle("comporator");
        initGUI();
        initCryptoWrapper();
    }

    private void initCryptoWrapper() {
        cryptoWrapper = new CryptoWrapper(new AESCipher());
    }

    private void initGUI() {
        Color lightBlue = new Color(51, 204, 255);   // light blue
        Color lightYellow = new Color(255, 255, 215);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //init
        panelMain = new JPanel();
        panelBottom = new JPanel();
        buttonsVerticalPanel = new JPanel();
        utilsVerticalPanel = new JPanel();
        buttonEncrypt = new JButton("encrypt");
        buttonDecrypt = new JButton("decrypt");
        buttonFromFile = new JButton("open file");
        buttonRoll = new JButton("<- flip");
        buttonGetKey = new JButton("key?");
        buttonSaveResult = new JButton("save");
        isInputHexCheckBox = new JCheckBox();
        isOutputHexCheckBox = new JCheckBox();
        isInputHexCheckBox.setText("hex");
        isOutputHexCheckBox.setText("hex");
        isOutputHexCheckBox.setSelected(true);
        fieldInput = new JTextArea("");
        fieldResult = new JTextArea("");
        fieldResult.setEditable(false);
        //set bg Color

        panelMain.setBackground(lightBlue);
        buttonEncrypt.setBackground(lightBlue);
        buttonsVerticalPanel.setBackground(lightBlue);
        utilsVerticalPanel.setBackground(lightBlue);
        panelBottom.setBackground(lightBlue);
        //preffered size
        fieldInput.setPreferredSize(new Dimension(120, 40));
        fieldResult.setPreferredSize(new Dimension(120, 40));
        //layouts
        setLayout(new BorderLayout());
        buttonsVerticalPanel.setLayout(new GridLayout(3, 1));
        utilsVerticalPanel.setLayout(new GridLayout(3, 1));
        panelMain.setLayout(new FlowLayout());
        // action listeners
        buttonEncrypt.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                buttonEncryptActionPefrormed();
            }
        });
        buttonRoll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonRollActionPefrormed();
            }
        });
        buttonDecrypt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttonDecryptActionPerformed();
            }
        });
        buttonFromFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttonFromFileActionPerformed();
            }
        });
        buttonSaveResult.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttonSaveResultActionPerformed();
            }
        });
        buttonGetKey.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttonGetKeyActionPefrormed();

            }
        });
        isInputHexCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                formatInput(isInputHexCheckBox.isSelected());
            }
        });
        isOutputHexCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                formatResult(isOutputHexCheckBox.isSelected());
            }
        });
        // add components to panels

        buttonsVerticalPanel.add(buttonEncrypt);
        buttonsVerticalPanel.add(buttonDecrypt);
        buttonsVerticalPanel.add(buttonFromFile);
        panelMain.add(buttonsVerticalPanel);
        utilsVerticalPanel.add(isInputHexCheckBox);
        utilsVerticalPanel.add(buttonRoll);
        utilsVerticalPanel.add(buttonGetKey);
        panelMain.add(utilsVerticalPanel);
        panelMain.add(fieldInput);
        panelMain.add(fieldInput);
        panelMain.add(fieldResult);
        panelMain.add(isOutputHexCheckBox);
        panelMain.add(buttonSaveResult);

        //add panels to frame
        getContentPane().add(panelMain, BorderLayout.CENTER);
        getContentPane().add(panelBottom, BorderLayout.SOUTH);
        fieldInput.requestFocusInWindow();
        pack();
    }

    private void formatInput(boolean isHex) {
        if (!isHex) {
            fieldInput.setText(new String(cryptoWrapper.toBytes(fieldInput.getText())));
        } else {
            fieldInput.setText(cryptoWrapper.toHex(fieldInput.getText().getBytes()));
        }
    }

    private void buttonGetKeyActionPefrormed() {
        JOptionPane.showMessageDialog(this, "Key: " + cryptoWrapper.getKey());
        repaint();
    }

    private void buttonRollActionPefrormed() {
        boolean isInputwasHex = isInputHexCheckBox.isSelected();
        boolean isOutputwasHex = isOutputHexCheckBox.isSelected();
        fieldInput.setText(fieldResult.getText());
        isInputHexCheckBox.setSelected(isOutputwasHex);
        isOutputHexCheckBox.setSelected(isInputwasHex);
        fieldResult.setText("");
        if (!fieldInput.getText().isEmpty()) {
            buttonDecryptActionPerformed();
        }
    }

    private void formatResult(boolean isHex) {
        try {
            if (!isHex) {
                fieldResult.setText(new String(cryptoWrapper.toBytes(fieldResult.getText()), "UTF-8"));
            } else {
                fieldResult.setText(cryptoWrapper.toHex(fieldResult.getText().getBytes("UTF-8")));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void buttonSaveResultActionPerformed() {
        JFileChooser chooser = new JFileChooser();
        int retrival = chooser.showSaveDialog(null);
        if (retrival == JFileChooser.APPROVE_OPTION) {
            try {
                byte[] bytes = lastBytes;
                System.out.println("Bytes for writing: " + Arrays.toString(bytes));
                BufferedOutputStream bos;
                FileOutputStream fos = new FileOutputStream(chooser.getSelectedFile());
                bos = new BufferedOutputStream(fos);
                bos.write(bytes);
                bos.flush();
                bos.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void buttonFromFileActionPerformed() {

        JFileChooser fileopen = new JFileChooser();
        int ret = fileopen.showDialog(null, "specify file");
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fileopen.getSelectedFile();
            openedFile = file;
            fieldInput.setText("<file: " + file.getName() + " >");
        }
    }


    private void buttonDecryptActionPerformed() {
        isDecryptWasLast = true;
        String result = "";
        if (isFileSelected()) {
            try {
                //Работает только с обычным текстом, надо с хексом - вместо false inputIsHexCheckBox.isSelected()
                lastBytes = cryptoWrapper.decrypt(openedFile, true);
                result = cryptoWrapper.toHex(lastBytes);
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        } else {
            lastBytes = cryptoWrapper.decrypt(fieldInput.getText(), isInputHexCheckBox.isSelected());
            result = cryptoWrapper.toHex(lastBytes);
        }

        formatResult(result);
    }

    //result - hex строка
    private void formatResult(String result) {
        if (isOutputHexCheckBox.isSelected()) {
            fieldResult.setText(result);
        } else {
            fieldResult.setText(new String(cryptoWrapper.toBytes(result)));
        }
    }

    private void buttonEncryptActionPefrormed() {
        isDecryptWasLast = false;
        String result = "";
        if (isFileSelected()) {
            try {
                result = cryptoWrapper.encrypt(openedFile);  //hex
                lastBytes = cryptoWrapper.toBytes(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            lastBytes = cryptoWrapper.encrypt(fieldInput.getText(), isInputHexCheckBox.isSelected());
            result = cryptoWrapper.toHex(lastBytes);
        }
        formatResult(result);
    }

    public static void main(String[] args) throws
            ClassNotFoundException,
            UnsupportedLookAndFeelException,
            InstantiationException,
            IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        new Thread(new Runnable() {
            @Override
            public void run() {
                new MainFrame().setVisible(true);
            }
        }).start();
    }

    private File openedFile;
    //-----------------------
    private JPanel panelMain;
    private JPanel buttonsVerticalPanel;
    private JPanel utilsVerticalPanel;
    private JPanel panelBottom;

    private JButton buttonEncrypt;
    private JCheckBox isInputHexCheckBox;
    private JButton buttonDecrypt;
    private JButton buttonFromFile;
    private JButton buttonRoll;
    private JButton buttonSaveResult;
    private JButton buttonGetKey;
    private JCheckBox isOutputHexCheckBox;
    private JTextArea fieldResult;
    private JTextArea fieldInput;
    private CryptoWrapper cryptoWrapper;

    public boolean isFileSelected() {
        return fieldInput.getText().startsWith("<file:");
    }
}

