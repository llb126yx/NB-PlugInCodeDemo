package com.ThirdParty.MyModel.MyType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.huawei.m2m.cig.tup.modules.protocol_adapter.IProtocolAdapter;


public class ProtocolAdapterImpl implements IProtocolAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ProtocolAdapterImpl.class);
    // 厂商名称
    private static final String MANU_FACTURERID = "ThirdParty";
    // 设备型号
    private static final String MODEL = "MyModel";

    @Override
    public String getManufacturerId() {
        return MANU_FACTURERID;
    }

    @Override
    public String getModel() {
        return MODEL;
    }

    public void activate() {
        logger.info("Codec demo HttpMessageHander activated.");
    }

    public void deactivate() {
        logger.info("Codec demo HttpMessageHander deactivated.");
    }

    public byte[] encode(ObjectNode input) throws Exception {
        logger.info("dynamic lrbtest " + input.toString());
        try {
            CmdProcess cmdProcess = new CmdProcess(input);
            byte[] byteNode = cmdProcess.toByte();
            return byteNode;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ObjectNode decode(byte[] binaryData) throws Exception {
        try {
            ReportProcess lightProcess = new ReportProcess(binaryData);
            ObjectNode objectNode = lightProcess.toJsonNode();
            logger.info("dynamic lrbtest " + objectNode.toString());
            return objectNode;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
