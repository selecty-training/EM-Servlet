package com.selecty.example.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.selecty.example.dao.DepartmentDao;
import com.selecty.example.dao.EmployeeDao;
import com.selecty.example.entity.Department;
import com.selecty.example.entity.Employee;
import com.selecty.example.entity.EmployeeInfo;
import com.selecty.example.service.base.BaseService;
import com.selecty.example.util.DbUtil;

public class SearchService extends BaseService {

	public SearchService() throws Exception {
		super(true);
	}

	/**
	 * メールアドレス、パスワードでログイン判定を行う
	 * @param mail
	 * @param pass
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> doLogin(String mail, String pass) throws Exception {
		Map<String, Object> loginData = new HashMap<>();
		EmployeeDao dao = new EmployeeDao(this.con);
		try {
			loginData.put("EMP", dao.selectLoginInfo(mail, pass));
			loginData.put("DEP_LIST", this.getDepartmentList());
		} finally {
			DbUtil.closeConnection(this.con);
		}
		return loginData;
	}

	/**
	 * 部署一覧を取得する
	 * @return
	 */
	private List<Department> getDepartmentList() throws Exception {
		DepartmentDao dao = new DepartmentDao(this.con);
		List<Department> depList = null;
		depList = dao.findAll();
		return depList;
	}

	/**
	 * 社員検索を行う
	 * @param form
	 * @return
	 */
	public List<EmployeeInfo> searchEmployeeInfo(Employee emp) throws Exception {
		EmployeeDao dao = new EmployeeDao(this.con);
		List<EmployeeInfo> empInfoList = null;
		try {
			empInfoList = dao.findByParam(emp);
			if (empInfoList.size() == 0) {
				empInfoList = null;
			}
		} finally {
			DbUtil.closeConnection(this.con);
		}
		return empInfoList;
	}

	/**
	 * 社員情報の主キー検索を行う
	 * @param empId
	 * @return
	 * @throws Exception
	 */
	public Employee searchEmployeeByPkey(Integer empId) throws Exception {
		EmployeeDao dao = new EmployeeDao(this.con);
		Employee employee = null;
		try {
			employee = dao.findByPramaryKey(empId);
		} finally {
			DbUtil.closeConnection(this.con);
		}
		return employee;
	}

	/**
	 * メールアドレスの重複チェック
	 * @param id
	 * @param mail
	 * @return
	 * @throws Exception
	 */
	public boolean checkDuplicationMail(Integer id, String mail) throws Exception {
		// メールアドレスで検索を行う
		EmployeeDao dao = new EmployeeDao(this.con);
		try {
			Employee emp = dao.findByMail(mail);

			if (emp != null) {
				if (id == emp.getIdEmployee()) {
					return true;
				}
			}
		} finally {
			DbUtil.closeConnection(this.con);
		}
		return false;
	}

}
