package com.selecty.example.page;

import javax.servlet.annotation.WebServlet;

import com.selecty.example.page.base.BaseServlet;
import com.selecty.example.service.SearchService;

/**
 * 社員登録情報の確認を行うページクラスです
 * メールアドレスが既に登録されているか判定します
 *
 */
@WebServlet(name="inputCheck", urlPatterns={"/inputCheck"})
public class InputCheckAction  extends BaseServlet {

	@Override
	protected String getPageName() {
		return "insert";
	}

	@Override
	protected String doAction() throws Exception {
		String[] pageParam = super.getInputParameter(
				 "empNm"	// 0
				,"empKn"	// 1
				,"mail"		// 2
				,"pass"		// 3
				,"depId"	// 4
			);
		
		
		SearchService service = new SearchService();
		if (service.checkDuplicationMail(null,pageParam[2])) {
			throw new Exception("入力されたメールアドレスは既に存在しています");
		}

		return "insertConfirm";
	}
}
