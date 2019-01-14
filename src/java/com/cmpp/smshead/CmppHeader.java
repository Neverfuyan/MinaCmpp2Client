package com.cmpp.smshead;
/**
 * 头文件
 * @author Zt
 *
 */
public class CmppHeader {
	private int Total_Length; 
	private int Command_Id;   
	private int Sequence_Id;
	
	public int getTotal_Length() {
		return Total_Length;
	}
	public void setTotal_Length(int total_Length) {
		Total_Length = total_Length;
	}
	public int getCommand_Id() {
		return Command_Id;
	}
	public void setCommand_Id(int command_Id) {
		Command_Id = command_Id;
	}
	public int getSequence_Id() {
		return Sequence_Id;
	}
	public void setSequence_Id(int sequence_Id) {
		Sequence_Id = sequence_Id;
	}
}
