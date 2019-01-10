package com.ThirdParty.MyModel.MyType;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.huawei.m2m.cig.tup.modules.protocol_adapter.IProtocolAdapter;

/**
 * Unit test for simple App.
 */
public class ProtocolServiceImplTest {

    private IProtocolAdapter protocolAdapter;

    @Before
    public void setProtocolAdapter() {
        this.protocolAdapter = new ProtocolAdapterImpl();
    }

    /**
     * 测试用例1：设备向平台上报数据。
     * <p>
     * <pre>
     * 设备上报数据:AA72000032088D0320623399
     * </pre>
     *
     * @throws Exception
     */
    @Test
    public void testDecodeDeviceReportData() throws Exception {
        byte[] deviceReqByte = initDeviceReqByte();
        ObjectNode objectNode = protocolAdapter.decode(deviceReqByte);
        String str = objectNode.toString();
        System.out.println(str);
    }

    /**
     * 测试用例2：平台向设备下发控制命令:
     * <p>
     * <pre>
     * {
     * //"identifier": "123",
     * "msgType": "cloudReq",
     * "cmd": "SET_DEVICE_LEVEL",
     * "mid": 2016,
     * "paras": { "value": "10" },
     * "hasMore": 0
     * }
     * </pre>
     */
    @Test
    public void testEncodeIoTSendCommand() throws Exception {
        ObjectNode CloudReqObjectNode = initCloudReqObjectNode();
        byte[] outputByte = protocolAdapter.encode(CloudReqObjectNode);
        System.out.println("cloudReq output:" + parseByte2HexStr(outputByte));
    }

    /**
     * 测试用例3：设备对平台命令的应答消息 有命令短id
     * <p>
     * <pre>
     * 设备应答消息:AA7201000107E0
     *
     * <pre>
     *
     * @throws Exception
     */
    @Test
    public void testDecodeDeviceResponseIoT() throws Exception {
        byte[] deviceRspByte = initDeviceRspByte();
        ObjectNode objectNode = protocolAdapter.decode(deviceRspByte);
        String str = objectNode.toString();
        System.out.println(str);
    }

    /**
     * 测试用例4：平台收到设备的上报数据后对设备的应答，如果不需要应答则返回null即可
     * <pre>
     * {
     * "identifier": "0",
     * "msgType": "cloudRsp",
     * "request": [AA,72,00,00,32,08,8D,03,20,62,33,99],
     * "errcode": 0,
     * "hasMore": 0
     * }
     *
     * <pre>
     *
     * @throws Exception
     */
    @Test
    public void testEncodeIoTResponseDevice() throws Exception {
        byte[] deviceReqByte = initDeviceReqByte();
        ObjectNode cloudRspObjectNode = initCloudRspObjectNode(deviceReqByte);
        byte[] outputByte2 = protocolAdapter.encode(cloudRspObjectNode);
        System.out.println("cloudRsp output:" + parseByte2HexStr(outputByte2));
    }

    public static String parseByte2HexStr(byte[] buf) {
        if (null == buf) {
            return null;
        }

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /*
     * 初始化：设备数据上报码流
     */
    private static byte[] initDeviceReqByte() {
        /**
         * 本例入参： AA 72 00 00 32 08 8D 03 20 62 33 99
         */
        byte[] byteDeviceReq = new byte[12];
        byteDeviceReq[0] = (byte) 0xAA;
        byteDeviceReq[1] = (byte) 0x72;
        byteDeviceReq[2] = (byte) 0x00;
        byteDeviceReq[3] = (byte) 0x00;
        byteDeviceReq[4] = (byte) 0x32;
        byteDeviceReq[5] = (byte) 0x08;
        byteDeviceReq[6] = (byte) 0x8D;
        byteDeviceReq[7] = (byte) 0x03;
        byteDeviceReq[8] = (byte) 0x20;
        byteDeviceReq[9] = (byte) 0x62;
        byteDeviceReq[10] = (byte) 0x33;
        byteDeviceReq[11] = (byte) 0x99;
        return byteDeviceReq;
    }

    /*
     * 初始化：平台向设备命令下发数据
     */
    private static ObjectNode initCloudReqObjectNode() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode cloudReqObjectNode = mapper.createObjectNode();
        ObjectNode paras = mapper.createObjectNode();
        paras.put("value", "10");
        cloudReqObjectNode.put("identifier", "123");
        cloudReqObjectNode.put("msgType", "cloudReq");
        cloudReqObjectNode.put("cmd", "SET_DEVICE_LEVEL");
        cloudReqObjectNode.put("paras", paras);
        cloudReqObjectNode.put("hasMore", 0);
        cloudReqObjectNode.put("mid", 2016);
        return cloudReqObjectNode;
    }

    /*
     * 初始化：设备对平台的响应码流
     */
    private static byte[] initDeviceRspByte() {
        /*
         * 测试用例：有命令短mid 设备应答消息:AA7201000107E0
         */
        byte[] byteDeviceRsp = new byte[12];
        byteDeviceRsp[0] = (byte) 0xAA;
        byteDeviceRsp[1] = (byte) 0x72;
        byteDeviceRsp[2] = (byte) 0x01;
        byteDeviceRsp[3] = (byte) 0x00;
        byteDeviceRsp[4] = (byte) 0x01;
        byteDeviceRsp[5] = (byte) 0x07;
        byteDeviceRsp[6] = (byte) 0xE0;
        return byteDeviceRsp;
    }

    /*
     * 初始化：平台对设备的应答数据
     */
    private static ObjectNode initCloudRspObjectNode(byte[] device2CloudByte) {

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode cloudRspObjectNode = mapper.createObjectNode();
        cloudRspObjectNode.put("identifier", "123");
        cloudRspObjectNode.put("msgType", "cloudRsp");
        // 设备上报的码流
        cloudRspObjectNode.put("request", device2CloudByte);
        cloudRspObjectNode.put("errcode", 0);
        cloudRspObjectNode.put("hasMore", 0);
        return cloudRspObjectNode;
    }
}
