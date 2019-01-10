package com.ThirdParty.MyModel.MyType;

public class ByteBufUtils {
    private int readInt = 0;
    private int writeInt = 0;
    private int availableSize = 0;
    private byte[] data;

    public ByteBufUtils(byte[] data)
    {
        this.data = data;
        this.availableSize = data.length;
    }

    public int readUnsignedByte()
    {
        return Byte.toUnsignedInt(this.data[(this.readInt++)]);
    }

    public int readableBytes()
    {
        return this.availableSize;
    }

    public int readUnsignedShort()
    {
        int high = Byte.toUnsignedInt(this.data[(this.readInt++)]);
        int low = Byte.toUnsignedInt(this.data[(this.readInt++)]);
        return (high << 8 & 0xFF00) + low;
    }

    public void readBytes(int start, int length, byte[] destArray)
    {
        System.arraycopy(this.data, start, destArray, 0, length);
        this.readInt += length;
    }

    public void readBytes(int length, byte[] destArray)
    {
        readBytes(this.readInt, length, destArray);
    }

    public void readBytes(byte[] destArray)
    {
        readBytes(this.readInt, destArray.length, destArray);
    }

    public ByteBufUtils writeByte(int number)
    {
        this.data[(this.writeInt++)] = ((byte)(number & 0xFF));
        return this;
    }

    public ByteBufUtils writeShort(int number)
    {
        byte high = (byte)((number & 0xFF00) >> 8);
        byte low = (byte)(number & 0xFF);
        this.data[(this.writeInt++)] = high;
        this.data[(this.writeInt++)] = low;
        return this;
    }

    public ByteBufUtils writeBytes(byte[] bytes)
    {
        System.arraycopy(bytes, 0, this.data, this.writeInt, bytes.length);
        return this;
    }

    public byte[] array()
    {
        return array();
    }
}
