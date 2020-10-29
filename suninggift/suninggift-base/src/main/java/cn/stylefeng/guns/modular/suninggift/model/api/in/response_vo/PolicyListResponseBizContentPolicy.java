package cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo;

import java.util.List;
import lombok.Data;

import java.util.Date;

@Data
public class PolicyListResponseBizContentPolicy {

	private String approve;//审批者

	private String checkFlag;//明细类型：1-下单明细2-垫支明细3-趸交明细4-分期明细5-退款明细

	private Date instDate;

	private String keyStr;//明细标识

	private Integer level;//明细等级	0-下单明细，政策明细开始

	private String name;//明细名称

	private String policyNo;

	private Integer policySpecStatus;

	private String prnId;//父节点ID,0级政策改字段为0

	private String psnId;//政策明细编码

	private String remarkInfo;//明细说明

	private Date updtDate;

	private String valueStr;//明细内容

	//子政策集合
	List<PolicyListResponseBizContentPolicy> childList;

}
