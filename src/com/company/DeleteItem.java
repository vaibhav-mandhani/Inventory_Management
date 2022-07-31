package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class DeleteItem extends MainUi{
    private JPanel deletePanel;
    private JButton DELETEITEMButton;
    private JLabel idLable;
    private JLabel itemLabel;
    private JTextField delItem;
    private JTextField delId;

    public DeleteItem() {
        DELETEITEMButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {



            }
        });
    }

    public JPanel getDeletePanel(){
        return deletePanel;
    }
}
