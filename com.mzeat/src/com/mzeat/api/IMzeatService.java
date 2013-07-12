package com.mzeat.api;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import com.mzeat.api.service.PostersResponse;
import com.mzeat.model.BindQQReturn;
import com.mzeat.model.CardActivate;
import com.mzeat.model.ChangeReturn;
import com.mzeat.model.CommentReturn;
import com.mzeat.model.EditInfoReturn;
import com.mzeat.model.EditUserFace;
import com.mzeat.model.InviteReturn;
import com.mzeat.model.MyOrder;
import com.mzeat.model.MyOrderItem;
import com.mzeat.model.Privilege;
import com.mzeat.model.PubShare;
import com.mzeat.model.QQ_Login_Return;
import com.mzeat.model.RegistInfo;
import com.mzeat.model.SaleReturn;
import com.mzeat.model.Share;
import com.mzeat.model.ShareDetail;
import com.mzeat.model.Signin;
import com.mzeat.model.U_commentlist;
import com.mzeat.model.Update;
import com.mzeat.model.User;

public interface IMzeatService {
	Map<String, Object> getPoster(String act, String r_type);

	Map<String, Object> getShoppingList(String act, String r_type, String page,
			String cate_id, String longitude, String latitude, String listgps,
			String keyword);

	User getUser(String act, String r_type, String email, String pwd);

	EditInfoReturn getEditInfoReturn(String email, String pwd, String b_day,
			String sex, String mobile, String new_password);

	RegistInfo getRegist(String email, String password, String mobile,
			String user_name);

	Signin getSignin();

	Map<String, Object> getMyOrder(String page);

	Privilege getPrivilege(String email, String pwd, String page,
			String m_latitude, String m_longitude, String keyword);

	Share getShareList(String page, String tag);

	ShareDetail getShareDetail(String share_id,String comment_id);

	CardActivate getCardActivate(String mzeatno, String mzeatpwd,
			String true_name, String mobile);

	EditUserFace getUserFace(File file);

	CommentReturn getCommentReturn(String share_id, String is_relay,
			String content, String parent_id);

	PubShare getPubShare(String content, String title, ArrayList<File> file);

	InviteReturn getInviteReturn(String page);

	SaleReturn getSaleReturn(String page);

	ChangeReturn getChangeReturn(String page);

	U_commentlist getU_commentlist(String email, String pwd);

	
	QQ_Login_Return getQq_Login_Return(String qq_id);
	
	BindQQReturn getBindQQReturn(String email,String  pwd);
	
	Update checkVersion(String versionName);
	
}
