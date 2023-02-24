import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Main {
    private JPanel automoviles;
    private JTextField jfid;
    private JTextField jfmarca;
    private JTextField jfmodelo;
    private JTextField jfanio;
    private JButton crear;
    private JButton buscar;
    private JButton actualizar;
    private JButton eliminar;
    private JComboBox comboestado;
    private JComboBox combocombustible;
    private JLabel antena;
    private PreparedStatement ps;

    public Main(){




        crear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection conn;
                try{
                    conn = getConnection();
                    ps = conn.prepareStatement("INSERT INTO vehiculo(id, marca, modelo, anio, tipo_combustible, estado) VALUES(?,?,?,?,?,?)" );
                    try{

                        if( !jfid.getText().matches("[0-9]*") ){
                            throw new SQLException("Ingresa bien los datos del Vehiculo");

                        }
                        else {
                            ps.setString(1, jfid.getText());
                            ps.setString(2, jfmarca.getText());
                            ps.setString(3, jfmodelo.getText());
                            ps.setString(4, jfanio.getText());
                            ps.setString(5, combocombustible.getSelectedItem().toString());
                            ps.setString(6, comboestado.getSelectedItem().toString());
                        }
                    }catch (SQLException es){
                        System.out.println("Error: " + es + "||||");
                        JOptionPane.showMessageDialog(null,"Ingrese bien los datos del Vehiculo");
                    }


                    System.out.println(ps);
                    int res = ps.executeUpdate();

                    if(res > 0){
                        JOptionPane.showMessageDialog(null, "Vehiculo Ingresado en el sistema");
                    }else{
                        JOptionPane.showMessageDialog(null, "El vehiculo No pudo ser Ingresada en el sistema");
                    }
                    conn.close();
                }catch (HeadlessException | SQLException f){
                    System.out.println(f);
                }

            }
        });

        //ELIMINAR
        eliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection conn;
                try {
                    conn = getConnection();
                    ps = conn.prepareStatement("DELETE FROM descripcion WHERE id = ?");
                    ps.setString(1, jfid.getText());
                    int rowsAffected = ps.executeUpdate(); // Cambiar a executeUpdate()
                    conn.close();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "VEHICULO ELIMINADO");
                    } else {
                        JOptionPane.showMessageDialog(null, "ERROR AL ELIMINARC EL VEHICULO");
                    }
                } catch (SQLException ex) {
                    System.out.println("Error: " + ex.getMessage());
                    JOptionPane.showMessageDialog(null, "DATOS INCORRECTOS");
                }
            }
        });
        buscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection conn;
                ResultSet rs;
                try{
                    conn = getConnection();


                    ps = conn.prepareStatement("SELECT * FROM descripcion WHERE id= ?" );
                    ps.setString(1, jfid.getText());

                    rs = ps.executeQuery();


                    try{

                        if( !jfid.getText().matches("[0-9]*") ){
                            throw new SQLException("DATOS INCORRECTOS");
                        }else{
                            if(rs.next()){
                                jfid.setText( Integer.toString(rs.getInt("id")) );
                                jfmarca.setText(rs.getString("marca"));
                                jfmodelo.setText(rs.getString("modelo"));
                                jfanio.setText(rs.getString("anio"));
                                String  Combustible = rs.getString("tipo_combustible");
                                if (Combustible != null) {
                                    combocombustible.setSelectedItem(Combustible);
                                }
                                String estado = rs.getString("estado");
                                if (estado != null) {
                                    comboestado.setSelectedItem(estado);
                                }
                            }else{
                                System.out.println("Error no funciona");
                                JOptionPane.showMessageDialog(null,"DIGITA BIEN LOS DATOS");
                            }
                        }
                    }catch (SQLException es){

                    }
                    conn.close();

                }catch (HeadlessException | SQLException f){
                    System.out.println(f);

                }
            }
        });
        actualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection conn;

                try{
                    conn = getConnection();
                    ps = conn.prepareStatement("UPDATE vehiculo SET id = ?, marca = ?, modelo = ?, anio = ?, tipo_combustible = ?, estado=? WHERE id ="+jfid.getText() );
                    try{

                        if( !jfid.getText().matches("[0-9]*") ){
                            throw new SQLException("Ingresa bien los datos del Vehiculo");

                        }else{
                            ps.setString(1, jfid.getText());
                            ps.setString(2, jfmarca.getText());
                            ps.setString(3, jfmodelo.getText());
                            ps.setString(4, jfanio.getText());
                            ps.setString(5, combocombustible.getSelectedItem().toString());
                            ps.setString(6, comboestado.getSelectedItem().toString());
                        }
                    }catch (SQLException es){
                        System.out.println("Error: " + es + "||||");
                        JOptionPane.showMessageDialog(null,"Ingrese bien los datos del Vehiculo");
                    }


                    System.out.println(ps);
                    int res = ps.executeUpdate();

                    if(res > 0){
                        JOptionPane.showMessageDialog(null, "Vehiculo modificada correctamente");
                    }else{
                        JOptionPane.showMessageDialog(null, "Vehiculo no se pudo modificar");
                    }
                    conn.close();
                }catch (HeadlessException | SQLException f){
                    System.out.println(f);
                }
            }
        });
    }
    public void mostrarDatos(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conexion = DriverManager.getConnection(
                    "jdbc:mysql://localhost/vehiculo","root","Ligacampeon1");

            Statement s = conexion.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM descripcion");

            while(rs.next()){

                JOptionPane.showMessageDialog(null, rs.getInt("id") + "\n" + rs.getString(2)
                        +"\n"+ rs.getString("modelo") +"\n" +
                        rs.getInt("anio") + "\n"+
                        rs.getString(5)+ "\n"+ rs.getString(6));

            }

            conexion.close();
        }catch (Exception ex){
            System.out.println(ex);
            ex.printStackTrace();
        }

    }

    public static Connection getConnection(){
        Connection conn = null;
        String base = "vehiculos";
        String url = "jdbc:mysql://localhost:3306/" + base;
        String user = "root";
        String password = "Hiphop1511@";

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url,user,password);
        }catch (ClassNotFoundException | SQLException ex){
            System.out.printf("Error: " + ex);
        }
        return conn;
    }




    public static void main(String[] args) {
        JFrame frame = new JFrame("VEHICULOS");
        frame.setContentPane(new Main().automoviles);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
