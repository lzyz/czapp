package com.czapp;

/**
 * @author daij
 * @version 1.0 状态实体
 */
public class WashingRoomPojo {

	private static final long serialVersionUID = -8379602526383806649L;
	public static final String TAG = "WashingRoomPojo";
	public int wrPojoId;
	public String wrAreaName;

	public String getFtw() {
		return Ftw;
	}

	public WashingRoomPojo setFtw(String ftw) {
		Ftw = ftw;
		return this;
	}

	public String getTxl() {
		return Txl;
	}

	public WashingRoomPojo setTxl(String txl) {
		Txl = txl;
		return this;
	}

	public String Txl;

	public String getFxl() {
		return Fxl;
	}

	public WashingRoomPojo setFxl(String fxl) {
		Fxl = fxl;
		return this;
	}

	public String Fxl;

	public String getTxy() {
		return Txy;
	}

	public WashingRoomPojo setTxy(String txy) {
		Txy = txy;
		return this;
	}

	public String Txy;

	public String getFxy() {
		return Fxy;
	}

	public WashingRoomPojo setFxy(String fxy) {
		Fxy = fxy;
		return this;
	}

	public String Fxy;

	public String getTtw() {
		return Ttw;
	}

	public WashingRoomPojo setTtw(String ttw) {
		Ttw = ttw;
		return this;
	}

	public String Ttw;
	public String Ftw;



	public int getWrPojoId() {
		return wrPojoId;
	}

	public WashingRoomPojo setWrPojoId(int wrPojoId) {
		this.wrPojoId = wrPojoId;
		return this;
	}

	public String getWrAreaName() {
		return wrAreaName;
	}

	public WashingRoomPojo setWrAreaName(String wrAreaName) {
		this.wrAreaName = wrAreaName;
		return this;
	}

}
