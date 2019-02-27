package com.danzhao.dto;

public class StuInfoDto {
	
 	private Integer stuid;

    private String stunumber;

    private String stuname;

    private String stusex;

    private String stutel1;

    private String stutel2;

    private String stutel3;

    private String ktype;

    private String zgtype;

    private String schoolname;
    
    private Integer deptid;
    
    private String deptname;
    
    private String profclass;

    private Integer profid;

    private String profname;
    
    private String stupwd;

    private String examnumber;

    private String idcard;

    private int erid;
    
    private String ername;
    
    private Integer roleid;
    
    private String testtime;
    
    private Integer stustatus;
    
    private Float totalscore;
    
    private String signature;
    
	public String getProfclass() {
		return profclass;
	}

	public void setProfclass(String profclass) {
		this.profclass = profclass;
	}

	public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Float getTotalscore() {
		return totalscore;
	}

	public void setTotalscore(Float totalscore) {
		this.totalscore = totalscore;
	}

	public Integer getStustatus() {
		return stustatus;
	}

	public void setStustatus(Integer stustatus) {
		this.stustatus = stustatus;
	}

	public String getTesttime() {
		return testtime;
	}

	public void setTesttime(String testtime) {
		this.testtime = testtime;
	}

	public Integer getRoleid() {
		return roleid;
	}

	public void setRoleid(Integer roleid) {
		this.roleid = roleid;
	}

	public Integer getStuid() {
		return stuid;
	}

	public void setStuid(Integer stuid) {
		this.stuid = stuid;
	}

	public String getStunumber() {
		return stunumber;
	}

	public void setStunumber(String stunumber) {
		this.stunumber = stunumber;
	}

	public String getStuname() {
		return stuname;
	}

	public void setStuname(String stuname) {
		this.stuname = stuname;
	}

	public String getStusex() {
		return stusex;
	}

	public void setStusex(String stusex) {
		this.stusex = stusex;
	}

	public String getStutel1() {
		return stutel1;
	}

	public void setStutel1(String stutel1) {
		this.stutel1 = stutel1;
	}

	public String getStutel2() {
		return stutel2;
	}

	public void setStutel2(String stutel2) {
		this.stutel2 = stutel2;
	}

	public String getStutel3() {
		return stutel3;
	}

	public void setStutel3(String stutel3) {
		this.stutel3 = stutel3;
	}

	public String getKtype() {
		return ktype;
	}

	public void setKtype(String ktype) {
		this.ktype = ktype;
	}

	public String getZgtype() {
		return zgtype;
	}

	public void setZgtype(String zgtype) {
		this.zgtype = zgtype;
	}

	public String getSchoolname() {
		return schoolname;
	}

	public void setSchoolname(String schoolname) {
		this.schoolname = schoolname;
	}

	public Integer getDeptid() {
		return deptid;
	}

	public void setDeptid(Integer deptid) {
		this.deptid = deptid;
	}

	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	public Integer getProfid() {
		return profid;
	}

	public void setProfid(Integer profid) {
		this.profid = profid;
	}

	public String getProfname() {
		return profname;
	}

	public void setProfname(String profname) {
		this.profname = profname;
	}

	public String getStupwd() {
		return stupwd;
	}

	public void setStupwd(String stupwd) {
		this.stupwd = stupwd;
	}

	public String getExamnumber() {
		return examnumber;
	}

	public void setExamnumber(String examnumber) {
		this.examnumber = examnumber;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public int getErid() {
		return erid;
	}

	public void setErid(int erid) {
		this.erid = erid;
	}

	public String getErname() {
		return ername;
	}

	public void setErname(String ername) {
		this.ername = ername;
	}

	@Override
	public String toString() {
		return "StuInfoDto [stunumber=" + stunumber + ", stuname=" + stuname
				+ ", stusex=" + stusex + ", deptid=" + deptid + ", profclass="
				+ profclass + ", profid=" + profid + ", profname=" + profname
				+ ", examnumber=" + examnumber + ", erid=" + erid + ", ername="
				+ ername + ", testtime=" + testtime + ", stustatus="
				+ stustatus + "]";
	}

}
