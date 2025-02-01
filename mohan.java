import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/InsertStudent")
public class mohan extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // JDBC URL, username, and password of MySQL server
    private static final String URL = "jdbc:mysql://localhost:3306/university";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve form parameters
        String regNo = request.getParameter("reg_no");
        String name = request.getParameter("name");
        String dob = request.getParameter("DOB");
        String contact = request.getParameter("contact");
        String email = request.getParameter("Email");
        String fatherName = request.getParameter("father_name");
        String motherName = request.getParameter("mother_name");
        String parentContact = request.getParameter("parent_contact");
        String stream = request.getParameter("stream");
        String branch = request.getParameter("branch");
        String password = request.getParameter("name");
        
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establish database connection
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            
            // SQL insert statement
            String sql = "INSERT INTO student_details(roll_no, name, DOB, phone, email, father_name, mother_name, parent_phone, stream, branch, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            // Create PreparedStatement
            pstmt = conn.prepareStatement(sql);
            
            // Set parameters
            pstmt.setString(1, regNo);
            pstmt.setString(2, name);
            pstmt.setDate(3, java.sql.Date.valueOf(dob));
            pstmt.setString(4, contact);
            pstmt.setString(5, email);
            pstmt.setString(6, fatherName);
            pstmt.setString(7, motherName);
            pstmt.setString(8, parentContact);
            pstmt.setString(9, stream);
            pstmt.setString(10, branch);
            pstmt.setString(11,password);
            
            // Execute the statement
            int rowsInserted = pstmt.executeUpdate();
            
            // Redirect to RetrieveStudentServlet with roll_no as a parameter
            if (rowsInserted > 0) {
                response.sendRedirect("RetrieveServlet?roll_no=" + regNo);
            } else {
                response.setContentType("text/html");
                response.getWriter().println("Failed to insert student information.");
            }
            
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.getWriter().println("MySQL JDBC Driver not found. Include it in your library path.");
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("SQL Error: " + e.getMessage());
        } finally {
            // Close resources
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
