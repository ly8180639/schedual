package org.schedual.server.cache;

import java.util.List;

//存放redis的UserSession
public class UserSession {
	
	//user唯一标示（跟钉钉无关）
	private String userId;
	
	//userCode
	private String userCode;
	
	//公司id
	private String corpId;
	
	//用户头像地址
	private String avatar;
	
	//用户姓名
	private String userName;
	
	//用户能管理的所有部门
	private List<Integer> partIds;
	
	
	
	
	
	//是不是管理员
	private Boolean isAdmin;
	
	

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getCorpId() {
		return corpId;
	}
	
	

	public List<Integer> getPartIds() {
		return partIds;
	}

	public void setPartIds(List<Integer> partIds) {
		this.partIds = partIds;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	

	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	
	
}
