import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/myapplication")
public class myappication extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/university";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String rollNo = request.getParameter("roll_no");

        if (rollNo != null && !rollNo.trim().isEmpty()) {
            Connection conn = null;
            PreparedStatement stmt = null;
            ResultSet rs = null;

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

                String query = "SELECT * FROM form_requests WHERE roll_no = ?";
                stmt = conn.prepareStatement(query);
                stmt.setString(1, rollNo);
                rs = stmt.executeQuery();

                if (rs.next()) {
                    String name = rs.getString("name");
                    String stream = rs.getString("stream");
                    String branch = rs.getString("branch");
                    String contact = rs.getString("contact");
                    String formNeeded = rs.getString("form_needed");
                    String purpose = rs.getString("purpose");
                    String status = rs.getString("status");

                    out.println("<html><head><title>University Management System</title><link rel='stylesheet' href='styles.css'></head><body>");

                    out.println("<header><div class='logo-container'>");
                    out.println("<img src='university-logo.avif' height='100px' width='100px'><br><br>");
                    out.println("<h1><strong> SHMN University</strong></h1>");
                    out.println("</div><br><br>");
                    out.println("<nav>");
                    out.println("<a href='./Change Password.html'>Change Password</a>");
                    out.println("<a href='./fromrequest.html'>From Request</a> ");
                    out.println("<a href='myapplication?roll_no=" + rollNo + "' method='post'>myapplications</a>");
                    out.println("<a href='./feedetails.html'>Student fee</a>");
                    out.println("<a href='./index.html'>Log Out</a>");
                    out.println("</nav>");
                    out.println("</header>");

                    out.println("<h1>Application Details</h1>");
                    out.println("<p>Roll No: " + rollNo + "</p>");
                    out.println("<p>Name: " + name + "</p>");
                    out.println("<p>Stream: " + stream + "</p>");
                    out.println("<p>Branch: " + branch + "</p>");
                    out.println("<p>Contact: " + contact + "</p>");
                    out.println("<p>Form Needed: " + formNeeded + "</p>");
                    out.println("<p>Purpose: " + purpose + "</p>");
                    out.println("<p>Status: " + status + "</p>");

                    out.println("<footer>");
                    out.println("<div class='footer'>");
                    out.println("<div>");
                    out.println("<div class='footer-col-2'><img src='university-logo.avif' alt='my company name' style='max-width:80px; height:auto; border-radius:100%;' id='first'><h1><strong>SHMN University</strong></h1></div>");
                    out.println("</div></div>");
                    out.println("</footer>");

                    out.println("</body></html>");
                } else {
                    out.println("<html><body><h1>No application found for roll number: " + rollNo + "</h1></body></html>");
                }
            } catch (Exception e) {
                e.printStackTrace();
                out.println("<html><body><h1>Error retrieving application details. Please try again later.</h1></body></html>");
            } finally {
                try { if (rs != null) rs.close(); } catch (Exception e) { e.printStackTrace(); }
                try { if (stmt != null) stmt.close(); } catch (Exception e) { e.printStackTrace(); }
                try { if (conn != null) conn.close(); } catch (Exception e) { e.printStackTrace(); }
            }
        } else {
            out.println("<html><body><h1>No roll number provided. Please provide a valid roll number in the URL.</h1></body></html>");
        }
    }
}
