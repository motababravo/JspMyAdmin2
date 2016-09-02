/**
 * 
 */
package com.jspmyadmin.app.database.users.controllers;

import org.json.JSONObject;

import com.jspmyadmin.app.database.users.beans.TablePrivilegeBean;
import com.jspmyadmin.app.database.users.logic.UserLogic;
import com.jspmyadmin.framework.constants.AppConstants;
import com.jspmyadmin.framework.constants.FrameworkConstants;
import com.jspmyadmin.framework.web.annotations.ValidateToken;
import com.jspmyadmin.framework.web.annotations.WebController;
import com.jspmyadmin.framework.web.utils.Controller;
import com.jspmyadmin.framework.web.utils.RequestLevel;
import com.jspmyadmin.framework.web.utils.View;
import com.jspmyadmin.framework.web.utils.ViewType;

/**
 * @author Yugandhar Gangu
 * @created_at 2016/07/15
 *
 */
@WebController(authentication = true, path = "/database_table_privileges.html", requestLevel = RequestLevel.DATABASE)
public class TablePrivilegesController extends Controller<TablePrivilegeBean> {

	private static final long serialVersionUID = 1L;

	@Override
	protected void handleGet(TablePrivilegeBean bean, View view) throws Exception {
		try {
			super.fillBasics(bean);
			UserLogic userLogic = new UserLogic();
			userLogic.fillTablePrivileges(bean);
			bean.setToken(super.generateToken());
			view.setType(ViewType.FORWARD);
			view.setPath(AppConstants.JSP_DATABASE_USERS_TABLEPRIVILEGES);
		} catch (Exception e) {
			view.setType(ViewType.REDIRECT);
			view.setPath(AppConstants.PATH_DATABASE_PRIVILEGES);
		}
	}

	@Override
	@ValidateToken
	protected void handlePost(TablePrivilegeBean bean, View view) throws Exception {
		if (FrameworkConstants.ONE.equals(bean.getFetch()) || FrameworkConstants.TWO.equals(bean.getFetch())) {
			this.handleGet(bean, view);
			return;
		}
		try {
			UserLogic userLogic = new UserLogic();
			userLogic.saveTablePrivileges(bean);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(FrameworkConstants.USER, bean.getUser());
			if (bean.getTable() != null) {
				jsonObject.put(FrameworkConstants.TABLE, bean.getTable());
			}
			view.setToken(jsonObject.toString());
			redirectParams.put(FrameworkConstants.ERR_KEY, AppConstants.MSG_SAVE_SUCCESS);
		} catch (Exception e) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(FrameworkConstants.USER, bean.getUser());
			view.setToken(jsonObject.toString());
			redirectParams.put(FrameworkConstants.ERR, e.getMessage());
		}
		view.setType(ViewType.REDIRECT);
		view.setPath(AppConstants.PATH_DATABASE_TABLE_PRIVILEGES);
	}

}