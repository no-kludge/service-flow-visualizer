package dao;

public class ReqData {
	int seqNo;
	String fromServiceName;
	String fromClassName;
	String toServiceName;
	String toClassName;
	String methSig;
	public int getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}
	public String getFromServiceName() {
		return fromServiceName;
	}
	public void setFromServiceName(String fromServiceName) {
		this.fromServiceName = fromServiceName;
	}
	public String getFromClassName() {
		return fromClassName;
	}
	public void setFromClassName(String fromClassName) {
		this.fromClassName = fromClassName;
	}
	public String getToServiceName() {
		return toServiceName;
	}
	public void setToServiceName(String toServiceName) {
		this.toServiceName = toServiceName;
	}
	public String getToClassName() {
		return toClassName;
	}
	public void setToClassName(String toClassName) {
		this.toClassName = toClassName;
	}
	public String getMethSig() {
		return methSig;
	}
	public void setMethSig(String methSig) {
		this.methSig = methSig;
	}
	
}
