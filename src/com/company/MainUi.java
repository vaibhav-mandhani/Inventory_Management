package com.company;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.Vector;


public class MainUi {
    private JTable table1;
    private JFrame jFrame;
    private JLabel heading;
    private JTextField textField1;
    private JTextField textField2;
    private JButton ADDITEMButton;
    private JButton DELETEITEMButton;
    private JButton UPDATEITEMButton;
    private JComboBox comboBox1;
    private JPanel mainPanel;
    private JLabel totalSum;

    public MainUi() {
        tableData();
        totalSum.setText(String.valueOf(total()));



        ADDITEMButton.addActionListener(new ActionListener() {

            @Override

            public void actionPerformed(ActionEvent e) {

                if(textField1.equals("")||textField2.equals("")){
                    JOptionPane.showMessageDialog(mainPanel,"Please Fill Both Fields.");
                }else{
//                    String[] data ={String.valueOf(counter),textField1.getText(),comboBox1.getSelectedIndex()==0?"1":""+comboBox1.getSelectedIndex(),textField2.getText()};
//                    DefaultTableModel defaultTableModel = (DefaultTableModel)table1.getModel();
//                    defaultTableModel.addRow(data);
                    try {

                        String sql = "insert into inventoryList"+"(Id,ItemName,Quantity,Price)"+"values (?,?,?,?)";


                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/inventory","root","root");
                        PreparedStatement statement = connection.prepareStatement(sql);



//                        ResultSet rs = statement.executeQuery("Select* from inventoryList");
//                        rs.last();
//                        counter=counter+rs.getRow();
                        statement.setInt(1,count()+1);
                        statement.setString(2,textField1.getText());
                        statement.setString(3,""+comboBox1.getSelectedItem());
                        statement.setString(4,textField2.getText());

                        statement.executeUpdate();

                        JOptionPane.showMessageDialog(mainPanel,"ITEM ADDED SUCCESSFULLY");
                        textField1.setText("");
                        textField2.setText("");
                    }catch (Exception ex){
//                        JOptionPane.showMessageDialog(null,ex.getMessage());
                        System.out.println(ex);
                    }
                    tableData();
                    totalSum.setText(String.valueOf(total()));

                }
            }
        });
        UPDATEITEMButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            try{
                DefaultTableModel dm = (DefaultTableModel)table1.getModel();
                int selectedRow = table1.getSelectedRow();
                int a=Integer.parseInt(dm.getValueAt(selectedRow,0).toString());
                String sql = "UPDATE inventoryList " +
                        "SET ItemName = '"+textField1.getText()+"',Price='"+textField2.getText()+"',Quantity='"+comboBox1.getSelectedItem()+"' WHERE Id="+a;
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/inventory","root","root");
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.executeUpdate();
                JOptionPane.showMessageDialog(null,"Updated successfully");

            }catch (Exception e2){
                System.out.println(e2);
            }
            tableData();
                totalSum.setText(String.valueOf(total()));


            }
        });
        DELETEITEMButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try{
                    DefaultTableModel dm = (DefaultTableModel)table1.getModel();
                    int selectedRow = table1.getSelectedRow();
                    int a=Integer.parseInt(dm.getValueAt(selectedRow,0).toString());
                    String b = "Delete from inventoryList where Id ="+a;
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/inventory","root","root");
                    PreparedStatement statement = connection.prepareStatement(b);
//                    statement.setInt(1,selectedRow);
//                    statement.setString(2,textField1.getText());
                    statement.executeUpdate();



                    JOptionPane.showMessageDialog(null,"deleted successfully");
                    tableData();


                }catch (Exception e1){
//                    JOptionPane.showMessageDialog(null,e1.getMessage());
                    System.out.println(e1);
                }


                totalSum.setText(String.valueOf(total()));
            }
        });
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DefaultTableModel dm = (DefaultTableModel)table1.getModel();
                int selectedRow = table1.getSelectedRow();
                textField1.setText(dm.getValueAt(selectedRow,1).toString());
                textField2.setText(dm.getValueAt(selectedRow,3).toString());

            }
        });
    }

    public JPanel getPanel() {
        return mainPanel;
    }
    public int count(){
        int counter = 0;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/inventory","root","root");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("Select Id from inventoryList");
            while (rs.next()){
                counter= rs.getInt(1);
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return counter;
    }
    public int total(){
        int sum=0;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/inventory","root","root");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("Select Price,Quantity from inventoryList");
            while (rs.next()){
                sum=sum+ (Integer.parseInt(rs.getString(1)))*(Integer.parseInt(rs.getString(2)));
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return sum;
    }



    public void tableData() {
        try{
            String a= "Select* from inventoryList";
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/inventory","root","root");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(a);
//            table1.setModel(new DefaultTableModel(null, new String[]{"ID", "ITEM NAME", "QUANTITY", "PRICE"}));
            table1.setModel(buildTableModel(rs));

        }catch (Exception ex1){
            JOptionPane.showMessageDialog(null,ex1.getMessage());
        }

    }

    public static DefaultTableModel buildTableModel(ResultSet rs)
            throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<String> columnNames = new Vector<String>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) {

            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);

    }
}
