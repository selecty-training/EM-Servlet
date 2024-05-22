package com.selecty.example.page;

import javax.servlet.annotation.WebServlet;

import com.selecty.example.page.base.BaseServlet;
import com.selecty.example.service.UpdateService;

/**
 * 社員情報削除を行う画面に対応したページクラスです
 *
 */
@WebServlet(name="delete", urlPatterns={"/delete"})
public class DeleteAction extends BaseServlet {
	@Override
	protected String getPageName() {
		return "deleteConfirm";
	}

	@Override
	protected String doAction() throws Exception {

		String[] pageParam = super.getInputParameter(
				 "empId"		// 0
		);

		UpdateService service = new UpdateService();
		service.deleteEmployee(Integer.parseInt(pageParam[0]));

		return "deleteResult";
	}

}
