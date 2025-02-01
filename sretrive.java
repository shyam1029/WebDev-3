import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/SRetrieveServlet")
public class sretrive extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String roll_no = request.getParameter("roll_no");
        String name = request.getParameter("name");

        // Database connection parameters
        String jdbcURL = "jdbc:mysql://localhost:3306/university";
        String jdbcUsername = "root";
        String jdbcPassword = "root";

        // JDBC objects
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the database connection
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);

            // SQL query to retrieve student details by registration number and password
            String sql = "SELECT * FROM student_details WHERE roll_no = ? AND name = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, roll_no);
            preparedStatement.setString(2, name);

            // Execute the query
            resultSet = preparedStatement.executeQuery();
            

            if (resultSet.next()) {
                // Successful login, create a session
                HttpSession session = request.getSession();
                session.setAttribute("studentRollNo", roll_no);
                
                // Output student details
                out.println("<html><head><title>University Management System</title><link rel='stylesheet' href='styles.css'></head><body>");

                out.println(" <header><div class='logo-container'>");
                out.println(" <img src='university-logo.avif' height='100px' width='100px'><br><br>");
                out.println("<h1><strong> SHMN University</strong></h1>");
                out.println("</div><br><br>");
                out.println("<nav>");
                out.println("<a href='./fromrequest.html'>Form Request</a> ");
                out.println("<a href='myapplication?roll_no="+roll_no+"' method='post'>myapplications</a>");
                out.println("<a href='./RetrieveSubject.html'>Academic</a>");
                out.println("<a href='./feedetails.html'>Student fee</a>");
                out.println("<a href='./index.html'>Log Out</a>");
                out.println("</nav>");
                out.println("</header>");
                out.println("<div class='portfolio-container'>");
                out.println("<div class='portfolio'>");
                out.println("<h1>Student Details</h1>");
                out.println("<div class='details'><strong>Roll Number:</strong> " + resultSet.getString("roll_no") + "</div>");
                out.println("<div class='details'><strong>Name:</strong> " + resultSet.getString("name") + "</div>");
                out.println("<div class='details'><strong>DOB:</strong> " + resultSet.getDate("DOB") + "</div>");
                out.println("<div class='details'><strong>Contact:</strong> " + resultSet.getString("phone") + "</div>");
                out.println("<div class='details'><strong>Email:</strong> " + resultSet.getString("email") + "</div>");
                out.println("<div class='details'><strong>Father Name:</strong> " + resultSet.getString("father_name") + "</div>");
                out.println("<div class='details'><strong>Mother Name:</strong> " + resultSet.getString("mother_name") + "</div>");
                out.println("<div class='details'><strong>Parent Contact:</strong> " + resultSet.getString("parent_phone") + "</div>");
                out.println("<div class='details'><strong>Stream:</strong> " + resultSet.getString("stream") + "</div>");
                out.println("<div class='details'><strong>Branch:</strong> " + resultSet.getString("branch") + "</div>");
                out.println("</div>");
                out.println("</div>");
                out.println("<footer> <div class='footer'> <div>  <div class='footer-col-2'><img src='university-logo.avif' alt='my company name' style='max-width:80px; height:auto; border-radius:100%; id='first'><h1><strong>SHMN University</strong></h1></div> </div></div></div></footer>");
                out.println("</body></html>");
            } else {
                // Failed login, redirect with error parameter
                response.sendRedirect("index.html?login=failed");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.sendRedirect("index.html?logi=failed");
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
