package com.mzeat.api;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.baidu.platform.comapi.map.m;
import com.google.gson.Gson;

import com.mzeat.MzeatApplication;
import com.mzeat.http.HttpClient;
import com.mzeat.http.Response;
import com.mzeat.model.Advs;
import com.mzeat.model.EditInfoReturn;
import com.mzeat.model.MyOrderItem;

import com.mzeat.model.MyOrederGood;

import com.mzeat.model.BaseModel;
import com.mzeat.model.BindQQReturn;
import com.mzeat.model.CardActivate;
import com.mzeat.model.Change;
import com.mzeat.model.ChangeReturn;
import com.mzeat.model.Comment;
import com.mzeat.model.CommentReturn;
import com.mzeat.model.Comments;
import com.mzeat.model.EditUserFace;
import com.mzeat.model.Invite;
import com.mzeat.model.InviteReturn;
import com.mzeat.model.My_share;
import com.mzeat.model.Page;
import com.mzeat.model.Privilege;
import com.mzeat.model.PrivilegeItem;
import com.mzeat.model.PubShare;
import com.mzeat.model.QQ_Login_Return;
import com.mzeat.model.RegistInfo;
import com.mzeat.model.Sale;
import com.mzeat.model.SaleReturn;
import com.mzeat.model.Share;
import com.mzeat.model.ShareDetail;
import com.mzeat.model.ShareItem;
import com.mzeat.model.ShareItemImgs;
import com.mzeat.model.Shopping;
import com.mzeat.model.Signin;
import com.mzeat.model.U_commentlist;
import com.mzeat.model.U_commentlist_item;
import com.mzeat.model.User;
import com.mzeat.util.JsonUtil;

public class MzeatService implements IMzeatService {

	private final String ServerUrl = "http://www.mzeat.com/mapi";
	private HttpClient mHttpClient;

	public static final int RESULT_OK = 1;
	public static final int RESULT_FAILE = 0;
	public static final int RESULT_ERROR = 2;
	public static final int RESULT_NODATA = 3;

	/**
	 * 构造方法
	 */
	public MzeatService() {
		mHttpClient = new HttpClient();
	}

	@Override
	public Map<String, Object> getPoster(String act, String r_type) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		ArrayList<Advs> advss = new ArrayList<Advs>();
		String url = String.format("%s/index.php?", ServerUrl);
		ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("act", act));
		params.add(new BasicNameValuePair("r_type", r_type));
		try {
			Response response = mHttpClient.post(url, params);
			// Log.e("response", response.toString());
			JSONObject jobj = response.asJSONObject();
			// Log.e("jobj", jobj.toString());
			String code = jobj.getString("open");
			// Log.e("code", code);

			if (code.equals("1")) {
				JSONArray jarray = jobj.getJSONArray("advs");

				// Log.e("jarray", jarray.toString());
				if (jarray != null && jarray.length() > 0) {
					for (int i = 0; i < jarray.length(); i++) {
						Advs advs = (Advs) JsonUtils.toBean(
								jarray.getString(i), Advs.class);
						advss.add(advs);

					}
					map.put("code", RESULT_OK);
					map.put("advs", advss);
				}
			} else if (code.equals("0")) {
				map.put("code", RESULT_FAILE);
			}

		} catch (Exception ex) {
			// TODO: handle exception
			ex.printStackTrace();
			map.put("code", RESULT_ERROR);
			return map;
		}
		return map;
	}

	@Override
	public Map<String, Object> getShoppingList(String act, String r_type,
			String page, String cate_id, String longitude, String latitude,
			String listgps, String keyword) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		ArrayList<Shopping> shoppings = new ArrayList<Shopping>();
		String url = String.format("%s/index.php?", ServerUrl);
		ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("act", act));
		params.add(new BasicNameValuePair("r_type", r_type));
		params.add(new BasicNameValuePair("page", page));
		params.add(new BasicNameValuePair("cate_id", cate_id));
		params.add(new BasicNameValuePair("m_latitude", latitude));
		params.add(new BasicNameValuePair("m_longitude", longitude));
		params.add(new BasicNameValuePair("listgps", listgps));
		params.add(new BasicNameValuePair("keyword", keyword));
		try {
			Response response = mHttpClient.post(url, params);
			// Log.e("response", response.toString());
			JSONObject jobj = response.asJSONObject();
			// Log.e("jobj", jobj.toString());
			String code = jobj.getString("open");
			// Log.e("code", code);
			Page mPage = (Page) JsonUtils.toBean(jobj.getJSONObject("page")
					.toString(), Page.class);
			map.put("page", mPage);
			// Log.e("page", mPage.getPage());
			JSONArray jarray = jobj.getJSONArray("item");
			if (code.equals("1")) {
				if (jarray != null && jarray.length() > 0) {

					// Log.e("jarray", jarray.toString());

					for (int i = 0; i < jarray.length(); i++) {

						Shopping shopping = (Shopping) JsonUtils.toBean(
								jarray.getString(i), Shopping.class);

						shoppings.add(shopping);

					}
					map.put("code", RESULT_OK);
					map.put("item", shoppings);
				} else {
					map.put("code", RESULT_NODATA);
				}
			} else {
				map.put("code", RESULT_FAILE);
			}

		} catch (Exception ex) {
			// TODO: handle exception
			ex.printStackTrace();
			map.put("code", RESULT_ERROR);
			return map;
		}
		return map;
	}

	@Override
	public User getUser(String act, String r_type, String email, String pwd) {
		// TODO Auto-generated method stub

		User user = new User();
		String url = String.format("%s/index.php?", ServerUrl);
		ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("act", act));
		params.add(new BasicNameValuePair("r_type", r_type));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("pwd", pwd));
		try {
			Response response = mHttpClient.post(url, params);
			// Log.e("response", response.toString());
			JSONObject jobj = response.asJSONObject();
			//Log.e("jobj", jobj.toString());
			String code = jobj.getString("user_login_status");
			// Log.e("code", code);

			if (code.equals("1")) {

				user = (User) JsonUtils.toBean(jobj.toString(), User.class);

			} else {
				user.setUser_login_status("0");
			}

		} catch (Exception ex) {
			// TODO: handle exception
			user.setUser_login_status("2");
			return user;
		}

		return user;
	}

	@Override
	public EditInfoReturn getEditInfoReturn(String email, String pwd,
			String b_day, String sex, String mobile, String new_password) {
		// TODO Auto-generated method stub
		EditInfoReturn result = new EditInfoReturn();
		String url = String.format("%s/index.php?", ServerUrl);
		ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("act", "editaccount"));
		params.add(new BasicNameValuePair("r_type", "1"));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("pwd", pwd));
		params.add(new BasicNameValuePair("b_day", b_day));
		params.add(new BasicNameValuePair("sex", sex));
		params.add(new BasicNameValuePair("mobile", mobile));
		params.add(new BasicNameValuePair("new_password", new_password));
		try {
			Response response = mHttpClient.post(url, params);
			// Log.e("response", response.toString());
			JSONObject jobj = response.asJSONObject();
			//Log.e("jobj", jobj.toString());
			String code = jobj.getString("user_info_return");
			// Log.e("code", code);

			if (code.equals("修改成功！")) {

				result = (EditInfoReturn) JsonUtils.toBean(jobj.toString(),
						EditInfoReturn.class);

			} else {
				result.setOpen("0");
			}

		} catch (Exception ex) {
			// TODO: handle exception
			result.setOpen("2");
			return result;
		}

		return result;
	}

	@Override
	public RegistInfo getRegist(String email, String password, String mobile,
			String user_name) {
		// TODO Auto-generated method stub
		RegistInfo result = new RegistInfo();
		String url = String.format("%s/index.php?", ServerUrl);
		ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("act", "register"));
		params.add(new BasicNameValuePair("r_type", "1"));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("mobile", mobile));
		params.add(new BasicNameValuePair("user_name", user_name));
		params.add(new BasicNameValuePair("qq_id", MzeatApplication.getInstance().getpPreferencesConfig().getString("qq_id", "")));
		try {
			Response response = mHttpClient.post(url, params);
			// Log.e("response", response.toString());
			JSONObject jobj = response.asJSONObject();
			//Log.e("jobj", jobj.toString());
			String code = jobj.getString("open");
			//Log.e("code", code);

			if (code.equals("1")) {

				result = (RegistInfo) JsonUtils.toBean(jobj.toString(),
						RegistInfo.class);

				//Log.e("result", result.getInfo());
			} else {
				result.setOpen("0");
				result.setInfo(jobj.getString("info"));
			}

		} catch (Exception ex) {
			// TODO: handle exception
			result.setOpen("2");
			return result;
		}

		return result;
	}

	@Override
	public Signin getSignin() {

		Signin signin = new Signin();
		// TODO Auto-generated method stub

		String url = String.format("%s/index.php?", ServerUrl);
		ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("act", "signin"));
		params.add(new BasicNameValuePair("r_type", "1"));
		params.add(new BasicNameValuePair("email", MzeatApplication
				.getInstance().getpPreferencesConfig().getString("email", "")));
		params.add(new BasicNameValuePair("pwd", MzeatApplication.getInstance()
				.getpPreferencesConfig().getString("pwd", "")));
		try {
			Response response = mHttpClient.post(url, params);
			// Log.e("response", response.toString());
			JSONObject jobj = response.asJSONObject();
			//Log.e("jobj", jobj.toString());
			String code = jobj.getString("open");
			//Log.e("code", code);

			if (code.equals("1")) {

				signin = (Signin) JsonUtils.toBean(jobj.toString(),
						Signin.class);

				//Log.e("result", signin.getInfo());
			} else {
				signin.setOpen("0");
			}

		} catch (Exception ex) {
			// TODO: handle exception
			signin.setOpen("2");
			return signin;
		}
		return signin;
	}

	@Override
	public Map<String, Object> getMyOrder(String page) {
		Map<String, Object> map = new HashMap<String, Object>();
		// TODO Auto-generated method stub
		ArrayList<MyOrderItem> myOrder = new ArrayList<MyOrderItem>();
		String url = String.format("%s/index.php?", ServerUrl);
		ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("act", "my_order_list"));
		params.add(new BasicNameValuePair("r_type", "1"));
		params.add(new BasicNameValuePair("page", page));
		params.add(new BasicNameValuePair("email", MzeatApplication
				.getInstance().getpPreferencesConfig().getString("email", "")));
		params.add(new BasicNameValuePair("pwd", MzeatApplication.getInstance()
				.getpPreferencesConfig().getString("pwd", "")));
		// PostersResponse res = null;
		try {
			Response response = mHttpClient.post(url, params);
			// Log.e("response", response.toString());
			JSONObject jobj = response.asJSONObject();
			//Log.e("jobj", jobj.toString());
			String code = jobj.getString("open");
			// ("code", code);
			Page mPage = (Page) JsonUtils.toBean(jobj.getJSONObject("page")
					.toString(), Page.class);
			map.put("page", mPage);
			JSONArray jarray = jobj.getJSONArray("item");
			// Log.e("jarray", jarray.toString());
			if (code.equals("1")) {
				if (jarray != null && jarray.length() > 0) {

					// Log.e("jarray", jarray.toString());

					for (int i = 0; i < jarray.length(); i++) {
						MyOrderItem myOrderItem = new MyOrderItem();
						Gson gson = new Gson();

						JSONObject item = jarray.getJSONObject(i);
						myOrderItem.setId(item.getString("id"));
						myOrderItem.setSn(item.getString("sn"));
						myOrderItem.setCreate_time_format(item
								.getString("create_time_format"));
						myOrderItem.setTotal_money(item
								.getString("total_money"));
						myOrderItem.setMoney(item.getString("money"));
						myOrderItem.setTotal_money_format(item
								.getString("total_money_format"));
						myOrderItem.setMoney_format(item
								.getString("money_format"));
						myOrderItem.setStatus(item.getString("status"));
						myOrderItem.setNum(item.getString("num"));

						JSONArray goods = item.getJSONArray("orderGoods");
						ArrayList<MyOrederGood> orderGoods = new ArrayList<MyOrederGood>();
						for (int j = 0; j < goods.length(); j++) {
							orderGoods.add(JsonUtil.fromJson(
									goods.getString(j), MyOrederGood.class));

						}
						myOrderItem.setOrderGoods(orderGoods);
						//Log.e("sn", myOrderItem.getCreate_time_format());
						//Log.e("sn", myOrderItem.getOrderGoods().get(0)
						//		.getName());

						// myOrderItem = (MyOrderItem)
						// JsonUtil.fromJson(jarray.get(i)
						// .toString(), MyOrderItem.class);

						myOrder.add(myOrderItem);
					}
					map.put("item", myOrder);
					map.put("code", RESULT_OK);
				} else {
					map.put("code", RESULT_NODATA);
				}

			} else {
				map.put("code", RESULT_FAILE);
			}

		} catch (Exception ex) {
			// TODO: handle exception
			ex.printStackTrace();
			map.put("code", RESULT_ERROR);
		}
		return map;
	}

	@Override
	public Privilege getPrivilege(String email, String pwd, String page,
			String m_latitude, String m_longitude,String keyword) {
		// TODO Auto-generated method stub
		Privilege privilege = new Privilege();
		String url = String.format("%s/index.php?", ServerUrl);
		ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("act", "nearbygoodses"));
		params.add(new BasicNameValuePair("r_type", "1"));
		params.add(new BasicNameValuePair("page", page));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("pwd", pwd));
		params.add(new BasicNameValuePair("m_latitude", m_latitude));
		params.add(new BasicNameValuePair("m_longitude", m_longitude));
		params.add(new BasicNameValuePair("keyword", keyword));
		try {
			Response response = mHttpClient.post(url, params);
			// Log.e("response", response.toString());
			JSONObject jobj = response.asJSONObject();
			//Log.e("jobj", jobj.toString());
			String code = jobj.getString("open");
			Page mPage = (Page) JsonUtils.toBean(jobj.getJSONObject("page")
					.toString(), Page.class);
			privilege.setPage(mPage);
			JSONArray jarray = jobj.getJSONArray("item");
			//Log.e("jarray", jarray.toString());
			//Log.e("code", code);

			if (code.equals("1") && jarray != null && jarray.length() > 0) {

				privilege.setOpen("1");
				ArrayList<PrivilegeItem> mItems = new ArrayList<PrivilegeItem>();
				for (int i = 0; i < jarray.length(); i++) {
					mItems.add(JsonUtil.fromJson(jarray.get(i).toString(),
							PrivilegeItem.class));

				}

				privilege.setItem(mItems);

				// privilege = JsonUtil.fromJson(jobj.toString(),
				// Privilege.class);

			} else {
				privilege.setOpen("0");
			}
		} catch (Exception ex) {
			// TODO: handle exception
			ex.printStackTrace();
			privilege.setOpen("2");
			return privilege;
		}
		return privilege;
	}

	@Override
	public Share getShareList(String page,String tag) {
		// TODO Auto-generated method stub
		Share share = new Share();
		String url = String.format("%s/index.php?", ServerUrl);
		ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("act", "sharelist"));
		params.add(new BasicNameValuePair("r_type", "1"));
		params.add(new BasicNameValuePair("page", page));
		params.add(new BasicNameValuePair("email", MzeatApplication
				.getInstance().getpPreferencesConfig().getString("email", "")));
		params.add(new BasicNameValuePair("pwd", MzeatApplication.getInstance()
				.getpPreferencesConfig().getString("pwd", "")));
		params.add(new BasicNameValuePair("is_new", "1"));
		params.add(new BasicNameValuePair("tag",tag));
		try {
			Response response = mHttpClient.post(url, params);
			// Log.e("response", response.toString());
			JSONObject jobj = response.asJSONObject();
			// Log.e("jobj", jobj.toString());
			String code = jobj.getString("return");
			Page mPage = (Page) JsonUtils.toBean(jobj.getJSONObject("page")
					.toString(), Page.class);
			share.setPage(mPage);
			JSONArray jarray = jobj.getJSONArray("item");
			// Log.e("jarray", jarray.toString());
			// Log.e("code", code);

			if (code.equals("1") && jarray != null && jarray.length() > 0) {

				share.setOpen("1");
				LinkedList<ShareItem> mItems = new LinkedList<ShareItem>();
				for (int i = 0; i < jarray.length(); i++) {
					
					ShareItem item = JsonUtil.fromJson(jarray.get(i).toString(), ShareItem.class);
					mItems.add(item);
					/**
					ShareItem item = new ShareItem();
					JSONObject obj = jarray.getJSONObject(i);
					item.setShare_id(obj.getString("share_id"));
					item.setUser_avatar(obj.getString("user_avatar"));
					item.setUser_name(obj.getString("user_name"));
					item.setCreate_time(obj.getString("create_time"));
					item.setTitle(obj.getString("title"));
					item.setContent(obj.getString("content"));
					item.setReply_count(obj.getString("reply_count"));
					ArrayList<String> img = new ArrayList<String>();
					JSONArray array = obj.getJSONArray("img");
					// Log.e("array",array.toString());
					for (int j = 0; j < array.length(); j++) {
						String img_url = array.getString(j);
						//Log.e("img_url", img_url);
						img.add(img_url);
					}
					mItems.add(item);
					**/

				}

				share.setItem(mItems);

				// privilege = JsonUtil.fromJson(jobj.toString(),
				// Privilege.class);

			} else {
				share.setOpen("0");
			}
		} catch (Exception ex) {
			// TODO: handle exception
			ex.printStackTrace();
			share.setOpen("2");
			return share;
		}
		return share;
	}

	@Override
	public ShareDetail getShareDetail(String share_id,String comment_id) {
		// TODO Auto-generated method stub
		ShareDetail shareDetail = new ShareDetail();
		String url = String.format("%s/index.php?", ServerUrl);
		ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("act", "share"));
		params.add(new BasicNameValuePair("r_type", "1"));
		params.add(new BasicNameValuePair("email", MzeatApplication
				.getInstance().getpPreferencesConfig().getString("email", "")));
		params.add(new BasicNameValuePair("pwd", MzeatApplication.getInstance()
				.getpPreferencesConfig().getString("pwd", "")));
		params.add(new BasicNameValuePair("share_id", share_id));
		params.add(new BasicNameValuePair("comment_id", comment_id));
		try {
			Response response = mHttpClient.post(url, params);
			 //Log.e("response", response.toString());
			JSONObject jobj = response.asJSONObject();
			// Log.e("jobj", jobj.toString());
			String code = jobj.getString("open");
			JSONObject item = jobj.getJSONObject("item");
			//Log.e("item", item.toString());
			// Log.e("code", code);

			if (code.equals("1") && item != null) {

				shareDetail.setOpen("1");

				shareDetail.setShare_id(item.getString("share_id"));

				shareDetail.setUid(item.getString("uid"));
				shareDetail.setUser_avatar(item.getString("user_avatar"));
				shareDetail.setUser_name(item.getString("user_name"));

				shareDetail.setTitle(item.getString("title"));
				shareDetail.setContent(item.getString("content"));
				shareDetail.setTime(item.getString("time"));
				shareDetail.setComment_count(item.getString("comment_count"));

				Comments mComments = new Comments();
				JSONObject comments = item.getJSONObject("comments");

				Page page = JsonUtil.fromJson(comments.getJSONObject("page")
						.toString(), Page.class);
				//Log.e("page", page.getPage());
				mComments.setPage(page);

				JSONArray list = comments.getJSONArray("list");
				List<Comment> comment = new ArrayList<Comment>();
				if (list != null && list.length() > 0) {
					for (int i = 0; i < list.length(); i++) {
						Comment com = new Comment();
						com = JsonUtil.fromJson(list.get(i).toString(),
								Comment.class);
						Log.e("com", com.getParent_id());
						comment.add(com);
					}
					mComments.setList(comment);
				}

				shareDetail.setComments(mComments);

				JSONArray mImgs = item.getJSONArray("imgs");
				List<ShareItemImgs> imgs = new ArrayList<ShareItemImgs>();
				if (mImgs != null && mImgs.length() > 0) {
					for (int i = 0; i < mImgs.length(); i++) {
						ShareItemImgs img = new ShareItemImgs();
						img = JsonUtil.fromJson(mImgs.get(i).toString(),
								ShareItemImgs.class);
						//Log.e("img", img.getSmall_img());
						imgs.add(img);
					}
					shareDetail.setImgs(imgs);
				}

				// privilege = JsonUtil.fromJson(jobj.toString(),
				// Privilege.class);

			} else {
				shareDetail.setOpen("0");
			}
		} catch (Exception ex) {
			// TODO: handle exception
			ex.printStackTrace();
			shareDetail.setOpen("2");
			return shareDetail;
		}
		return shareDetail;
	}

	@Override
	public CardActivate getCardActivate(String mzeatno, String mzeatpwd,
			String true_name, String mobile) {
		// TODO Auto-generated method stub

		CardActivate cardActivate = new CardActivate();
		// TODO Auto-generated method stub

		String url = String.format("%s/index.php?", ServerUrl);
		ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("act", "registervip"));
		params.add(new BasicNameValuePair("r_type", "1"));
		params.add(new BasicNameValuePair("email", MzeatApplication
				.getInstance().getpPreferencesConfig().getString("email", "")));
		params.add(new BasicNameValuePair("password", MzeatApplication
				.getInstance().getpPreferencesConfig().getString("pwd", "")));

		params.add(new BasicNameValuePair("mzeatno", mzeatno));
		params.add(new BasicNameValuePair("mzeatpwd", mzeatpwd));
		params.add(new BasicNameValuePair("true_name", true_name));
		params.add(new BasicNameValuePair("mobile", mobile));
		try {
			Response response = mHttpClient.post(url, params);
			// Log.e("response", response.toString());
			JSONObject jobj = response.asJSONObject();
			//Log.e("jobj", jobj.toString());
			String code = jobj.getString("open");
			//Log.e("code", code);

			if (code.equals("1")) {

				cardActivate = (CardActivate) JsonUtils.toBean(jobj.toString(),
						CardActivate.class);

				//Log.e("result", cardActivate.getUser_info_return());
			} else {
				cardActivate.setOpen("0");
				cardActivate.setUser_info_return(jobj
						.getString("user_info_return"));
			}

		} catch (Exception ex) {
			// TODO: handle exception
			cardActivate.setOpen("2");
			return cardActivate;
		}
		return cardActivate;
	}

	@Override
	public EditUserFace getUserFace(File file) {
		// TODO Auto-generated method stub
		EditUserFace editUserFace = new EditUserFace();
		// TODO Auto-generated method stub

		String url = String.format("%s/index.php?", ServerUrl);
		ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("act", "avatar"));
		params.add(new BasicNameValuePair("r_type", "1"));
		params.add(new BasicNameValuePair("email", MzeatApplication
				.getInstance().getpPreferencesConfig().getString("email", "")));
		params.add(new BasicNameValuePair("pwd", MzeatApplication.getInstance()
				.getpPreferencesConfig().getString("pwd", "")));

		try {
			Response response = mHttpClient.post(url, params, file,"image_1");
			// Log.e("response", response.toString());
			JSONObject jobj = response.asJSONObject();
			//Log.e("jobj", jobj.toString());
			String code = jobj.getString("rdd");
			//Log.e("code", code);

			if (code != null) {

				editUserFace.setOpen("1");

			} else {
				editUserFace.setOpen("0");

			}

		} catch (Exception ex) {
			// TODO: handle exception
			editUserFace.setOpen("2");
			return editUserFace;
		}
		return editUserFace;
	}

	@Override
	public CommentReturn getCommentReturn(String share_id, String is_relay,
			String content, String parent_id) {
		// TODO Auto-generated method stub
		CommentReturn commentReturn = new CommentReturn();
		// TODO Auto-generated method stub

		String url = String.format("%s/index.php?", ServerUrl);
		ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("act", "addcomment"));
		params.add(new BasicNameValuePair("r_type", "1"));
		params.add(new BasicNameValuePair("email", MzeatApplication
				.getInstance().getpPreferencesConfig().getString("email", "")));
		params.add(new BasicNameValuePair("pwd", MzeatApplication.getInstance()
				.getpPreferencesConfig().getString("pwd", "")));
		params.add(new BasicNameValuePair("share_id", share_id));
		params.add(new BasicNameValuePair("is_relay", is_relay));
		params.add(new BasicNameValuePair("content", content));
		params.add(new BasicNameValuePair("parent_id", parent_id));

		try {
			Response response = mHttpClient.post(url, params);
			// Log.e("response", response.toString());
			JSONObject jobj = response.asJSONObject();
			//Log.e("jobj", jobj.toString());
			String code = jobj.getString("status");
			//Log.e("code", code);

			if (code.equals("1")) {

				commentReturn.setOpen("1");

			} else {
				commentReturn.setOpen("0");

			}

		} catch (Exception ex) {
			// TODO: handle exception
			commentReturn.setOpen("2");
			return commentReturn;
		}
		return commentReturn;
	}

	@Override
	public PubShare getPubShare(String content, String title,
			ArrayList<File> file) {
		// TODO Auto-generated method stub
		PubShare pubShare = new PubShare();
		// TODO Auto-generated method stub

		String url = String.format("%s/index.php?", ServerUrl);
		ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("act", "addshare"));
		params.add(new BasicNameValuePair("r_type", "1"));
		params.add(new BasicNameValuePair("email", MzeatApplication
				.getInstance().getpPreferencesConfig().getString("email", "")));
		params.add(new BasicNameValuePair("pwd", MzeatApplication.getInstance()
				.getpPreferencesConfig().getString("pwd", "")));
		params.add(new BasicNameValuePair("title", title));
		params.add(new BasicNameValuePair("content", content));
		ArrayList<String> filenames = new ArrayList<String>();
		for (int i = 0; i < file.size(); i++) {
			String filename = "image_1";
			filenames.add(filename);
		}
		//Log.e("filenames", String.valueOf(filenames.size()));
		try {
			Response response = mHttpClient.post(url, params,file,filenames);
			// Log.e("response", response.toString());
			JSONObject jobj = response.asJSONObject();
			Log.e("jobj", jobj.toString());
			String code = jobj.getString("status");
			//String string = jobj.getString("string");
			//Log.e("string", string);

			if (code.equals("1")) {

				pubShare.setOpen("1");
				pubShare.setInfo(jobj.getString("info"));

			} else {
				pubShare.setOpen("0");
				pubShare.setInfo(jobj.getString("info"));

			}

		} catch (Exception ex) {
			// TODO: handle exception
			pubShare.setOpen("2");
			return pubShare;
		}
		return pubShare;
	}

	@Override
	public InviteReturn getInviteReturn(String page) {
		// TODO Auto-generated method stub
		InviteReturn inviteReturn = new InviteReturn();
		String url = String.format("%s/index.php?", ServerUrl);
		ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("act", "yczplist"));
		params.add(new BasicNameValuePair("r_type", "1"));
		params.add(new BasicNameValuePair("page", page));
		params.add(new BasicNameValuePair("email", MzeatApplication
				.getInstance().getpPreferencesConfig().getString("email", "")));
		params.add(new BasicNameValuePair("pwd", MzeatApplication.getInstance()
				.getpPreferencesConfig().getString("pwd", "")));
	
		try {
			Response response = mHttpClient.post(url, params);
			// Log.e("response", response.toString());
			JSONObject jobj = response.asJSONObject();
			// Log.e("jobj", jobj.toString());
			String code = jobj.getString("open");
			Page mPage = (Page) JsonUtils.toBean(jobj.getJSONObject("page")
					.toString(), Page.class);
			inviteReturn.setPage(mPage);
			JSONArray jarray = jobj.getJSONArray("item");
			// Log.e("jarray", jarray.toString());
			// Log.e("code", code);

			if (code.equals("1") && jarray != null && jarray.length() > 0) {

				inviteReturn.setOpen("1");
				ArrayList<Invite> mItems = new ArrayList<Invite>();
				for (int i = 0; i < jarray.length(); i++) {
					Invite item = new Invite();
					 Log.e("jarray", jarray.get(i).toString());
					item = JsonUtil.fromJson(jarray.get(i).toString(), Invite.class);
					mItems.add(item);
					Log.e("item",item.getAddress());
				}

				inviteReturn.setItem(mItems);

				

			} else {
				inviteReturn.setOpen("0");
			}
		} catch (Exception ex) {
			// TODO: handle exception
			ex.printStackTrace();
			inviteReturn.setOpen("2");
			return inviteReturn;
		}
		return inviteReturn;	}

	@Override
	public SaleReturn getSaleReturn(String page) {
		// TODO Auto-generated method stub
		SaleReturn saleReturn = new SaleReturn();
		String url = String.format("%s/index.php?", ServerUrl);
		ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("act", "noticelist"));
		params.add(new BasicNameValuePair("r_type", "1"));
		params.add(new BasicNameValuePair("page", page));
		params.add(new BasicNameValuePair("email", MzeatApplication
				.getInstance().getpPreferencesConfig().getString("email", "")));
		params.add(new BasicNameValuePair("pwd", MzeatApplication.getInstance()
				.getpPreferencesConfig().getString("pwd", "")));
	
		try {
			Response response = mHttpClient.post(url, params);
			// Log.e("response", response.toString());
			JSONObject jobj = response.asJSONObject();
			// Log.e("jobj", jobj.toString());
			String code = jobj.getString("open");
			Page mPage = (Page) JsonUtils.toBean(jobj.getJSONObject("page")
					.toString(), Page.class);
			saleReturn.setPage(mPage);
			JSONArray jarray = jobj.getJSONArray("item");
			// Log.e("jarray", jarray.toString());
			// Log.e("code", code);

			if (code.equals("1") && jarray != null && jarray.length() > 0) {

				saleReturn.setOpen("1");
				ArrayList<Sale> mItems = new ArrayList<Sale>();
				for (int i = 0; i < jarray.length(); i++) {
					Sale item = new Sale();
					 Log.e("jarray", jarray.get(i).toString());
					item = JsonUtil.fromJson(jarray.get(i).toString(), Sale.class);
					mItems.add(item);
					Log.e("item",item.getNotice_id());
				}

				saleReturn.setItem(mItems);

				

			} else {
				saleReturn.setOpen("0");
			}
		} catch (Exception ex) {
			// TODO: handle exception
			ex.printStackTrace();
			saleReturn.setOpen("2");
			return saleReturn;
		}
		return saleReturn;
	}

	@Override
	public ChangeReturn getChangeReturn(String page) {
		// TODO Auto-generated method stub
		ChangeReturn changeReturn = new ChangeReturn();
		String url = String.format("%s/index.php?", ServerUrl);
		ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("act", "goodslist"));
		params.add(new BasicNameValuePair("r_type", "1"));
		params.add(new BasicNameValuePair("page", page));
		params.add(new BasicNameValuePair("email", MzeatApplication
				.getInstance().getpPreferencesConfig().getString("email", "")));
		params.add(new BasicNameValuePair("pwd", MzeatApplication.getInstance()
				.getpPreferencesConfig().getString("pwd", "")));
	
		try {
			Response response = mHttpClient.post(url, params);
			// Log.e("response", response.toString());
			JSONObject jobj = response.asJSONObject();
			// Log.e("jobj", jobj.toString());
			String code = jobj.getString("open");
			Page mPage = (Page) JsonUtils.toBean(jobj.getJSONObject("page")
					.toString(), Page.class);
			changeReturn.setPage(mPage);
			JSONArray jarray = jobj.getJSONArray("item");
			// Log.e("jarray", jarray.toString());
			// Log.e("code", code);

			if (code.equals("1") && jarray != null && jarray.length() > 0) {

				changeReturn.setOpen("1");
				ArrayList<Change> mItems = new ArrayList<Change>();
				for (int i = 0; i < jarray.length(); i++) {
					Change item = new Change();
					 Log.e("jarray", jarray.get(i).toString());
					item = JsonUtil.fromJson(jarray.get(i).toString(), Change.class);
					mItems.add(item);
					Log.e("item",item.getGoods_id());
				}

				changeReturn.setItem(mItems);

				

			} else {
				changeReturn.setOpen("0");
			}
		} catch (Exception ex) {
			// TODO: handle exception
			ex.printStackTrace();
			changeReturn.setOpen("2");
			return changeReturn;
		}
		return changeReturn;
	}

	@Override
	public U_commentlist getU_commentlist(String email, String pwd) {
		// TODO Auto-generated method stub
		U_commentlist u_commentlist = new U_commentlist();
		String url = String.format("%s/index.php?", ServerUrl);
		ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("act", "u_commentlist"));
		params.add(new BasicNameValuePair("r_type", "1"));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("pwd", pwd));
	
		try {
			Response response = mHttpClient.post(url, params);
			// Log.e("response", response.toString());
			JSONObject jobj = response.asJSONObject();
			
			// Log.e("jobj", jobj.toString());
			//String code = jobj.getString("return");
			Page mPage = (Page) JsonUtils.toBean(jobj.getJSONObject("page")
					.toString(), Page.class);
			u_commentlist.setPage(mPage);
			
			String total = jobj.getString("total");
			JSONArray  jarray = jobj.getJSONArray("item");
			 //Log.e("jarray", jarray.toString());
			JSONObject my_share = jobj.getJSONObject("my_share");
			
			// Log.e("jarray", jarray.toString());
			// Log.e("code", code);

			if ( total  != null ) {

				
				if (  jarray != null && jarray.length() > 0) {
					ArrayList<U_commentlist_item> mItems = new ArrayList<U_commentlist_item>();
					for (int i = 0; i < jarray.length(); i++) {
						U_commentlist_item item = new U_commentlist_item();
						// Log.e("jarray", jarray.get(i).toString());
						item = JsonUtil.fromJson(jarray.get(i).toString(), U_commentlist_item.class);
						mItems.add(item);
						//Log.e("item",item.getShare_id());
					}

					u_commentlist.setItem(mItems);

				}
				u_commentlist.setTotal(total);
				int Status = Integer.valueOf(my_share.getString("Status"));
				if (Status != 0) {
					
					ArrayList<My_share>  mShares = new ArrayList<My_share>();
					
					for (int i = 0; i < my_share.length() -1; i++) {
						My_share item = new My_share();
						 //Log.e("jarray", mShares.get(i).toString());
						item = JsonUtil.fromJson(my_share.getJSONObject(String.valueOf(i)).toString(), My_share.class);
						mShares.add(item);
						//Log.e("item",item.getShare_id());
					}

					u_commentlist.setMy_share(mShares);

				} 
				u_commentlist.setOpen("1");
			}else {
				u_commentlist.setOpen("0");
			} 
		} catch (Exception ex) {
			// TODO: handle exception
			ex.printStackTrace();
			u_commentlist.setOpen("2");
			return u_commentlist;
		}
		return u_commentlist;
	}

	@Override
	public QQ_Login_Return getQq_Login_Return(String qq_id) {
		// TODO Auto-generated method stub
		QQ_Login_Return qq_Login_Return = new QQ_Login_Return();
		String url = String.format("%s/index.php?", ServerUrl);
		ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("act", "synclogin"));
		params.add(new BasicNameValuePair("r_type", "1"));
		params.add(new BasicNameValuePair("qq_id", qq_id));
		params.add(new BasicNameValuePair("login_type", "qq"));
	
		try {
			Response response = mHttpClient.post(url, params);
			JSONObject jobj = response.asJSONObject();
			
			 Log.e("jobj", jobj.toString());
			String code = jobj.getString("resulttype");
			
			

			if ( code.equals("1")) {

				qq_Login_Return = JsonUtil.fromJson(jobj.toString(), QQ_Login_Return.class);
			}else {
				qq_Login_Return.setResulttype("0");
			} 
		} catch (Exception ex) {
			// TODO: handle exception
			ex.printStackTrace();
			qq_Login_Return.setResulttype("2");
			return qq_Login_Return;
		}
		return qq_Login_Return;
	}

	@Override
	public BindQQReturn getBindQQReturn(String email, String pwd) {
		// TODO Auto-generated method stub
		BindQQReturn bindQQReturn = new BindQQReturn();
		String url = String.format("%s/index.php?", ServerUrl);
		ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("act", "syncbind"));
		params.add(new BasicNameValuePair("r_type", "1"));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("pwd", pwd));
		params.add(new BasicNameValuePair("login_type", "qq"));
		params.add(new BasicNameValuePair("qq_id", MzeatApplication.getInstance().getpPreferencesConfig().getString("qq_id", "")));

		try {
			Response response = mHttpClient.post(url, params);
			JSONObject jobj = response.asJSONObject();
			
			 Log.e("jobj", jobj.toString());
			String code = jobj.getString("return");
			
			

			if ( code.equals("1")) {
				bindQQReturn.setOpen("1");
				bindQQReturn.setInfo(jobj.getString("info"));
			}else {
				bindQQReturn.setOpen("0");
				bindQQReturn.setInfo(jobj.getString("info"));
			} 
		} catch (Exception ex) {
			// TODO: handle exception
			ex.printStackTrace();
			bindQQReturn.setOpen("2");
			return bindQQReturn;
		}
		return bindQQReturn;
	}

}
