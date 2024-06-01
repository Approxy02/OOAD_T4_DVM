package org.DVM.UI;

import org.DVM.Control.Communication.OtherDVM;
import org.DVM.Stock.Item;
import org.DVM.Stock.Stock;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.Console;
import java.util.ArrayList;

public class UIManager extends JFrame {
    String title = "Distributed Vending Machine";

    //region <UI 객체 정의>

    private class UIPanel extends JPanel{
        public UIPanel(){
            super(new BorderLayout());

            // Title Panel
            JPanel titlePanel = new JPanel();
            JLabel titleLabel = new JLabel(title, JLabel.CENTER);
            
            titleLabel.setFont(new Font(titleLabel.getFont().getFontName(), Font.BOLD, 18));
            titlePanel.add(titleLabel);

            this.add(titlePanel, BorderLayout.NORTH);
        }
    }

    private class UITextField extends JTextField {
        private final Document document = super.getDocument();
        private final Document placeHolderDocument = new PlainDocument();

        private String placeHolder;

        @Override
        public String getText() {
            try {
                return this.document.getText(0, this.document.getLength());
            } catch (BadLocationException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void setText(String text) {
            try {

                this.document.remove(0, this.document.getLength());
                this.document.insertString(0, text, null);

            } catch (BadLocationException e) {
                throw new RuntimeException(e);
            }
        }

        public String getPlaceHolder() { return this.placeHolder; }

        public void setPlaceHolder(String placeHolder) {
            this.placeHolder = placeHolder;

            try {

                this.placeHolderDocument.remove(0, this.placeHolderDocument.getLength());
                this.placeHolderDocument.insertString(0, placeHolder, null);

            } catch (BadLocationException e) {
                throw new RuntimeException(e);
            }
        }

        private void showPlaceHolder() {
            super.setDocument(this.placeHolderDocument);

            this.setForeground(Color.GRAY);
        }

        private void hidePlaceHolder() {
            super.setDocument(this.document);

            this.setForeground(Color.BLACK);
        }

        public UITextField() {
            this("", "");
        }

        public UITextField(String text) {
            this(text, "");
        }

        public UITextField(String text, String placeHolder) {
            setText(text); setPlaceHolder(placeHolder);

            if(this.document.getLength() == 0) {
                this.showPlaceHolder();
            }
            else{
                this.hidePlaceHolder();
            }

            this.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    hidePlaceHolder();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if(document.getLength() == 0) showPlaceHolder();
                }
            });
        }

        public void setRegexFilter(String regex) {
            ((AbstractDocument)this.document).setDocumentFilter(new DocumentFilter() {
                @Override
                public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                    if (string == null) {
                        return;
                    }

                    if (string.matches(regex)) {
                        super.insertString(fb, offset, string, attr);
                    }
                }

                @Override
                public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                    if (text == null) {
                        return;
                    }

                    if (text.matches(regex)) {
                        super.replace(fb, offset, length, text, attrs);
                    }
                }

                @Override
                public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                    super.remove(fb, offset, length);
                }
            });
        }

        public void setNumericOnly(){
            this.setRegexFilter("\\d+");
        }
    }

    //endregion

    private final CardLayout layout;

    private final JPanel showPanel;

    private void showUI(String name){
        layout.show(showPanel, name);
    }

    public UIManager() {
        setTitle(title);
        setLocationRelativeTo(null);
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        layout = new CardLayout();

        showPanel = new JPanel(layout);

        // Add all panels
        showPanel.add(createMainPanel(), "MainPanel");
        showPanel.add(createPaymentPanel1(), "PaymentPanel1");
        showPanel.add(createPaymentPanel2(), "PaymentPanel2");
        showPanel.add(createPrePaymentPanel1(), "PrePaymentPanel1");
        showPanel.add(createPrePaymentPanel2(), "PrePaymentPanel2");
        showPanel.add(createVerificationCodePanel(), "VerificationCodePanel");
        showPanel.add(createLocationInfoPanel(), "LocationInfoPanel");
        showPanel.add(createDispenseResultPanel(), "DispenseResultPanel");

        add(showPanel);

        setVisible(true);
    }

    public void display(String UItype, ArrayList<Item> items, Item item, OtherDVM dvm, String vCode) {
        switch (UItype) {
            case "MainUI":
                mainUIdisplay(items);
                break;
            case "PaymentUI-1":
                payUI_1(item);
                break;
            case "PaymentUI-2":
                payUI_2(item);
                break;
            case "PrepaymentUI-1":
                prepayUI_1(item);
                break;
            case "PrepaymentUI-2":
                prepayUI_2(item);
                break;
            case "LocationInfoUI":
                locationInfoUI(item, dvm);
                break;
            case "VerificationCodeDisplayUI":
                vCodeUI(item);
                break;
            case "DispenseResultUI":
                dispenseUI(item);
                break;
            default:
                break;
        }
    }
    
    private String mainDisplayString = null;
    private ArrayList<Item> items = null;
    private Item item = new Item("null", 0, 0, 0);
    private JLabel categoryValue;
    private JLabel quantityValue;
    private JLabel categoryValue1;
    private JLabel quantityValue1;
    private JLabel categoryValue2;
    private JLabel quantityValue2;
    private JLabel nameValue;
    private JLabel locationValue;
    private void mainUIdisplay(ArrayList<Item> items) {
        this.items = items;
        showUI("MainPanel");
    }

    private void payUI_1(Item item) {
        System.out.println("payUI_1");
        this.item = item;
        categoryValue.setText(item.name + "(" + item.code + ")");
        quantityValue.setText(String.valueOf(item.quantity));
        showUI("PaymentPanel1");
    }

    private void payUI_2(Item item) {
        System.out.println("payUI_2");
        categoryValue1.setText(item.name + "(" + item.code + ")");
        quantityValue1.setText(String.valueOf(item.quantity));
        showUI("PaymentPanel2");
    }

    private String prepayUI_1(Item item) {
        return "";
    }

    private void prepayUI_2(Item item) {
    }

    private void locationInfoUI(Item item, OtherDVM dvm) {
        System.out.println("locationInfoUI");
        nameValue.setText(dvm.name);
        locationValue.setText("(" + dvm.coor_x + ", " + dvm.coor_y + ")");
        showUI("LocationInfoPanel");
    }

    private void vCodeUI(Item item) {
    }

    private void dispenseUI(Item item) {
        System.out.println("dispenseUI");
        categoryValue2.setText(item.name + "(" + item.code + ")");
        quantityValue2.setText(String.valueOf(item.quantity));
        showUI("DispenseResultPanel");
    }


    private JPanel createMainPanel() {
        UIPanel panel = new UIPanel();

        // Item Grid Panel
        JPanel itemGridPanel = new JPanel();
        itemGridPanel.setLayout(new GridLayout(4, 5));
        Stock stock = new Stock();
        ArrayList<Item> items = stock.itemList();
//        String[] itemNames = {
//                "콜라(01)", "사이다(02)", "녹차(03)", "홍차(04)", "밀크티(05)",
//                "탄산수(06)", "보리차(07)", "캔커피(08)", "물(09)", "에너지드링크(10)",
//                "유자차(11)", "식혜(12)", "아이스티(13)", "딸기주스(14)", "오렌지주스(15)",
//                "포도주스(16)", "이온음료(17)", "아메리카노(18)", "핫초코(19)", "카페라떼(20)"
//        };

        for (Item item : items) {
            String item_name = item.name + "(" + item.code + ")";
            JLabel itemLabel = new JLabel(item_name, JLabel.CENTER);
            itemLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            itemGridPanel.add(itemLabel);
        }
        panel.add(itemGridPanel, BorderLayout.CENTER);

        // Input Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 4, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        UITextField categoryField = new UITextField("", "종류(코드입력)");

        categoryField.setNumericOnly();

        UITextField quantityField = new UITextField("", "수량(0~99)");
        quantityField.setNumericOnly();


        JButton purchaseButton = new JButton("구매하기");

        UITextField verificationCodeField = new UITextField("", "인증코드");

        JButton prePaymentCodeButton = new JButton("선결제 인증코드");

        inputPanel.add(categoryField);
        inputPanel.add(quantityField);
        inputPanel.add(purchaseButton);
        inputPanel.add(new JLabel()); // 빈 칸
        inputPanel.add(verificationCodeField);
        inputPanel.add(prePaymentCodeButton);

        panel.add(inputPanel, BorderLayout.SOUTH);

        // Action Listeners
        purchaseButton.addActionListener(e -> {
            mainDisplayString = categoryField.getText();
            mainDisplayString += " " + quantityField.getText();
            synchronized (UIManager.this) {
                UIManager.this.notify(); // Notify waiting thread
            }
        });

        prePaymentCodeButton.addActionListener(e -> {
            // Pre-payment code action
            mainDisplayString = verificationCodeField.getText();
            if (mainDisplayString.length() != 10) {
                JOptionPane.showMessageDialog(null, "인증코드는 10자리여야 합니다.");
            } else {
                synchronized (UIManager.this) {
                    UIManager.this.notify(); // Notify waiting thread
                }
            }
        });

        return panel;
    }

    private JPanel createPaymentPanel1() {
        UIPanel panel = new UIPanel();

        // Item Info Panel
        JPanel itemInfoPanel = new JPanel();
        itemInfoPanel.setLayout(new GridLayout(2, 2));
        itemInfoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        System.out.println("item: " + item);

        JLabel categoryLabel = new JLabel("종류", JLabel.CENTER);
        categoryLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        String item_name = item.name + "(" + item.code + ")";
        System.out.println("item_name: " + item_name);
        categoryValue = new JLabel("item_name", JLabel.CENTER);

        JLabel quantityLabel = new JLabel("수량", JLabel.CENTER);
        quantityLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        quantityValue = new JLabel("5", JLabel.CENTER);

        itemInfoPanel.add(categoryLabel);
        itemInfoPanel.add(categoryValue);
        itemInfoPanel.add(quantityLabel);
        itemInfoPanel.add(quantityValue);

        panel.add(itemInfoPanel, BorderLayout.CENTER);

        // Payment Panel
        JPanel paymentPanel = new JPanel();
        paymentPanel.setLayout(new GridLayout(3, 1, 10, 10));
        paymentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel cardNumberLabel = new JLabel("카드 번호를 입력해주세요.");
        UITextField cardNumberField = new UITextField("", "카드번호");
        cardNumberField.setNumericOnly();

        JButton paymentButton = new JButton("결제");

        paymentPanel.add(cardNumberLabel);
        paymentPanel.add(cardNumberField);
        paymentPanel.add(paymentButton);

        panel.add(paymentPanel, BorderLayout.SOUTH);

        paymentButton.addActionListener(e -> {
            // Handle payment
//            paymentButton.setEnabled(false);
//            cardNumberField.setEnabled(false);

            mainDisplayString = cardNumberField.getText();
            synchronized (UIManager.this) {
                UIManager.this.notify(); // Notify waiting thread
            }
        });

        return panel;
    }

    private JPanel createPaymentPanel2() {
        UIPanel panel = new UIPanel();

        // Item Info Panel
        JPanel itemInfoPanel = new JPanel();
        itemInfoPanel.setLayout(new GridLayout(2, 2));
        itemInfoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel categoryLabel = new JLabel("종류", JLabel.CENTER);
        categoryLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        categoryValue1 = new JLabel("콜라(01)", JLabel.CENTER);

        JLabel quantityLabel = new JLabel("수량", JLabel.CENTER);
        quantityLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        quantityValue1 = new JLabel("5", JLabel.CENTER);

        itemInfoPanel.add(categoryLabel);
        itemInfoPanel.add(categoryValue1);
        itemInfoPanel.add(quantityLabel);
        itemInfoPanel.add(quantityValue1);

        panel.add(itemInfoPanel, BorderLayout.CENTER);

        // Confirmation Panel
        JPanel confirmationPanel = new JPanel();
        confirmationPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        confirmationPanel.setLayout(new GridLayout(2, 1, 10, 10));

        JLabel confirmationLabel = new JLabel("결제가 완료되었습니다!", JLabel.CENTER);
        JButton receiveButton = new JButton("음료 받기");

        confirmationPanel.add(confirmationLabel);
        confirmationPanel.add(receiveButton);

        panel.add(confirmationPanel, BorderLayout.SOUTH);

        receiveButton.addActionListener(e -> {
                    synchronized (UIManager.this) {
                        UIManager.this.notify(); // Notify waiting thread
                    }
                }
        );

        return panel;
    }

    private JPanel createPrePaymentPanel1() {
        UIPanel panel = new UIPanel();

        // Item Info Panel
        JPanel itemInfoPanel = new JPanel();
        itemInfoPanel.setLayout(new GridLayout(3, 2));
        itemInfoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel categoryLabel = new JLabel("종류", JLabel.CENTER);
        categoryLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JLabel categoryValue = new JLabel("콜라(01)", JLabel.CENTER);

        JLabel quantityLabel = new JLabel("수량", JLabel.CENTER);
        quantityLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JLabel quantityValue = new JLabel("5", JLabel.CENTER);

        JLabel locationLabel = new JLabel("위치", JLabel.CENTER);
        locationLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JLabel locationValue = new JLabel("(5, 5)", JLabel.CENTER);

        itemInfoPanel.add(categoryLabel);
        itemInfoPanel.add(categoryValue);
        itemInfoPanel.add(quantityLabel);
        itemInfoPanel.add(quantityValue);
        itemInfoPanel.add(locationLabel);
        itemInfoPanel.add(locationValue);

        panel.add(itemInfoPanel, BorderLayout.CENTER);

        // Payment Panel
        JPanel paymentPanel = new JPanel();
        paymentPanel.setLayout(new GridLayout(3, 1, 10, 10));
        paymentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel cardNumberLabel = new JLabel("카드 번호를 입력해주세요.");
        UITextField cardNumberField = new UITextField("", "카드번호");
        JButton paymentButton = new JButton("결제");

        paymentPanel.add(cardNumberLabel);
        paymentPanel.add(cardNumberField);
        paymentPanel.add(paymentButton);

        panel.add(paymentPanel, BorderLayout.SOUTH);

        paymentButton.addActionListener(e -> {
            // Handle payment
            paymentButton.setEnabled(false);
            cardNumberField.setEnabled(false);
            JOptionPane.showMessageDialog(this, "결제중...");
            // Simulate payment delay
            Timer timer = new Timer(2000, evt -> {
                paymentButton.setEnabled(true);
                cardNumberField.setEnabled(true);
                JOptionPane.showMessageDialog(this, "결제가 완료되었습니다!");
                showUI("PrePaymentPanel2");
            });
            timer.setRepeats(false);
            timer.start();
        });

        return panel;
    }

    private JPanel createPrePaymentPanel2() {
        UIPanel panel = new UIPanel();

        // Item Info Panel
        JPanel itemInfoPanel = new JPanel();
        itemInfoPanel.setLayout(new GridLayout(3, 2));
        itemInfoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel categoryLabel = new JLabel("종류", JLabel.CENTER);
        categoryLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JLabel categoryValue = new JLabel("콜라(01)", JLabel.CENTER);

        JLabel quantityLabel = new JLabel("수량", JLabel.CENTER);
        quantityLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JLabel quantityValue = new JLabel("5", JLabel.CENTER);

        JLabel locationLabel = new JLabel("위치", JLabel.CENTER);
        locationLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JLabel locationValue = new JLabel("(5, 5)", JLabel.CENTER);

        itemInfoPanel.add(categoryLabel);
        itemInfoPanel.add(categoryValue);
        itemInfoPanel.add(quantityLabel);
        itemInfoPanel.add(quantityValue);
        itemInfoPanel.add(locationLabel);
        itemInfoPanel.add(locationValue);

        panel.add(itemInfoPanel, BorderLayout.CENTER);

        // Confirmation Panel
        JPanel confirmationPanel = new JPanel();
        confirmationPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        confirmationPanel.setLayout(new GridLayout(2, 1, 10, 10));

        JLabel confirmationLabel = new JLabel("결제가 완료되었습니다!", JLabel.CENTER);
        JButton receiveCodeButton = new JButton("인증 코드 받기");

        confirmationPanel.add(confirmationLabel);
        confirmationPanel.add(receiveCodeButton);

        panel.add(confirmationPanel, BorderLayout.SOUTH);

        receiveCodeButton.addActionListener(e -> showUI("MainPanel"));

        return panel;
    }

    private JPanel createVerificationCodePanel() {
        UIPanel panel = new UIPanel();

        // Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(3, 2));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel nameLabel = new JLabel("DVM 이름", JLabel.CENTER);
        nameLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JLabel nameValue = new JLabel("Team5", JLabel.CENTER);

        JLabel locationLabel = new JLabel("위치", JLabel.CENTER);
        locationLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JLabel locationValue = new JLabel("(x, y)", JLabel.CENTER);

        JLabel codeLabel = new JLabel("인증코드", JLabel.CENTER);
        codeLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JLabel codeValue = new JLabel("AD13254D33", JLabel.CENTER);

        infoPanel.add(nameLabel);
        infoPanel.add(nameValue);
        infoPanel.add(locationLabel);
        infoPanel.add(locationValue);
        infoPanel.add(codeLabel);
        infoPanel.add(codeValue);

        panel.add(infoPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        buttonPanel.setLayout(new GridLayout(1, 1, 10, 10));

        JButton backButton = new JButton("돌아가기");
        buttonPanel.add(backButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        backButton.addActionListener(e -> showUI("MainPanel"));

        return panel;
    }

    private JPanel createLocationInfoPanel() {
        UIPanel panel = new UIPanel();

        // Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(2, 2));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel nameLabel = new JLabel("DVM 이름", JLabel.CENTER);
        nameLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        nameValue = new JLabel("Team5", JLabel.CENTER);

        JLabel locationLabel = new JLabel("위치", JLabel.CENTER);
        locationLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        locationValue = new JLabel("(x, y)", JLabel.CENTER);

        infoPanel.add(nameLabel);
        infoPanel.add(nameValue);
        infoPanel.add(locationLabel);
        infoPanel.add(locationValue);

        panel.add(infoPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        buttonPanel.setLayout(new GridLayout(1, 1, 10, 10));

        JButton nextButton = new JButton("다음");
        buttonPanel.add(nextButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        nextButton.addActionListener(e -> showUI("MainPanel"));

        return panel;
    }

    private JPanel createDispenseResultPanel() {
        UIPanel panel = new UIPanel();

        // Item Info Panel
        JPanel itemInfoPanel = new JPanel();
        itemInfoPanel.setLayout(new GridLayout(2, 2));
        itemInfoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel categoryLabel = new JLabel("종류", JLabel.CENTER);
        categoryLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        categoryValue2 = new JLabel("콜라(01)", JLabel.CENTER);

        JLabel quantityLabel = new JLabel("수량", JLabel.CENTER);
        quantityLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        quantityValue2 = new JLabel("5", JLabel.CENTER);

        itemInfoPanel.add(categoryLabel);
        itemInfoPanel.add(categoryValue2);
        itemInfoPanel.add(quantityLabel);
        itemInfoPanel.add(quantityValue2);

        panel.add(itemInfoPanel, BorderLayout.CENTER);

        // Confirmation Panel
        JPanel confirmationPanel = new JPanel();
        confirmationPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        confirmationPanel.setLayout(new GridLayout(2, 1, 10, 10));

        JLabel confirmationLabel = new JLabel("음료를 수령하세요!", JLabel.CENTER);
        JButton completeButton = new JButton("완료");

        confirmationPanel.add(confirmationLabel);
        confirmationPanel.add(completeButton);

        panel.add(confirmationPanel, BorderLayout.SOUTH);

        completeButton.addActionListener(e -> {
            synchronized (UIManager.this) {
                UIManager.this.notify(); // Notify waiting thread
            }
        });

        return panel;
    }

    public synchronized String returnString(String s) {
        mainDisplayString = s;

        return mainDisplayString;
    }

    public synchronized String waitForInputString() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mainDisplayString;
    }

    public synchronized Item waitForInput() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return item;
    }
}
