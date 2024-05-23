package com.selecty.example.page.base;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.selecty.example.entity.Department;
import com.selecty.example.entity.Employee;

public abstract class BaseServlet extends HttpServlet {
    //-------------------------------------- プロパティ群
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected HttpSession session;
    protected String message;

    @Override
    protected void service(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
        arg0.setCharacterEncoding("UTF-8");
        arg1.setContentType("text/html; charset=UTF-8");
        arg1.setCharacterEncoding("UTF-8");

        this.request = arg0;
        this.response = arg1;
        this.session = arg0.getSession();

        this.message = null;

        // 入力値を保持する
        this.keepInputValues();

        String nextPage = this.getPageName();
        try {
            if (!"login".equals(this.getPageName())) {
                if (session != null) {
                    Employee employee = (Employee) session.getAttribute("LOGIN_EMP");
                    @SuppressWarnings("unchecked")
                    List<Department> departmentList = (List<Department>) session.getAttribute("DEP_LIST");
                    if (
                            (employee == null || "".equals(employee.getNmEmployee()))
                                    ||
                                    (departmentList == null || departmentList.size() == 0)
                    ){
                        nextPage = "login.jsp";
                        throw new Exception("不正なログイン、またはログイン有効期間が過ぎています");
                    }
                }
            }

            nextPage = this.doAction();
        } catch (Exception e) {
            e.printStackTrace();
            message = e.getMessage();
            // エラーメッセージをリクエスト属性に設定
            request.setAttribute("message", message);
            // エラーページにフォワード
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        arg0.getRequestDispatcher(nextPage + ".jsp").forward(arg0, arg1);
    }

    //-------------------------------------- Utilメソッド群
    protected String[] getInputParameter(String... names) {
        String[] values = new String[names.length];
        for (int i = 0; i < names.length; i++) {
            values[i] = this.request.getParameter(names[i]);
        }
        return values;
    }

    // 入力値を保持する
    private void keepInputValues() {
        for (String parameterName : this.request.getParameterMap().keySet()) {
            String parameterValue = this.request.getParameter(parameterName);
            this.request.setAttribute(parameterName, parameterValue);
        }
    }

    //-------------------------------------- 抽象メソッド群
    protected abstract String getPageName();

    protected abstract String doAction() throws Exception;
}
