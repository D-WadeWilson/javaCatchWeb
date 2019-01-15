package dataobject;

import java.io.Serializable;
import java.util.Date;

public class CompanyInfo implements Serializable {
    private static final long serialVersionUID = 977384952931363555L;

    /**
     * 主键
     */
    private String id;

    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 联系人
     */
    private String linkman;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 电话
     */
    private String telephone;

    /**
     * 手机
     */
    private String mobilephone;

    /**
     * 状态 1 激活 0 冻结
     */
    private Integer status;

    /**
     * 所属区域
     */
    private String area;

    /**
     * 所属行业
     */
    private String trade;

    /**
     * 企业规模(0 300人以下；1 300-1000人；2 1000-5000人；3 5000-10000人；4 10000人以上)
     */
    private Integer scale;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 预留字段
     */
    private String mark1;

    /**
     * @return 主键
     */
    public String getId() {
        return id;
    }

    /**
     * @param id 
	 *            主键
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return 企业名称
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @param companyName 
	 *            企业名称
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * @return 用户名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName 
	 *            用户名
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return 联系人
     */
    public String getLinkman() {
        return linkman;
    }

    /**
     * @param linkman 
	 *            联系人
     */
    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    /**
     * @return 邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email 
	 *            邮箱
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return 电话
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * @param telephone 
	 *            电话
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * @return 手机
     */
    public String getMobilephone() {
        return mobilephone;
    }

    /**
     * @param mobilephone 
	 *            手机
     */
    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    /**
     * @return 状态 1 激活 0 冻结
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status 
	 *            状态 1 激活 0 冻结
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return 所属区域
     */
    public String getArea() {
        return area;
    }

    /**
     * @param area 
	 *            所属区域
     */
    public void setArea(String area) {
        this.area = area;
    }

    /**
     * @return 所属行业
     */
    public String getTrade() {
        return trade;
    }

    /**
     * @param trade 
	 *            所属行业
     */
    public void setTrade(String trade) {
        this.trade = trade;
    }

    /**
     * @return 企业规模(0 300人以下；1 300-1000人；2 1000-5000人；3 5000-10000人；4 10000人以上)
     */
    public Integer getScale() {
        return scale;
    }

    /**
     * @param scale 
	 *            企业规模(0 300人以下；1 300-1000人；2 1000-5000人；3 5000-10000人；4 10000人以上)
     */
    public void setScale(Integer scale) {
        this.scale = scale;
    }

    /**
     * @return 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime 
	 *            创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return 预留字段
     */
    public String getMark1() {
        return mark1;
    }

    /**
     * @param mark1 
	 *            预留字段
     */
    public void setMark1(String mark1) {
        this.mark1 = mark1;
    }
}