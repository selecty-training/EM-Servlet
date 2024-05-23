package com.selecty.example.page;

import javax.servlet.annotation.WebServlet;

import com.selecty.example.entity.Employee;
import com.selecty.example.page.base.BaseServlet;
import com.selecty.example.service.SearchService;
import com.selecty.example.service.UpdateService;
import com.selecty.example.service.UpdateService.UPDATE_MODE;

/**
 * 入力された情報で、データベースの情報を更新します
 * メールアドレスの更新を行う場合、重複があればエラーとし、確認用のパスワードと一致していない場合もエラーとなります
 *
 */
@WebServlet(name="update", urlPatterns={"/update"})
public class UpdateAction extends BaseServlet {

	@Override
	protected String getPageName() {
		return "updateInput";
	}

	@Override
	protected String doAction() throws Exception {
		// データを取得
		String[] pageParam = super.getInputParameter(
				 "empId"				// 0
				,"empNm"				// 1
				,"empKn"				// 2
				,"mail"					// 3
				,"pass"					// 4
				,"passConf"				// 5
				,"passOld"				// 6
				,"depId"				// 7
		);
		String pass = pageParam[6];

		if (!"".equals(pageParam[4]) || !"".equals(pageParam[5])) {
			if (!pageParam[4].equals(pageParam[5])) {
				throw new Exception("パスワードが一致していません");
			}
			pass = pageParam[4];
		}

		SearchService sService = new SearchService();
		if (sService.checkDuplicationMail(Integer.parseInt(pageParam[0]), pageParam[3])) {
			System.out.println("入力されたメールアドレスは既に存在しています");
			throw new Exception("入力されたメールアドレスは既に存在しています");
		}

		UpdateService uService= new UpdateService();
		Employee emp = new Employee();
		emp.setIdEmployee(Integer.parseInt(pageParam[0]));
		emp.setNmEmployee(pageParam[1]);
		emp.setKnEmployee(pageParam[2]);
		emp.setMailAddress(pageParam[3]);
		emp.setPassword(pass);
		emp.setIdDepartment(Integer.parseInt(pageParam[7]));

		uService.registEmployee(emp, UPDATE_MODE.UPDATE);
		return "updateResult";
	}
}
