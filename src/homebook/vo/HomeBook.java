package homebook.vo;

import java.sql.Timestamp;

public class HomeBook {
	private long serialno;
	private Timestamp day;
	private String section;
	private String accounttitle;
	private String remark;
	private long revenue;
	private long expense;
	public long getSerialno() {
		return serialno;
	}
	public void setSerialno(long serialno) {
		this.serialno = serialno;
	}
	public Timestamp getDay() {
		return day;
	}
	public void setDay(Timestamp day) {
		this.day = day;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public String getAccounttitle() {
		return accounttitle;
	}
	public void setAccounttitle(String accounttitle) {
		this.accounttitle = accounttitle;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public long getRevenue() {
		return revenue;
	}
	public void setRevenue(long revenue) {
		this.revenue = revenue;
	}
	public long getExpense() {
		return expense;
	}
	public void setExpense(long expense) {
		this.expense = expense;
	}
	@Override
	public String toString() {
		return "HomeBook [serialno=" + serialno + ", day=" + day + ", section=" + section + ", accounttitle="
				+ accounttitle + ", remark=" + remark + ", revenue=" + revenue + ", expense=" + expense + "]";
	}
	
	
}
