import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/AdminLogin")
public class adminl extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String reg_no = request.getParameter("reg_no");
        String password = request.getParameter("password");

        // Dummy check for admin credentials (replace with actual check)
        if ("admin".equals(reg_no) && "admin123".equals(password)) {
            HttpSession session = request.getSession();
            session.setAttribute("admin", "true");
            response.sendRedirect("Admin.html");
        } else {
            response.setContentType("text/html");
            response.getWriter().println("<script type=\"text/javascript\">");
            response.getWriter().println("alert('Invalid admin credentials');");
            response.getWriter().println("window.location.href = 'index.html';");
            response.getWriter().println("</script>");
        }
    }
}
