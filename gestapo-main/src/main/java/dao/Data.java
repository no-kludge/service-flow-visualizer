package dao;

public class Data {
	int invocationId;
	int seqNo;
	String className;
	String method;
	String side;
	String service;
	
	public int getInvocationId() {
		return invocationId;
	}
	public void setInvocationId(int invocationId) {
		this.invocationId = invocationId;
	}
	public int getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getSide() {
		return side;
	}
	public void setSide(String side) {
		this.side = side;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	
	@Override
	public String toString() {
		return "Data [invocationId=" + invocationId + ", seqNo=" + seqNo
				+ ", className=" + className + ", method=" + method + ", side="
				+ side + ", service=" + service + "]";
	}		
}
