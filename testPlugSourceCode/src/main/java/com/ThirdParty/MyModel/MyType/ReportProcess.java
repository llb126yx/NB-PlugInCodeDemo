package com.ThirdParty.MyModel.MyType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ReportProcess {
    //private String identifier;

    private String msgType = "deviceReq";
    private int hasMore = 0;
    private int errcode = 0;
    private byte bDeviceReq = 0x00;
    private byte bDeviceRsp = 0x01;
    
    //定义两个成员变量,BatteryLevel upData,存储解码数据。
    // 应根据profile中定义的字段来添加成员变量
    private int BatteryLevel = 0;
    private byte[] upData;

    private byte noMid = 0x00;
    private byte hasMid = 0x01;
    private boolean isContainMid = false;
    private int mid = 0;

    /**
     * 
     * @return
     */
    public ReportProcess(byte[] binaryData) {
		/* 设备上报数据格式为:前两个字节表示batteryLevel,大端;
		 * 第三个字节表示后续字节长度;
		 * 第四个字节到最后表示不定长负载数据,其长度由第三个字节的值表示
		 * 因此,数据长度的合法值是： binaryData[2] + 3
		 * BatteryLevel和upData是定义的成员变量.用来存储解码得到的数据。
		 */
    	int tempVal;
    	//长度校验，不合法则直接退出
    	if(binaryData.length < 3)
    	{
    		return;
    	}
    	tempVal = binaryData[2] & 0xFF;
    	if(binaryData.length < tempVal + 3)
    	{
    		return;
    	}
    	//计算batteryLevel,将前两个字节拼起来
    	tempVal = ((binaryData[0] << 8)&0xff00) + (binaryData[1] & 0xFF);
    	this.BatteryLevel = tempVal;
    	//取出不定长负载,存入upData字段
    	tempVal = binaryData[2] & 0xFF;
    	byte[] payload = new byte[tempVal];
    	System.arraycopy(binaryData, 3, payload, 0, tempVal);
        this.upData = payload;
    }

    public ObjectNode toJsonNode() {
        try {
            //组装body体
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode root = mapper.createObjectNode();

            // root.put("identifier", this.identifier);
            root.put("msgType", "deviceReq");

            ArrayNode arrynode = mapper.createArrayNode();

            //serviceId=Battery 数据组装
            ObjectNode serviceNode = mapper.createObjectNode();
            ObjectNode serviceDataNode = mapper.createObjectNode();
            serviceDataNode.put("BatteryLevel", this.BatteryLevel);
            serviceNode.put("serviceId", "Battery");
            serviceNode.set("serviceData", serviceDataNode);
            arrynode.add(serviceNode);
            //serviceId=Transmission 数据组装
            serviceNode = mapper.createObjectNode();
            serviceDataNode = mapper.createObjectNode();
            serviceDataNode.put("upData", this.upData);
            serviceNode.put("serviceId", "Transmission");
            serviceNode.set("serviceData", serviceDataNode);
            arrynode.add(serviceNode);

            root.set("data", arrynode);

            return root;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}