package dataobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class CheckGrade implements Serializable {
    private static final long serialVersionUID = 376718900729406165L;

    /**
     * 主键
     */
    private String id;

    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 状态(0未上报 1暂存 2待审核 3审核通过 4审核驳回 5算分中)
     */
    private Integer status;

    /**
     * 企业规模(0 300人以下；1 300-1000人；2 1000-5000人；3 5000-10000人；4 10000人以上)
     */
    private Integer scale;

    /**
     * 行业
     */
    private String trade;

    /**
     * 问卷
     */
    private String questionnaire;

    /**
     * 评估得分
     */
    private BigDecimal score;

    /**
     * 评估时间
     */
    private Date evaluationTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 阶段 0待确定 1起步建设 2单项覆盖 3集成提升 4创新突破
     */
    private Integer step;

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
     * @return 状态(0未上报 1暂存 2待审核 3审核通过 4审核驳回 5算分中)
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status 
	 *            状态(0未上报 1暂存 2待审核 3审核通过 4审核驳回 5算分中)
     */
    public void setStatus(Integer status) {
        this.status = status;
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
     * @return 行业
     */
    public String getTrade() {
        return trade;
    }

    /**
     * @param trade 
	 *            行业
     */
    public void setTrade(String trade) {
        this.trade = trade;
    }

    /**
     * @return 问卷
     */
    public String getQuestionnaire() {
        return questionnaire;
    }

    /**
     * @param questionnaire 
	 *            问卷
     */
    public void setQuestionnaire(String questionnaire) {
        this.questionnaire = questionnaire;
    }

    /**
     * @return 评估得分
     */
    public BigDecimal getScore() {
        return score;
    }

    /**
     * @param score 
	 *            评估得分
     */
    public void setScore(BigDecimal score) {
        this.score = score;
    }

    /**
     * @return 评估时间
     */
    public Date getEvaluationTime() {
        return evaluationTime;
    }

    /**
     * @param evaluationTime 
	 *            评估时间
     */
    public void setEvaluationTime(Date evaluationTime) {
        this.evaluationTime = evaluationTime;
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
     * @return 阶段 0待确定 1起步建设 2单项覆盖 3集成提升 4创新突破
     */
    public Integer getStep() {
        return step;
    }

    /**
     * @param step 
	 *            阶段 0待确定 1起步建设 2单项覆盖 3集成提升 4创新突破
     */
    public void setStep(Integer step) {
        this.step = step;
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