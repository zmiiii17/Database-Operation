import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StudentDB {
    String url = "jdbc:mysql://localhost:3306/campus";
    Connection koneksi;

    public Connection getConnection(String user, String pass) throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }

    public void readStudents() {
        String sql = "SELECT * FROM students";
        try {
            koneksi = getConnection("root", "userr");
            Statement st = koneksi.createStatement();
            ResultSet rs = st.executeQuery(sql);
            System.out.println("NIM\tNAMA\tALAMAT\tGENDER");
            while (rs.next()) {
                String nim = rs.getString(1);
                String name = rs.getString(2);
                String address = rs.getString(3);
                String gender = rs.getString(4);

                System.out.println(nim + "\t" + name + "\t" + address + "\t" + gender);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (koneksi != null) {
                    koneksi.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void insertStudent(String nim, String name, String address, String gender) {
        // if (nim.length() != 11) {
        //     System.out.println("NIM harus terdiri dari 11 digit");
        //     return;
        // }

        // if (!gender.equalsIgnoreCase("L") && !gender.equalsIgnoreCase("P")) {
        //     System.out.println("Gender harus 'L' atau 'P'");
        //     return;
        // }

        boolean valid = true;
        String error = "";

        if (nim.length() != 11) {
            valid = false;
            error += "NIM harus terdiri dari 11 digit\n";
        }

        if (!gender.equalsIgnoreCase("L") && !gender.equalsIgnoreCase("P")) {
            valid = false;
            error += "Gender harus 'L' atau 'P'\n";
        }


        try {
            koneksi = getConnection("root", "userr");
            PreparedStatement ps = koneksi.prepareStatement("SELECT COUNT(*) FROM students WHERE nim = ?");
            ps.setString(1, nim);
            ResultSet rs = ps.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                valid = false;
                error += "NIM sudah ada\n";
                System.out.println(error);
                return;
            }

            if (valid) {
            PreparedStatement insertPs = koneksi.prepareStatement("INSERT INTO students(nim, name, address, gender) VALUES(?,?,?,?)");
            insertPs.setString(1, nim);
            insertPs.setString(2, name);
            insertPs.setString(3, address);
            insertPs.setString(4, gender);

            int result = insertPs.executeUpdate();
            if (result > 0) {
                System.out.println("Data berhasil disimpan");
            } else {
                System.out.println("Data gagal disimpan");
            }
            }else{
                System.out.println(error);
                return;
            }
    


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (koneksi != null) {
                    koneksi.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

